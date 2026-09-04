import apiClient from './client';

export const getSensorReadings = () => apiClient.get('/api/sensor-readings').then(res => res.data);