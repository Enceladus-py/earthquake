package com.kartaca.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {

    private static final String TOPIC = "random-eq-topic";

    @Autowired
    private KafkaTemplate<String, Earthquake> kafkaTemplate;

    public void sendMessage(Earthquake eq) {
        kafkaTemplate.send(TOPIC, eq);
    }
}
