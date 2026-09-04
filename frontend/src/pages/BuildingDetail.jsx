import { useParams, Link } from 'react-router-dom';
import { usePolling } from '../hooks/usePolling';
import { getSensors } from '../api/sensors';

function BuildingDetail() {
  const { id } = useParams();
  const { data, loading, error } = usePolling(getSensors, 8000, [id]);

  if (loading) return <p style={{ padding: 20 }}>Loading sensors...</p>;
  if (error) return <p style={{ padding: 20, color: 'red' }}>Error: {error}</p>;

  const sensors = (data || []).filter(s => String(s.building.id) === id);

  return (
    <div style={{ padding: 20 }}>
      <Link to="/">← Back to Buildings</Link>
      <h1>Sensors ({sensors.length}) <span style={{ fontSize: 12, color: '#4caf50' }}>● live</span></h1>
      {sensors.length === 0 && <p>No sensors found for this building.</p>}
      <ul>
        {sensors.map(s => (
          <li key={s.id}>
            <Link to={`/sensors/${s.id}`}>
              <strong>{s.sensorId}</strong> — {s.sensorType} — {s.location}
            </Link>
            {' — '}
            <span style={{ color: s.status === 'ACTIVE' ? 'lightgreen' : 'gray' }}>
              {s.status}
            </span>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default BuildingDetail;