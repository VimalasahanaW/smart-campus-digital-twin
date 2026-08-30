package com.example.smartcampus.controller;

import com.example.smartcampus.entity.Prediction;
import com.example.smartcampus.service.PredictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    @PutMapping("/{id}")
    public ResponseEntity<Prediction> updatePrediction(@PathVariable Long id, @RequestBody Prediction prediction) {
        Prediction updated = predictionService.updatePrediction(id, prediction);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrediction(@PathVariable Long id) {
        boolean deleted = predictionService.deletePrediction(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}