import { Link } from 'react-router-dom';
import { usePolling } from '../hooks/usePolling';
import { getBuildings } from '../api/buildings';

function Dashboard() {
  const { data, loading, error } = usePolling(getBuildings, 8000);

  if (loading) return <p style={{ padding: 20 }}>Loading buildings... (backend may take up to 90s to wake up)</p>;
  if (error) return <p style={{ padding: 20, color: 'red' }}>Error: {error}</p>;

  const buildings = data || [];

  return (
    <div style={{ padding: 20 }}>
     <nav style={{ marginBottom: 20 }}>
  <Link to="/alerts">🔔 View Alerts</Link>
  {' | '}
  <Link to="/predictions">📈 View Predictions</Link>
  {' | '}
  <Link to="/twin">🏢 Digital Twin</Link>
</nav>
      <h1>Campus Buildings <span style={{ fontSize: 12, color: '#4caf50' }}>● live</span></h1>
      <ul>
        {buildings.map(b => (
          <li key={b.id}>
            <Link to={`/buildings/${b.id}`}>{b.name}</Link> — {b.location} ({b.buildingType})
          </li>
        ))}
      </ul>
    </div>
  );
}

export default Dashboard;