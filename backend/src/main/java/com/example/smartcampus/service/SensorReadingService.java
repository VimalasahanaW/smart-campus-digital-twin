package com.example.smartcampus.service;

import com.example.smartcampus.entity.SensorReading;
import com.example.smartcampus.repository.SensorReadingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SensorReadingService {

    @Autowired
    private SensorReadingRepository sensorReadingRepository;

    public List<SensorReading> getAllReadings() {
        return sensorReadingRepository.findAll();
    }

    public List<SensorReading> getReadingsBySensorId(String sensorId) {
        return sensorReadingRepository.findBySensor_SensorId(sensorId);
    }

    public SensorReading createReading(SensorReading reading) {
        return sensorReadingRepository.save(reading);
    }
}