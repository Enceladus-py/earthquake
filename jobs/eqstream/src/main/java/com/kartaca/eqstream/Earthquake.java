package com.kartaca.eqstream;

import java.time.LocalDateTime;

public class Earthquake {
    private Long id;
    private LocalDateTime createdAt;
    private double lat;
    private double lon;
    private double magnitude;

    // getters and setters
    public double getLat() {
        return lat;
    }
    public double getLon() {
        return lon;
    }
    public double getMagnitude() {
        return magnitude;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public Long getId() {
        return id;
    }
}
