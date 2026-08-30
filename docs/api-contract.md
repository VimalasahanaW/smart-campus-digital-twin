# Smart Campus API Contract

Base URL (local dev): http://localhost:8080
Base URL (production): https://smart-campus-backend-yzt1.onrender.com

All responses are JSON. All entities support full CRUD (GET, POST, PUT, DELETE).

---

## Building

### GET /api/buildings
Returns all buildings.

Response:[
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
Request body:{
"name": "Main Block",
"location": "Chennai",
"buildingType": "Academic"
}

### PUT /api/buildings/{id}
Updates an existing building. Same body shape as POST. Returns 404 if id doesn't exist.

### DELETE /api/buildings/{id}
Deletes a building. Returns 204 No Content on success, 404 if id doesn't exist.

---

## Sensor

### GET /api/sensors
[
{
"id": 1,
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


### PUT /api/sensors/{id}
Updates an existing sensor. Same body shape as POST. Returns 404 if id doesn't exist.

### DELETE /api/sensors/{id}
Deletes a sensor. Returns 204 No Content on success, 404 if id doesn't exist.

**Live sensors currently in production:**
- id 1 → S001, TEMPERATURE
- id 2 → S002, OCCUPANCY
- id 3 → S003, ENERGY

---

## SensorReading

Readings are being generated continuously by a Python simulator and written to this 
live database right now.

### GET /api/sensor-readings
Returns all readings, most recent included.

[
{
"id": 8,
"value": 25.6,
"timestamp": "2026-08-29T21:55:40",
"sensor": {
"id": 1,
"sensorId": "S001",
"sensorType": "TEMPERATURE",
"location": "A101",
"status": "ACTIVE",
"building": { "id": 1, "name": "Main Block", "location": "Chennai", "buildingType": "Academic" }
}
}
]


### GET /api/sensor-readings?sensorId={sensorId}
Filters readings by a sensor's sensorId (e.g. "S001"). Same response shape as above.

### POST /api/sensor-readings

{
"value": 29.4,
"timestamp": "2026-08-23T15:30:00",
"sensor": { "id": 1 }
}

Note: POSTing a reading also triggers automatic threshold checks — see Alert section below.

### PUT /api/sensor-readings/{id}
Updates an existing reading. Same body shape as POST. Returns 404 if id doesn't exist.

### DELETE /api/sensor-readings/{id}
Deletes a reading. Returns 204 No Content on success, 404 if id doesn't exist.

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
"id": 1,
"sensorId": "S001",
"sensorType": "TEMPERATURE",
"location": "A101",
"status": "ACTIVE",
"building": { "id": 1, "name": "Main Block", "location": "Chennai", "buildingType": "Academic" }
}
}
]


### GET /api/predictions?sensorId={sensorId}
Filters predictions by a sensor's sensorId. Same response shape as above.

### POST /api/predictions

{
"predictedValue": 31.2,
"predictionType": "TEMPERATURE_FORECAST",
"forDate": "2026-08-23T18:00:00",
"createdAt": "2026-08-23T16:00:00",
"sensor": { "id": 1 }
}


### PUT /api/predictions/{id}
Updates an existing prediction. Same body shape as POST. Returns 404 if id doesn't exist.

### DELETE /api/predictions/{id}
Deletes a prediction. Returns 204 No Content on success, 404 if id doesn't exist.

**Note:** not populated yet — ML training hasn't started. Frontend can build the UI 
against this shape now; data will start appearing once training is complete.

---

## Alert

Alerts are created AUTOMATICALLY by the backend when a sensor reading crosses a 
threshold (temperature > 35°C, occupancy > 50, energy > 4.5 kW). No manual POST needed 
in normal operation — this endpoint is mainly for the frontend to READ alerts.

### GET /api/alerts

[
{
"id": 1,
"message": "Temperature exceeded 35°C at A101",
"severity": "HIGH",
"timestamp": "2026-08-24T14:02:28.901594",
"sensor": {
"id": 1,
"sensorId": "S001",
"sensorType": "TEMPERATURE",
"location": "A101",
"status": "ACTIVE",
"building": { "id": 1, "name": "Main Block", "location": "Chennai", "buildingType": "Academic" }
}
}
]


### POST /api/alerts

{
"message": "Temperature exceeded 35°C at A101",
"severity": "HIGH",
"timestamp": "2026-08-24T14:02:28",
"sensor": { "id": 1 }
}

Manual creation is supported but not typically needed — alerts are usually auto-generated.

### PUT /api/alerts/{id}
Updates an existing alert. Same body shape as POST. Returns 404 if id doesn't exist.

### DELETE /api/alerts/{id}
Deletes an alert. Returns 204 No Content on success, 404 if id doesn't exist.

---

## Notes for Frontend / ML integration

- **Cold starts:** this is free-tier hosting (Render + Aiven). If the backend hasn't 
  been used in a while, the first request can take 30-90 seconds to respond. This is 
  normal — not a bug.
- **Units:** Temperature is in °C, Occupancy is a headcount, Energy is in kW.
- **Live data:** sensor-readings and alerts are genuinely live and growing — a Python 
  simulator is running periodically, feeding realistic data based on time of day.

  