import { Link } from 'react-router-dom';
import { usePolling } from '../hooks/usePolling';
import { getPredictions } from '../api/predictions';

function PredictionsPanel() {
  const { data, loading, error } = usePolling(getPredictions, 8000);

  if (loading) return <p style={{ padding: 20 }}>Loading predictions...</p>;
  if (error) return <p style={{ padding: 20, color: 'red' }}>Error: {error}</p>;

  const predictions = data || [];

  return (
    <div style={{ padding: 20 }}>
      <Link to="/">← Back to Dashboard</Link>
      <h1>Predictions ({predictions.length}) <span style={{ fontSize: 12, color: '#4caf50' }}>● live</span></h1>
      {predictions.length === 0 && (
        <p style={{ color: '#888' }}>
          No predictions yet — ML model training hasn't started. This panel will populate
          automatically once it does.
        </p>
      )}
      <ul style={{ listStyle: 'none', padding: 0 }}>
        {predictions.map((p, i) => (
          <li
            key={i}
            style={{
              padding: '10px 14px',
              marginBottom: 8,
              background: '#1a1a1a',
              borderLeft: '4px solid #6c8cff',
            }}
          >
            <strong>{p.predictionType}</strong>
            {' — Predicted: '}
            {p.predictedValue}
            <div style={{ fontSize: 12, color: '#888', marginTop: 4 }}>
              {p.sensor?.sensorId} • For: {new Date(p.forDate).toLocaleString()}
            </div>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default PredictionsPanel;