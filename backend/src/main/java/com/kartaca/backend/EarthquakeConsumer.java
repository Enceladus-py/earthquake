package com.kartaca.backend;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class EarthquakeConsumer {

    private static final String consumerTopic = "random-eq-clustered";

    @Autowired
    private EarthquakeRepository eqRepo;

    @KafkaListener(id = "backend", topics = consumerTopic)
    public void listen(Earthquake eq) throws JsonProcessingException {
        if (eqRepo.findEarthquakesWithin50km(eq.getLat(), eq.getLon()).isEmpty()) {
            Earthquake savedEq = eqRepo.save(eq);
            EarthquakeSocketHandler.sendToAll(savedEq);
            System.out.println(savedEq.getId());
        }
        else {
            System.out.println("Not empty!");
        }
    }
}
