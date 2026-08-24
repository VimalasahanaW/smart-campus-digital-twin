# Smart Campus API Contract

Base URL (local dev): http://localhost:8080
Base URL (production): TBD once deployed

All responses are JSON.

---

## Building

### GET /api/buildings
Returns all buildings.

Response:
[
  {
    "id": 1,
    "name": "Main Block",
    "location": "Chennai",
    "buildingType": "Academic"
  }
]

### GET /api/buildings/{id}
Returns a single building by id.

### POST /api/buildings
Request body:
{
  "name": "Main Block",
  "location": "Chennai",
  "buildingType": "Academic"
}

---

## Sensor (coming next)

### GET /api/sensors
[
  {
    "id": 2,
    "sensorId": "S001",
    "sensorType": "TEMPERATURE",
    "location": "A101",
    "status": "ACTIVE",
    "building": {
      "id": 1,
      "name": "Main Block",
      "location": "Chennai",
      "buildingType": "Academic"
    }
  }
]

### POST /api/sensors
{
  "sensorId": "S001",
  "sensorType": "TEMPERATURE",
  "location": "A101",
  "status": "ACTIVE",
  "building": { "id": 1 }
}

---

## SensorReading (coming next)

### GET /api/sensor-readings?sensorId=S001
[
  {
    "id": 1,
    "sensorId": "S001",
    "value": 29.4,
    "timestamp": "2026-08-23T15:30:00"
  }
]

### POST /api/sensor-readings
{
  "sensorId": "S001",
  "value": 29.4,
  "timestamp": "2026-08-23T15:30:00"
}

---

## Prediction (ML writes here)

### GET /api/predictions
[
  {
    "id": 1,
    "predictedValue": 31.2,
    "predictionType": "TEMPERATURE_FORECAST",
    "forDate": "2026-08-23T18:00:00",
    "createdAt": "2026-08-23T16:00:00",
    "sensor": {
      "id": 2,
      "sensorId": "S001",
      "sensorType": "TEMPERATURE",
      "location": "A101",
      "status": "ACTIVE",
      "building": { "id": 1, "name": "Main Block", "location": "Chennai", "buildingType": "Academic" }
    }
  }
]

### POST /api/predictions
{
  "predictedValue": 31.2,
  "predictionType": "TEMPERATURE_FORECAST",
  "forDate": "2026-08-23T18:00:00",
  "createdAt": "2026-08-23T16:00:00",
  "sensor": { "id": 2 }
}

---

## Alert

### GET /api/alerts
[
  {
    "id": 1,
    "sensorId": "S001",
    "message": "Temperature exceeded 35°C",
    "severity": "HIGH",
    "timestamp": "2026-08-23T15:35:00"
  }
]