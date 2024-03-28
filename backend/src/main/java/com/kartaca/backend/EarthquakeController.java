package com.kartaca.backend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/earthquakes")
public class EarthquakeController {

    @Autowired
    private EarthquakeRepository earthquakeRepository;

    @PostMapping
    public ResponseEntity<Earthquake> createEarthquake(@RequestBody Earthquake eq) {
        Earthquake savedEq = earthquakeRepository.save(eq);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEq);
    }
}
