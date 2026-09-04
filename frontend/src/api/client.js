import axios from 'axios';

const apiClient = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  timeout: 90000, // Render free tier cold start can take 30-90s
});

export default apiClient;