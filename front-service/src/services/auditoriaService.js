// src/services/auditoriaService.js
import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8083/auditorias',
  headers: {
    'Content-Type': 'application/json',
  },
});

export const listarAuditorias = async () => {
  try {
    const response = await api.get('/');
    return response.data;
  } catch (error) {
    throw error.response?.data || 'Error al listar auditorías';
  }
};