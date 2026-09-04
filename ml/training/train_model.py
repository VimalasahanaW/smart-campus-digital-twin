import requests
import pandas as pd
from sklearn.linear_model import LinearRegression
import joblib
import os

API_BASE = "https://smart-campus-backend-yzt1.onrender.com"

def fetch_readings():
    response = requests.get(f"{API_BASE}/api/sensor-readings", timeout=60)
    response.raise_for_status()
    data = response.json()
    print(f"Fetched {len(data)} readings")
    return data

def prepare_dataframe(readings):
    rows = []
    for r in readings:
        rows.append({
            "value": r["value"],
            "timestamp": r["timestamp"],
            "sensor_type": r["sensor"]["sensorType"],
            "sensor_db_id": r["sensor"]["id"],
        })
    df = pd.DataFrame(rows)
    df["timestamp"] = pd.to_datetime(df["timestamp"])

    # Feature engineering
    df["hour"] = df["timestamp"].dt.hour
    df["day_of_week"] = df["timestamp"].dt.dayofweek  # 0=Monday, 6=Sunday
    df["is_class_hours"] = df["hour"].between(9, 17).astype(int)  # 1 if 9am-5pm

    return df

def train_for_sensor_type(df, sensor_type):
    subset = df[df["sensor_type"] == sensor_type]
    if len(subset) < 5:
        print(f"Not enough data for {sensor_type} ({len(subset)} rows) — skipping")
        return None

    X = subset[["hour", "day_of_week", "is_class_hours"]]
    y = subset["value"]

    model = LinearRegression()
    model.fit(X, y)

    print(f"Trained model for {sensor_type} on {len(subset)} rows "
          f"(features: hour, day_of_week, is_class_hours)")
    return model

if __name__ == "__main__":
    readings = fetch_readings()
    df = prepare_dataframe(readings)

    os.makedirs("../models", exist_ok=True)

    for sensor_type in ["TEMPERATURE", "OCCUPANCY", "ENERGY"]:
        model = train_for_sensor_type(df, sensor_type)
        if model is not None:
            filename = f"../models/{sensor_type.lower()}_model.pkl"
            joblib.dump(model, filename)
            print(f"Saved model to {filename}")

    print("\nTraining complete.")