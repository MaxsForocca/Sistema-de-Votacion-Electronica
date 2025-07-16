// src/services/documentoService.js
import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8084/documentos',
  headers: {
    'Content-Type': 'application/json',
  },
});

export const listarDocumentos = async () => {
  try {
    const response = await api.get('');
    return response.data;
  } catch (error) {
    throw error.response?.data || 'Error al listar documentos';
  }
};

export const subirDocumento = async (formData) => {
  try {
    const response = await axios.post('http://localhost:8084/documentos/subir', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });
    return response.data;
  } catch (error) {
    throw error.response?.data || 'Error al subir documento';
  }
};

export const descargarDocumento = async (filename) => {
  try {
    const response = await axios.get(`http://localhost:8084/documentos/${filename}`, {
      responseType: 'blob',
    });
    return response.data;
  } catch (error) {
    throw error.response?.data || 'Error al descargar documento';
  }
};