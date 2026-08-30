package com.example.smartcampus.service;

import com.example.smartcampus.entity.Sensor;
import com.example.smartcampus.repository.SensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SensorService {

    @Autowired
    private SensorRepository sensorRepository;

    public List<Sensor> getAllSensors() {
        return sensorRepository.findAll();
    }

    public Sensor createSensor(Sensor sensor) {
        return sensorRepository.save(sensor);
    }

    public Sensor getSensorById(Long id) {
        return sensorRepository.findById(id).orElse(null);
    }
    public Sensor updateSensor(Long id, Sensor updatedSensor) {
        Sensor existing = sensorRepository.findById(id).orElse(null);
        if (existing == null) {
            return null;
        }
        existing.setSensorId(updatedSensor.getSensorId());
        existing.setSensorType(updatedSensor.getSensorType());
        existing.setLocation(updatedSensor.getLocation());
        existing.setStatus(updatedSensor.getStatus());
        existing.setBuilding(updatedSensor.getBuilding());
        return sensorRepository.save(existing);
    }

    public boolean deleteSensor(Long id) {
        if (!sensorRepository.existsById(id)) {
            return false;
        }
        sensorRepository.deleteById(id);
        return true;
    }
}