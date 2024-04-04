package com.kartaca.backend;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTask {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTask.class);

    @Autowired
    private EarthquakeRepository earthquakeRepository;

    private final List<Long> deletedIds = new ArrayList<>();

    @Scheduled(fixedRate = 3600000) // Scheduled to run every hour
    public void removeOldEarthquakes() {
        LocalDateTime threshold = LocalDateTime.now().minusHours(12);
        List<Earthquake> oldEarthquakes = earthquakeRepository.findByCreatedAtBefore(threshold);
        for (Earthquake eq : oldEarthquakes) {
            deletedIds.add(eq.getId());
            earthquakeRepository.delete(eq);
        }
        log.info("Deleted earthquakes with IDs: {}", deletedIds);
    }

    public List<Long> getDeletedEarthquakes() {
        return deletedIds;
    }

    public void clearDeletedIds() {
        deletedIds.clear();
    }
}
