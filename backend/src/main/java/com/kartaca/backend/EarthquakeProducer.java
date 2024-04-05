package com.kartaca.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static com.kartaca.backend.EarthquakeTopic.TOPIC;
@Component
public class EarthquakeProducer {
    @Autowired
    private KafkaTemplate<String, Earthquake> kafkaTemplate;

    public void sendMessage(Earthquake eq) {
        kafkaTemplate.send(TOPIC, eq);
    }
}
