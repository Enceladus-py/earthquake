package com.kartaca.eqstream;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.connector.base.DeliveryGuarantee;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.formats.json.JsonDeserializationSchema;
import org.apache.flink.formats.json.JsonSerializationSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessAllWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class EarthquakeStreamJob {

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        JsonDeserializationSchema<Earthquake> jsondFormat = new JsonDeserializationSchema<>(Earthquake.class);

        KafkaSource<Earthquake> source = KafkaSource.<Earthquake>builder()
                .setBootstrapServers("kafka:9092")
                .setTopics("random-eq-topic")
                .setGroupId("flink")
                .setStartingOffsets(OffsetsInitializer.earliest())
                .setValueOnlyDeserializer(jsondFormat)
                .build();

        DataStream<Earthquake> eqClustered = env
                .fromSource(source, WatermarkStrategy
                        .<Earthquake>forBoundedOutOfOrderness(Duration.ofSeconds(20))
                        .withIdleness(Duration.ofSeconds(30)), "Kafka Source")
                .filter(eq -> eq.getMagnitude() >= 7.0)
                .windowAll(TumblingEventTimeWindows.of(Duration.ofSeconds(5)))
                .process(new ClusterEarthquakes())
                .name("Earthquake Clustering");

        JsonSerializationSchema<Earthquake> jsonsFormat = new JsonSerializationSchema<>();

        KafkaSink<Earthquake> sink = KafkaSink.<Earthquake>builder()
                .setBootstrapServers("kafka:9092")
                .setRecordSerializer(KafkaRecordSerializationSchema.builder()
                        .setTopic("random-eq-clustered")
                        .setValueSerializationSchema(jsonsFormat)
                        .build()
                )
                .setDeliveryGuarantee(DeliveryGuarantee.AT_LEAST_ONCE)
                .build();

        eqClustered.sinkTo(sink);
        env.execute("Earthquake Stream Job");
    }

    private static double calculateDistance(Earthquake eq1, Earthquake eq2) {
        // Earth's radius in kilometers
        final double R = 6371.0;

        double lat1 = Math.toRadians(eq1.getLat());
        double lon1 = Math.toRadians(eq1.getLon());
        double lat2 = Math.toRadians(eq2.getLat());
        double lon2 = Math.toRadians(eq2.getLon());

        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;

        double a = Math.pow(Math.sin(dLat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dLon / 2), 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

    private static class ClusterEarthquakes
            extends ProcessAllWindowFunction<Earthquake, Earthquake, TimeWindow> {

        @Override
        public void process(Context context, Iterable<Earthquake> eqList, Collector<Earthquake> out) {
            boolean discard = false;
            List<Earthquake> clustered = new ArrayList<>();
            for (Earthquake eq : eqList) {
                discard = false;
                for(Earthquake clusteredEq : clustered) {
                    double distance = calculateDistance(eq, clusteredEq);
                    if (distance < 50) {
                        discard = true;
                        break;
                    }
                }
                if (!discard) {
                    clustered.add(eq);
                    out.collect(eq);
                }
            }
        }
    }
}
