import { Routes, Route } from 'react-router-dom';
import Dashboard from './pages/Dashboard';
import BuildingDetail from './pages/BuildingDetail';
import SensorDetail from './pages/SensorDetail';
import AlertsPanel from './pages/AlertsPanel';
import PredictionsPanel from './pages/PredictionsPanel';
import DigitalTwin from './pages/DigitalTwin';

function App() {
  return (
    <Routes>
      <Route path="/" element={<Dashboard />} />
      <Route path="/buildings/:id" element={<BuildingDetail />} />
      <Route path="/sensors/:id" element={<SensorDetail />} />
      <Route path="/alerts" element={<AlertsPanel />} />
      <Route path="/predictions" element={<PredictionsPanel />} />
      <Route path="/twin" element={<DigitalTwin />} />
    </Routes>
  );
}

export default App;