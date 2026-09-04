import apiClient from './client';

export const getAlerts = () => apiClient.get('/api/alerts').then(res => res.data);