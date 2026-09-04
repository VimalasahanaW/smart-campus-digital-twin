import { useParams, Link } from 'react-router-dom';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';
import { usePolling } from '../hooks/usePolling';
import { getSensorReadings } from '../api/readings';

function SensorDetail() {
  const { id } = useParams();
  const { data, loading, error } = usePolling(getSensorReadings, 8000, [id]);

  if (loading) return <p style={{ padding: 20 }}>Loading readings...</p>;
  if (error) return <p style={{ padding: 20, color: 'red' }}>Error: {error}</p>;

  const readings = (data || [])
    .filter(r => String(r.sensor.id) === id)
    .sort((a, b) => new Date(a.timestamp) - new Date(b.timestamp));

  if (readings.length === 0) return <p style={{ padding: 20 }}>No readings yet for this sensor.</p>;

  const latest = readings[readings.length - 1];
  const chartData = readings.map(r => ({
    time: new Date(r.timestamp).toLocaleTimeString(),
    value: r.value,
  }));

  return (
    <div style={{ padding: 20 }}>
      <Link to={`/buildings/${latest.sensor.building.id}`}>← Back to Building</Link>
      <h1>{latest.sensor.sensorId} — {latest.sensor.sensorType} <span style={{ fontSize: 12, color: '#4caf50' }}>● live</span></h1>
      <p>Location: {latest.sensor.location}</p>
      <h2>Current: {latest.value} {latest.sensor.sensorType === 'TEMPERATURE' ? '°C' : latest.sensor.sensorType === 'ENERGY' ? 'kW' : ''}</h2>
      <p style={{ color: '#888' }}>Last updated: {new Date(latest.timestamp).toLocaleString()}</p>

      <div style={{ width: '100%', height: 300, marginTop: 20 }}>
        <ResponsiveContainer>
          <LineChart data={chartData}>
            <CartesianGrid strokeDasharray="3 3" />
            <XAxis dataKey="time" />
            <YAxis />
            <Tooltip />
            <Line type="monotone" dataKey="value" stroke="#8884d8" dot={false} />
          </LineChart>
        </ResponsiveContainer>
      </div>
    </div>
  );
}

export default SensorDetail;