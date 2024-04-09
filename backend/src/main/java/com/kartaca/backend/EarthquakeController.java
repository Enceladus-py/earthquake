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

    @PostMapping("/add")
    public ResponseEntity<String> createEarthquake(@RequestBody Earthquake eq) {
        eqProducer.sendMessage(eq);
        String message = "lat: " + eq.getLat() + " lon: " + eq.getLon() + " mag: " + eq.getMagnitude();
        return ResponseEntity.ok("eq sent with params:" + message);
    }

    @GetMapping("/deleted")
    public ResponseEntity<List<Long>> getDeletedEarthquakes() {
        List<Long> deletedEarthquakes = new ArrayList<>(scheduledTask.getDeletedEarthquakes());
        scheduledTask.clearDeletedIds();
        return new ResponseEntity<>(deletedEarthquakes, HttpStatus.OK);
    }
}
