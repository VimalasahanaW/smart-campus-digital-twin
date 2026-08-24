package com.example.smartcampus.controller;

import com.example.smartcampus.entity.Prediction;
import com.example.smartcampus.service.PredictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/predictions")
public class PredictionController {

    @Autowired
    private PredictionService predictionService;

    @GetMapping
    public List<Prediction> getPredictions(@RequestParam(required = false) String sensorId) {
        if (sensorId != null) {
            return predictionService.getPredictionsBySensorId(sensorId);
        }
        return predictionService.getAllPredictions();
    }

    @PostMapping
    public Prediction createPrediction(@RequestBody Prediction prediction) {
        return predictionService.createPrediction(prediction);
    }
}