package com.kartaca.backend;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class EarthquakeConsumer {

    public static final String consumerTopic = "random-eq-clustered";

    @KafkaListener(id = "backend", topics = consumerTopic)
    public void listen(Earthquake eq) {
        // TODO check against database again and connect to websocket
        System.out.println(eq.getId());
    }
}
