package com.example.smartcampus.repository;

import com.example.smartcampus.entity.Prediction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PredictionRepository extends JpaRepository<Prediction, Long> {
    List<Prediction> findBySensor_SensorId(String sensorId);
}