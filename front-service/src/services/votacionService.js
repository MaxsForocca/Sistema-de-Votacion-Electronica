// src/services/votacionService.js
import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8083/votaciones',
  headers: {
    'Content-Type': 'application/json',
  },
});

export const listarVotaciones = async () => {
  try {
    const response = await api.get('/completas');
    return response.data;
  } catch (error) {
    throw error.response?.data || 'Error al listar votaciones';
  }
};

export const crearVotacion = async (votacion) => {
  try {
    const response = await api.post('/completa', votacion);
    return response.data;
  } catch (error) {
    throw error.response?.data || 'Error al crear votación';
  }
};

export const actualizarVotacion = async (id, votacion) => {
  try {
    const response = await api.put(`/${id}`, votacion);
    return response.data;
  } catch (error) {
    throw error.response?.data || 'Error al actualizar votación';
  }
};

export const eliminarVotacion = async (id) => {
  try {
    const response = await api.delete(`/${id}`);
    return response.data;
  } catch (error) {
    throw error.response?.data || 'Error al eliminar votación';
  }
};