package com.example.smartcampus.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "sensors")
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sensorId;      // e.g. "S001"
    private String sensorType;    // e.g. "TEMPERATURE", "OCCUPANCY", "ENERGY"
    private String location;      // e.g. "A101"
    private String status;        // e.g. "ACTIVE", "INACTIVE"

    @ManyToOne
    @JoinColumn(name = "building_id")
    private Building building;

    public Sensor() {}

    public Sensor(String sensorId, String sensorType, String location, String status, Building building) {
        this.sensorId = sensorId;
        this.sensorType = sensorType;
        this.location = location;
        this.status = status;
        this.building = building;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSensorId() { return sensorId; }
    public void setSensorId(String sensorId) { this.sensorId = sensorId; }

    public String getSensorType() { return sensorType; }
    public void setSensorType(String sensorType) { this.sensorType = sensorType; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Building getBuilding() { return building; }
    public void setBuilding(Building building) { this.building = building; }
}