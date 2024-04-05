package com.kartaca.backend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/earthquakes")
public class EarthquakeController {

    @Autowired
    private EarthquakeRepository earthquakeRepository;

    @Autowired
    private ScheduledTask scheduledTask;

    @Autowired
    private KafkaProducer kafkaProducer;

    private static final double THRESHOLD = 7.0;

    @PostMapping("/add")
    public ResponseEntity<Earthquake> createEarthquake(@RequestBody Earthquake eq) {
        if (eq.getMagnitude() >= THRESHOLD) {
            kafkaProducer.sendMessage(eq);
        }
        Earthquake savedEq = earthquakeRepository.save(eq);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEq);
    }

    @GetMapping("/deleted")
    public ResponseEntity<List<Long>> getDeletedEarthquakes() {
        List<Long> deletedEarthquakes = new ArrayList<>(scheduledTask.getDeletedEarthquakes());
        scheduledTask.clearDeletedIds();
        return new ResponseEntity<>(deletedEarthquakes, HttpStatus.OK);
    }
}
