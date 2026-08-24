package com.example.smartcampus.controller;

import com.example.smartcampus.entity.SensorReading;
import com.example.smartcampus.service.SensorReadingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sensor-readings")
public class SensorReadingController {

    @Autowired
    private SensorReadingService sensorReadingService;

    @GetMapping
    public List<SensorReading> getReadings(@RequestParam(required = false) String sensorId) {
        if (sensorId != null) {
            return sensorReadingService.getReadingsBySensorId(sensorId);
        }
        return sensorReadingService.getAllReadings();
    }

    @PostMapping
    public SensorReading createReading(@RequestBody SensorReading reading) {
        return sensorReadingService.createReading(reading);
    }
}