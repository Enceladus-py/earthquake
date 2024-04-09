package com.kartaca.backend;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EarthquakeRepository extends CrudRepository<Earthquake, Long> {
    List<Earthquake> findByCreatedAtBefore(LocalDateTime threshold);
    @Query(value = "SELECT * FROM Earthquake e WHERE 2 * 6371 * ATAN2(" +
            "SQRT(SIN(RADIANS((:latitude - e.lat) / 2)) ^ 2 + " +
            "COS(RADIANS(:latitude)) * COS(RADIANS(e.lat)) * SIN(RADIANS((:longitude - e.lon) / 2)) ^ 2)," +
            "SQRT(1 - (SIN(RADIANS((:latitude - e.lat) / 2)) ^ 2 +" +
            "COS(RADIANS(:latitude)) * COS(RADIANS(e.lat)) * SIN(RADIANS((:longitude - e.lon) / 2)) ^ 2))) <= 50",
            nativeQuery = true)
    List<Earthquake> findEarthquakesWithin50km(@Param("latitude") Double latitude, @Param("longitude") Double longitude);
}