package com.kartaca.backend;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
public class Earthquake {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @CreationTimestamp
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
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setLat(double lat) {
        this.lat = lat;
    }
    public void setLon(double lon) {
        this.lon = lon;
    }
    public void setMagnitude(double magnitude) {
        this.magnitude = magnitude;
    }
}
