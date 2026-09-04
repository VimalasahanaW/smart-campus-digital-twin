import apiClient from './client';

export const getSensors = () => apiClient.get('/api/sensors').then(res => res.data);