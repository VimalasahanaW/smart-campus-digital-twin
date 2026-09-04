import apiClient from './client';

export const getPredictions = () => apiClient.get('/api/predictions').then(res => res.data);