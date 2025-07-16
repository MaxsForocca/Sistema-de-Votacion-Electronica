// src/services/departamentoService.js
import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8082/departamentos',
  headers: {
    'Content-Type': 'application/json',
  },
});

export const listarDepartamentos = async () => {
  try {
    const response = await api.get(''); // ✅ sin barra
    return response.data;
  } catch (error) {
    throw error.response?.data || 'Error al listar departamentos';
  }
};

export const crearDepartamento = async (departamento) => {
  try {
    const response = await api.post('', departamento); // ✅ sin barra
    return response.data;
  } catch (error) {
    throw error.response?.data || 'Error al crear departamento';
  }
};

export const actualizarDepartamento = async (id, departamento) => {
  try {
    const response = await api.put(`/${id}`, departamento);
    return response.data;
  } catch (error) {
    throw error.response?.data || 'Error al actualizar departamento';
  }
};

export const eliminarDepartamento = async (id) => {
  try {
    const response = await api.delete(`/${id}`);
    return response.data;
  } catch (error) {
    throw error.response?.data || 'Error al eliminar departamento';
  }
};
