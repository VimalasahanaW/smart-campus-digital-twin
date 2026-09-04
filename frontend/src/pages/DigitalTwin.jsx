import { Link } from 'react-router-dom';
import { usePolling } from '../hooks/usePolling';
import { getSensors } from '../api/sensors';
import { getSensorReadings } from '../api/readings';

const THRESHOLDS = {
  TEMPERATURE: 35,
  OCCUPANCY: 50,
  ENERGY: 4.5,
};

function getLatestValue(readings, sensorId) {
  const forSensor = readings
    .filter(r => r.sensor.id === sensorId)
    .sort((a, b) => new Date(b.timestamp) - new Date(a.timestamp));
  return forSensor.length > 0 ? forSensor[0].value : null;
}

function isCritical(sensorType, value) {
  if (value === null) return false;
  const threshold = THRESHOLDS[sensorType];
  return threshold !== undefined && value > threshold;
}

function DigitalTwin() {
  const sensorsPoll = usePolling(getSensors, 8000);
  const readingsPoll = usePolling(getSensorReadings, 8000);

  const loading = sensorsPoll.loading || readingsPoll.loading;
  const error = sensorsPoll.error || readingsPoll.error;

  if (loading) return <p style={{ padding: 20 }}>Loading digital twin...</p>;
  if (error) return <p style={{ padding: 20, color: 'red' }}>Error: {error}</p>;

  const sensors = sensorsPoll.data || [];
  const readings = readingsPoll.data || [];

  // Group sensors by location -> "rooms"
  const rooms = {};
  sensors.forEach(s => {
    if (!rooms[s.location]) rooms[s.location] = [];
    rooms[s.location].push(s);
  });

  return (
    <div style={{ padding: 20 }}>
      <Link to="/">← Back to Dashboard</Link>
      <h1>Digital Twin — Live Floor Plan <span style={{ fontSize: 12, color: '#4caf50' }}>● live</span></h1>

      <div
        style={{
          display: 'grid',
          gridTemplateColumns: 'repeat(auto-fill, minmax(220px, 1fr))',
          gap: 16,
          marginTop: 20,
        }}
      >
        {Object.entries(rooms).map(([location, roomSensors]) => {
          const sensorStates = roomSensors.map(s => {
            const value = getLatestValue(readings, s.id);
            return { ...s, value, critical: isCritical(s.sensorType, value) };
          });
          const roomIsCritical = sensorStates.some(s => s.critical);

          return (
            <div
              key={location}
              style={{
                border: `2px solid ${roomIsCritical ? '#ff4d4d' : '#4caf50'}`,
                borderRadius: 8,
                padding: 14,
                background: roomIsCritical ? 'rgba(255,77,77,0.08)' : 'rgba(76,175,80,0.06)',
                transition: 'all 0.3s ease',
              }}
            >
              <h3 style={{ margin: '0 0 10px 0' }}>
                {location} {roomIsCritical && <span style={{ color: '#ff4d4d' }}>⚠</span>}
              </h3>
              {sensorStates.map(s => (
                <div key={s.id} style={{ fontSize: 13, marginBottom: 4 }}>
                  <Link to={`/sensors/${s.id}`} style={{ color: s.critical ? '#ff4d4d' : '#ccc' }}>
                    {s.sensorType}: {s.value !== null ? s.value : '—'}
                    {s.sensorType === 'TEMPERATURE' && s.value !== null ? '°C' : ''}
                    {s.sensorType === 'ENERGY' && s.value !== null ? 'kW' : ''}
                  </Link>
                </div>
              ))}
            </div>
          );
        })}
      </div>

      {Object.keys(rooms).length === 0 && <p style={{ marginTop: 20 }}>No sensor locations found.</p>}
    </div>
  );
}

export default DigitalTwin;