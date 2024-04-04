package com.kartaca.backend;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EarthquakeRepository extends CrudRepository<Earthquake, Long> {
    List<Earthquake> findByCreatedAtBefore(LocalDateTime threshold);
}
