// src/services/votoService.js
import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8083/votos',
  headers: {
    'Content-Type': 'application/json',
  },
});

export const emitirVoto = async (respuestaVotacion) => {
  try {
    const response = await api.post('', respuestaVotacion);
    return response.data;
  } catch (error) {
    throw error.response?.data || 'Error al emitir voto';
  }
};