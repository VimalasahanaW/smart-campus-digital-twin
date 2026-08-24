package com.example.smartcampus.repository;

import com.example.smartcampus.entity.SensorReading;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SensorReadingRepository extends JpaRepository<SensorReading, Long> {
    List<SensorReading> findBySensor_SensorId(String sensorId);
}