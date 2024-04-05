package com.kartaca.backend;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class EarthquakeTopic {

    public static final String TOPIC = "random-eq-topic";
    @Bean
    public NewTopic randomEarthquakeTopic() {
        return TopicBuilder.name(TOPIC)
                .partitions(10)
                .replicas(1)
                .build();
    }
}
