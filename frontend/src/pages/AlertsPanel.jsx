import { Link } from 'react-router-dom';
import { usePolling } from '../hooks/usePolling';
import { getAlerts } from '../api/alerts';

const severityColor = {
  HIGH: '#ff4d4d',
  MEDIUM: '#ffaa00',
  LOW: '#ffe066',
};

function AlertsPanel() {
  const { data, loading, error } = usePolling(getAlerts, 7000);

  if (loading) return <p style={{ padding: 20 }}>Loading alerts...</p>;
  if (error) return <p style={{ padding: 20, color: 'red' }}>Error: {error}</p>;

  const alerts = [...(data || [])].sort((a, b) => new Date(b.timestamp) - new Date(a.timestamp));

  return (
    <div style={{ padding: 20 }}>
      <Link to="/">← Back to Dashboard</Link>
      <h1>Alerts ({alerts.length}) <span style={{ fontSize: 12, color: '#4caf50' }}>● live</span></h1>
      {alerts.length === 0 && <p>No alerts right now — all sensors within normal thresholds.</p>}
      <ul style={{ listStyle: 'none', padding: 0 }}>
        {alerts.map((a, i) => (
          <li
            key={i}
            style={{
              borderLeft: `4px solid ${severityColor[a.severity] || '#888'}`,
              padding: '10px 14px',
              marginBottom: 8,
              background: '#1a1a1a',
            }}
          >
            <strong style={{ color: severityColor[a.severity] || '#888' }}>{a.severity}</strong>
            {' — '}
            {a.message}
            <div style={{ fontSize: 12, color: '#888', marginTop: 4 }}>
              {a.sensor?.sensorId} • {new Date(a.timestamp).toLocaleString()}
            </div>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default AlertsPanel;