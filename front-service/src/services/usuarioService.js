// src/api/usuarioService.js
import axios from 'axios';

// Configuración de Axios
const api = axios.create({
  baseURL: 'http://localhost:8082/usuario',
  headers: {
    'Content-Type': 'application/json',
  },
});

// Función: Login de usuario
export const loginUsuario = async (username, password) => {
  try {
    const response = await api.post('/login', { username, password });
    return response.data;
  } catch (error) {
    throw error.response?.data || 'Error en login';
  }
};

// Función: Obtener usuario por ID para votación
export const obtenerUsuarioPorIdVoting = async (id) => {
  try {
    const response = await api.get(`/${id}`);
    return response.data;
  } catch (error) {
    throw error.response?.data || 'Error al obtener usuario';
  }
};

// Función: Listar todos los usuarios (ADMIN)
export const listarUsuarios = async () => {
  try {
    const response = await api.get(''); // ✅ sin barra
    return response.data;
  } catch (error) {
    throw error.response?.data || 'Error al listar usuarios';
  }
};

// Función: Obtener usuario por ID específico (ADMIN)
export const obtenerUsuarioPorId = async (id) => {
  try {
    const response = await api.get(`/specific/${id}`);
    return response.data;
  } catch (error) {
    throw error.response?.data || 'Error al obtener usuario';
  }
};

// Función: Crear usuario
export const crearUsuario = async (usuarioDTO) => {
  try {
    const response = await api.post('', usuarioDTO); // ✅ sin barra
    return response.data;
  } catch (error) {
    throw error.response?.data || 'Error al crear usuario';
  }
};

// Función: Actualizar usuario
export const actualizarUsuario = async (id, usuarioDTO) => {
  try {
    const response = await api.put(`/${id}`, usuarioDTO);
    return response.data;
  } catch (error) {
    throw error.response?.data || 'Error al actualizar usuario';
  }
};

// Función: Eliminar usuario
export const eliminarUsuario = async (id) => {
  try {
    const response = await api.delete(`/${id}`);
    return response.data;
  } catch (error) {
    throw error.response?.data || 'Error al eliminar usuario';
  }
};
