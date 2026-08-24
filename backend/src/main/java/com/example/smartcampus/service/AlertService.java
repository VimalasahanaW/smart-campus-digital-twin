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
}