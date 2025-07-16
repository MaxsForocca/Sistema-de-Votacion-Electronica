// src/services/categoriaService.js
import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8083/categorias',
  headers: {
    'Content-Type': 'application/json',
  },
});

export const listarCategorias = async () => {
  try {
    const response = await api.get('');
    return response.data;
  } catch (error) {
    throw error.response?.data || 'Error al listar categorías';
  }
};

export const crearCategoria = async (categoria) => {
  try {
    const response = await api.post('', categoria);
    return response.data;
  } catch (error) {
    throw error.response?.data || 'Error al crear categoría';
  }
};

export const actualizarCategoria = async (id, categoria) => {
  try {
    const response = await api.put(`/${id}`, categoria);
    return response.data;
  } catch (error) {
    throw error.response?.data || 'Error al actualizar categoría';
  }
};

export const eliminarCategoria = async (id) => {
  try {
    const response = await api.delete(`/${id}`);
    return response.data;
  } catch (error) {
    throw error.response?.data || 'Error al eliminar categoría';
  }
};