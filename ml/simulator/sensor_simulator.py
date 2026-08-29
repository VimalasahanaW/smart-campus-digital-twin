import requests
import random
import time
from datetime import datetime

# Live backend URL (Render)
API_URL = "https://smart-campus-backend-yzt1.onrender.com/api/sensor-readings"

# Existing sensors in your database (id: 2 = TEMPERATURE sensor)
SENSORS = [
    {"id": 1, "type": "TEMPERATURE"},
]

def generate_value(sensor_type, hour):
    """Generate realistic values based on sensor type and time of day."""
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
        response = requests.post(API_URL, json=payload, timeout=60)
        print(f"[{now.strftime('%H:%M:%S')}] Sensor {sensor['id']} ({sensor['type']}): {value} -> {response.status_code}")
    except requests.exceptions.RequestException as e:
        print(f"Error sending reading: {e}")

if __name__ == "__main__":
    print("Starting sensor simulator... (Ctrl+C to stop)")
    while True:
        for sensor in SENSORS:
            send_reading(sensor)
        time.sleep(30)