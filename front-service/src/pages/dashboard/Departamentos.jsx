// src/pages/dashboard/Departamentos.jsx
import React, { useState, useEffect } from 'react';
import { FaPlus, FaEdit, FaTrash, FaEye, FaMapMarkedAlt } from 'react-icons/fa';
import { 
  listarDepartamentos, 
  crearDepartamento, 
  actualizarDepartamento, 
  eliminarDepartamento 
} from '../../services/departamentoService';
import '../../styles/crud.css';

const Departamentos = () => {
  const [departamentos, setDepartamentos] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [showViewModal, setShowViewModal] = useState(false);
  const [editMode, setEditMode] = useState(false);
  const [selectedDepartment, setSelectedDepartment] = useState(null);
  const [formData, setFormData] = useState({
    nombre: ''
  });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    cargarDepartamentos();
  }, []);

  const cargarDepartamentos = async () => {
    try {
      setLoading(true);
      const data = await listarDepartamentos();
      setDepartamentos(data);
    } catch (error) {
      setError('Error al cargar departamentos');
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  const resetForm = () => {
    setFormData({ nombre: '' });
    setEditMode(false);
    setSelectedDepartment(null);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editMode) {
        await actualizarDepartamento(selectedDepartment.id, formData);
      } else {
        await crearDepartamento(formData);
      }
      setShowModal(false);
      resetForm();
      cargarDepartamentos();
    } catch (error) {
      setError(error);
    }
  };

  const handleEdit = (departamento) => {
    setFormData({ nombre: departamento.nombre });
    setSelectedDepartment(departamento);
    setEditMode(true);
    setShowModal(true);
  };

  const handleDelete = async (id) => {
    if (window.confirm('¿Está seguro de eliminar este departamento?')) {
      try {
        await eliminarDepartamento(id);
        cargarDepartamentos();
      } catch (error) {
        setError(error);
      }
    }
  };

  const handleView = (departamento) => {
    setSelectedDepartment(departamento);
    setShowViewModal(true);
  };

  if (loading) return <div className="loading">Cargando departamentos...</div>;

  return (
    <div className="crud-container">
      <div className="content-title">
        <FaMapMarkedAlt className="title-icon" />
        <h2>Gestión de Departamentos</h2>
      </div>

      <div className="entity-content">
        <div className="crud-header">
          <button 
            className="btn-primary"
            onClick={() => setShowModal(true)}
          >
            <FaPlus /> Nuevo Departamento
          </button>
        </div>

        {error && <div className="error-message">{error}</div>}

        <div className="departamentos-list">
          {departamentos.map(departamento => (
            <div className="departamento-card" key={departamento.id}>
              <div className="departamento-info">
                <FaMapMarkedAlt className="departamento-icon" />
                <span className="departamento-nombre">{departamento.nombre}</span>
              </div>
              <div className="actions">
                <button 
                  className="btn-view"
                  onClick={() => handleView(departamento)}
                >
                  <FaEye />
                </button>
                <button 
                  className="btn-edit"
                  onClick={() => handleEdit(departamento)}
                >
                  <FaEdit />
                </button>
                <button 
                  className="btn-delete"
                  onClick={() => handleDelete(departamento.id)}
                >
                  <FaTrash />
                </button>
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* Modal Crear/Editar */}
      {showModal && (
        <div className="modal-overlay">
          <div className="modal">
            <div className="modal-header">
              <h3>{editMode ? 'Editar Departamento' : 'Nuevo Departamento'}</h3>
              <button 
                className="modal-close"
                onClick={() => {
                  setShowModal(false);
                  resetForm();
                }}
              >
                ×
              </button>
            </div>
            <form onSubmit={handleSubmit} className="modal-form">
              <div className="form-group">
                <label>Nombre del Departamento:</label>
                <input
                  type="text"
                  value={formData.nombre}
                  onChange={(e) => setFormData({...formData, nombre: e.target.value})}
                  required
                  placeholder="Ingrese el nombre del departamento"
                />
              </div>

              <div className="modal-actions">
                <button type="submit" className="btn-primary">
                  {editMode ? 'Actualizar' : 'Crear'}
                </button>
                <button 
                  type="button" 
                  className="btn-secondary"
                  onClick={() => {
                    setShowModal(false);
                    resetForm();
                  }}
                >
                  Cancelar
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* Modal Ver Detalles */}
      {showViewModal && selectedDepartment && (
        <div className="modal-overlay">
          <div className="modal">
            <div className="modal-header">
              <h3>Detalles del Departamento</h3>
              <button 
                className="modal-close"
                onClick={() => setShowViewModal(false)}
              >
                ×
              </button>
            </div>
            <div className="modal-content">
              <div className="detail-item">
                <strong>ID:</strong> {selectedDepartment.id}
              </div>
              <div className="detail-item">
                <strong>Nombre:</strong> {selectedDepartment.nombre}
              </div>
            </div>
            <div className="modal-actions">
              <button 
                className="btn-secondary"
                onClick={() => setShowViewModal(false)}
              >
                Cerrar
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default Departamentos;