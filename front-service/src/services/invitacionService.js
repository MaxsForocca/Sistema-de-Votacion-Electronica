// src/services/invitacionService.js
import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8083/invitaciones',
  headers: {
    'Content-Type': 'application/json',
  },
});

export const crearInvitacion = async (invitacion) => {
  try {
    const response = await api.post('', invitacion);
    return response.data;
  } catch (error) {
    throw error.response?.data || 'Error al crear invitación';
  }
};

export const listarVotacionesPorUsuario = async (usuarioId) => {
  try {
    const response = await api.get(`/usuario/${usuarioId}`);
    return response.data;
  } catch (error) {
    if (error.response?.status === 204) {
      return []; // No hay votaciones
    }
    throw error.response?.data || 'Error al listar votaciones del usuario';
  }
};

export const eliminarInvitacion = async (id) => {
  try {
    const response = await api.delete(`/${id}`);
    return response.data;
  } catch (error) {
    throw error.response?.data || 'Error al eliminar invitación';
  }
};