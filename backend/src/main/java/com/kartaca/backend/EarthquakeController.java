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
    private ScheduledTask scheduledTask;

    @Autowired
    private EarthquakeProducer eqProducer;

    @Autowired
    private EarthquakeRepository eqRepo;

    @GetMapping("/list")
    public ResponseEntity<List<Earthquake>> getAllEarthquakes() {
        List<Earthquake> earthquakes = eqRepo.findAll();
        return ResponseEntity.ok(earthquakes);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addEarthquake(@RequestBody Earthquake eq) {
        eqProducer.sendMessage(eq);
        String message = "lat: " + eq.getLat() + " lon: " + eq.getLon() + " mag: " + eq.getMagnitude();
        return ResponseEntity.ok("eq sent with params:" + message);
    }

    @PostMapping("/create")
    public ResponseEntity<Earthquake> createEarthquake(@RequestBody Earthquake eq) {
        if (eq.getMagnitude() >= 7.0 && eqRepo.findEarthquakesWithin50km(eq.getLat(), eq.getLon()).isEmpty()) {
            Earthquake savedEq = eqRepo.save(eq);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedEq);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @GetMapping("/deleted")
    public ResponseEntity<List<Long>> getDeletedEarthquakes() {
        List<Long> deletedEarthquakes = new ArrayList<>(scheduledTask.getDeletedEarthquakes());
        scheduledTask.clearDeletedIds();
        return new ResponseEntity<>(deletedEarthquakes, HttpStatus.OK);
    }
}
