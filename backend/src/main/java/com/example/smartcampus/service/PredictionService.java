package com.example.smartcampus.service;

import com.example.smartcampus.entity.Prediction;
import com.example.smartcampus.repository.PredictionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PredictionService {

    @Autowired
    private PredictionRepository predictionRepository;

    public List<Prediction> getAllPredictions() {
        return predictionRepository.findAll();
    }

    public List<Prediction> getPredictionsBySensorId(String sensorId) {
        return predictionRepository.findBySensor_SensorId(sensorId);
    }

    public Prediction createPrediction(Prediction prediction) {
        return predictionRepository.save(prediction);
    }
}