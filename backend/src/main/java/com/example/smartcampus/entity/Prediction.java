package com.example.smartcampus.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "predictions")
public class Prediction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double predictedValue;
    private String predictionType;   // e.g. "TEMPERATURE_FORECAST", "OCCUPANCY_FORECAST"
    private LocalDateTime forDate;   // the time this prediction is FOR
    private LocalDateTime createdAt; // when the prediction was generated

    @ManyToOne
    @JoinColumn(name = "sensor_id")
    private Sensor sensor;

    public Prediction() {}

    public Prediction(Double predictedValue, String predictionType, LocalDateTime forDate,
                      LocalDateTime createdAt, Sensor sensor) {
        this.predictedValue = predictedValue;
        this.predictionType = predictionType;
        this.forDate = forDate;
        this.createdAt = createdAt;
        this.sensor = sensor;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Double getPredictedValue() { return predictedValue; }
    public void setPredictedValue(Double predictedValue) { this.predictedValue = predictedValue; }

    public String getPredictionType() { return predictionType; }
    public void setPredictionType(String predictionType) { this.predictionType = predictionType; }

    public LocalDateTime getForDate() { return forDate; }
    public void setForDate(LocalDateTime forDate) { this.forDate = forDate; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Sensor getSensor() { return sensor; }
    public void setSensor(Sensor sensor) { this.sensor = sensor; }
}