import requests
import joblib
import os
from datetime import datetime, timedelta

API_BASE = "https://smart-campus-backend-yzt1.onrender.com"
PREDICTIONS_URL = f"{API_BASE}/api/predictions"
SENSORS_URL = f"{API_BASE}/api/sensors"

MODELS_DIR = "../models"

SENSOR_TYPE_TO_PREDICTION_TYPE = {
    "TEMPERATURE": "TEMPERATURE_FORECAST",
    "OCCUPANCY": "OCCUPANCY_FORECAST",
    "ENERGY": "ENERGY_FORECAST",
}

def fetch_active_sensors():
    """Get current sensors from the backend, grouped by type."""
    response = requests.get(SENSORS_URL, timeout=60)
    response.raise_for_status()
    sensors = response.json()
    return [s for s in sensors if s.get("status") == "ACTIVE"]

def load_model(sensor_type):
    """Load a previously trained model for this sensor type, if it exists."""
    filename = os.path.join(MODELS_DIR, f"{sensor_type.lower()}_model.pkl")
    if not os.path.exists(filename):
        print(f"No trained model found for {sensor_type} at {filename}")
        return None
    return joblib.load(filename)

def build_features(target_time):
    """Build the same 3 features the model was trained on, for a future timestamp."""
    hour = target_time.hour
    day_of_week = target_time.weekday()
    is_class_hours = 1 if 9 <= hour <= 17 else 0
    return [[hour, day_of_week, is_class_hours]]

def clip_to_realistic_range(sensor_type, value):
    """Prevent nonsensical predictions (e.g. negative occupancy)."""
    if sensor_type == "OCCUPANCY":
        return max(0, min(value, 100))
    elif sensor_type == "TEMPERATURE":
        return max(10, min(value, 45))
    elif sensor_type == "ENERGY":
        return max(0, min(value, 10))
    return value

def predict_and_post(sensor, model, target_time):
    sensor_type = sensor["sensorType"]
    prediction_type = SENSOR_TYPE_TO_PREDICTION_TYPE.get(sensor_type, "UNKNOWN_FORECAST")

    features = build_features(target_time)
    raw_value = float(model.predict(features)[0])
    predicted_value = round(clip_to_realistic_range(sensor_type, raw_value), 2)

    payload = {
        "predictedValue": predicted_value,
        "predictionType": prediction_type,
        "forDate": target_time.strftime("%Y-%m-%dT%H:%M:%S"),
        "createdAt": datetime.now().strftime("%Y-%m-%dT%H:%M:%S"),
        "sensor": {"id": sensor["id"]}
    }

    try:
        response = requests.post(PREDICTIONS_URL, json=payload, timeout=60)
        print(f"Sensor {sensor['id']} ({sensor_type}) -> predicted {predicted_value} "
              f"for {target_time.strftime('%Y-%m-%d %H:%M')} -> {response.status_code}")
    except requests.exceptions.RequestException as e:
        print(f"Error posting prediction: {e}")

if __name__ == "__main__":
    print("Generating predictions...")

    sensors = fetch_active_sensors()
    print(f"Found {len(sensors)} active sensors")

    # Predict 1 hour ahead from now
    target_time = datetime.now() + timedelta(hours=1)

    for sensor in sensors:
        sensor_type = sensor["sensorType"]
        model = load_model(sensor_type)
        if model is not None:
            predict_and_post(sensor, model, target_time)

    print("\nPrediction run complete.")