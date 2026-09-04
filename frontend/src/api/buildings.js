import apiClient from './client';

export const getBuildings = () => apiClient.get('/api/buildings').then(res => res.data);