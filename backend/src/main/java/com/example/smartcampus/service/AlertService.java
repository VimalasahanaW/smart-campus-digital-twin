package com.example.smartcampus.service;

import com.example.smartcampus.entity.Alert;
import com.example.smartcampus.repository.AlertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlertService {

    @Autowired
    private AlertRepository alertRepository;

    public List<Alert> getAllAlerts() {
        return alertRepository.findAll();
    }

    public Alert createAlert(Alert alert) {
        return alertRepository.save(alert);
    }
    public Alert updateAlert(Long id, Alert updatedAlert) {
        Alert existing = alertRepository.findById(id).orElse(null);
        if (existing == null) {
            return null;
        }
        existing.setMessage(updatedAlert.getMessage());
        existing.setSeverity(updatedAlert.getSeverity());
        existing.setTimestamp(updatedAlert.getTimestamp());
        existing.setSensor(updatedAlert.getSensor());
        return alertRepository.save(existing);
    }

    public boolean deleteAlert(Long id) {
        if (!alertRepository.existsById(id)) {
            return false;
        }
        alertRepository.deleteById(id);
        return true;
    }
}