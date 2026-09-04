import requests
import random
import time
from datetime import datetime

API_BASE = "https://smart-campus-backend-yzt1.onrender.com"
SENSORS_URL = f"{API_BASE}/api/sensors"
READINGS_URL = f"{API_BASE}/api/sensor-readings"

def fetch_active_sensors():
    """Query the backend to discover currently registered sensors."""
    try:
        response = requests.get(SENSORS_URL, timeout=60)
        response.raise_for_status()
        sensors = response.json()
        active = [
            {"id": s["id"], "type": s["sensorType"]}
            for s in sensors
            if s.get("status") == "ACTIVE"
        ]
        print(f"Discovered {len(active)} active sensors: "
              f"{[s['type'] for s in active]}")
        return active
    except requests.exceptions.RequestException as e:
        print(f"Error fetching sensors: {e}")
        return []

def generate_value(sensor_type, hour):
    if sensor_type == "TEMPERATURE":
        base = 26 + 6 * max(0, 1 - abs(hour - 14) / 8)
        return round(base + random.uniform(-1.5, 1.5), 1)
    elif sensor_type == "OCCUPANCY":
        if 9 <= hour <= 17:
            return random.randint(20, 55)
        else:
            return random.randint(0, 5)
    elif sensor_type == "ENERGY":
        if 9 <= hour <= 17:
            return round(random.uniform(2.5, 5.0), 2)
        else:
            return round(random.uniform(0.3, 1.5), 2)
    return 0

def send_reading(sensor):
    now = datetime.now()
    value = generate_value(sensor["type"], now.hour)

    payload = {
        "value": value,
        "timestamp": now.strftime("%Y-%m-%dT%H:%M:%S"),
        "sensor": {"id": sensor["id"]}
    }

    try:
        response = requests.post(READINGS_URL, json=payload, timeout=60)
        print(f"[{now.strftime('%H:%M:%S')}] Sensor {sensor['id']} "
              f"({sensor['type']}): {value} -> {response.status_code}")
    except requests.exceptions.RequestException as e:
        print(f"Error sending reading: {e}")

if __name__ == "__main__":
    print("Starting sensor simulator... (Ctrl+C to stop)")

    SENSOR_REFRESH_INTERVAL = 10  # re-check for new/removed sensors every N cycles
    cycle_count = 0
    sensors = fetch_active_sensors()

    while True:
        if not sensors:
            print("No active sensors found. Retrying in 30s...")
            time.sleep(30)
            sensors = fetch_active_sensors()
            continue

        for sensor in sensors:
            send_reading(sensor)

        cycle_count += 1
        if cycle_count % SENSOR_REFRESH_INTERVAL == 0:
            sensors = fetch_active_sensors()  # pick up any newly added sensors

        time.sleep(30)