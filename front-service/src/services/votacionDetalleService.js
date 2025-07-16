// src/services/votacionDetalleService.js
import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8083/votaciones',
  headers: {
    'Content-Type': 'application/json',
  },
});

export const obtenerVotacionDetalle = async (id) => {
  try {
    const response = await api.get(`/${id}`);
    return response.data;
  } catch (error) {
    throw error.response?.data || 'Error al obtener detalles de votación';
  }
};