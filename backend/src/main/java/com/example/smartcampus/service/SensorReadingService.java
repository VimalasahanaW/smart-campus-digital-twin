package com.example.smartcampus.service;

import com.example.smartcampus.entity.Alert;
import com.example.smartcampus.entity.Sensor;
import com.example.smartcampus.entity.SensorReading;
import com.example.smartcampus.repository.SensorRepository;
import com.example.smartcampus.repository.SensorReadingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SensorReadingService {

    @Autowired
    private SensorReadingRepository sensorReadingRepository;

    @Autowired
    private SensorRepository sensorRepository;

    @Autowired
    private AlertService alertService;

    public List<SensorReading> getAllReadings() {
        return sensorReadingRepository.findAll();
    }

    public List<SensorReading> getReadingsBySensorId(String sensorId) {
        return sensorReadingRepository.findBySensor_SensorId(sensorId);
    }

    public SensorReading createReading(SensorReading reading) {
        SensorReading saved = sensorReadingRepository.save(reading);

        // fetch the FULL sensor from DB, since the request only sent its id
        if (saved.getSensor() != null && saved.getSensor().getId() != null) {
            Sensor fullSensor = sensorRepository.findById(saved.getSensor().getId()).orElse(null);
            if (fullSensor != null) {
                checkThresholdAndAlert(saved, fullSensor);
            }
        }

        return saved;
    }

    private void checkThresholdAndAlert(SensorReading reading, Sensor sensor) {
        if (sensor.getSensorType() == null || reading.getValue() == null) {
            return;
        }

        String sensorType = sensor.getSensorType();
        double value = reading.getValue();
        String message = null;
        String severity = null;

        if (sensorType.equals("TEMPERATURE") && value > 35.0) {
            message = "Temperature exceeded 35°C at " + sensor.getLocation();
            severity = "HIGH";
        } else if (sensorType.equals("OCCUPANCY") && value > 50) {
            message = "Occupancy exceeded capacity at " + sensor.getLocation();
            severity = "MEDIUM";
        } else if (sensorType.equals("ENERGY") && value > 4.5) {
            message = "Energy usage spike at " + sensor.getLocation();
            severity = "LOW";
        }

        if (message != null) {
            Alert alert = new Alert(message, severity, LocalDateTime.now(), sensor);
            alertService.createAlert(alert);
        }
    }
    public SensorReading updateReading(Long id, SensorReading updatedReading) {
        SensorReading existing = sensorReadingRepository.findById(id).orElse(null);
        if (existing == null) {
            return null;
        }
        existing.setValue(updatedReading.getValue());
        existing.setTimestamp(updatedReading.getTimestamp());
        existing.setSensor(updatedReading.getSensor());
        return sensorReadingRepository.save(existing);
    }

    public boolean deleteReading(Long id) {
        if (!sensorReadingRepository.existsById(id)) {
            return false;
        }
        sensorReadingRepository.deleteById(id);
        return true;
    }
}