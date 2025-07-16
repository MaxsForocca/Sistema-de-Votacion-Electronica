import React, { useState, useEffect } from 'react';
import { FaPlus, FaEdit, FaTrash, FaEye, FaTags } from 'react-icons/fa';
import {
  listarCategorias,
  crearCategoria,
  actualizarCategoria,
  eliminarCategoria,
} from '../../services/categoriaService';
import '../../styles/crud.css';

const Categoria = () => {
  const [categorias, setCategorias] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [showViewModal, setShowViewModal] = useState(false);
  const [editMode, setEditMode] = useState(false);
  const [selectedCategory, setSelectedCategory] = useState(null);
  const [formData, setFormData] = useState({ nombre: '' });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    cargarCategorias();
  }, []);

  const cargarCategorias = async () => {
    try {
      setLoading(true);
      const data = await listarCategorias();
      setCategorias(data);
    } catch (error) {
      setError('Error al cargar categorías');
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  const resetForm = () => {
    setFormData({ nombre: '' });
    setEditMode(false);
    setSelectedCategory(null);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editMode) {
        await actualizarCategoria(selectedCategory.id, formData);
      } else {
        await crearCategoria(formData);
      }
      setShowModal(false);
      resetForm();
      cargarCategorias();
    } catch (error) {
      setError(error);
    }
  };

  const handleEdit = (categoria) => {
    setFormData({ nombre: categoria.nombre });
    setSelectedCategory(categoria);
    setEditMode(true);
    setShowModal(true);
  };

  const handleDelete = async (id) => {
    if (window.confirm('¿Está seguro de eliminar esta categoría?')) {
      try {
        await eliminarCategoria(id);
        cargarCategorias();
      } catch (error) {
        setError(error);
      }
    }
  };

  const handleView = (categoria) => {
    setSelectedCategory(categoria);
    setShowViewModal(true);
  };

  if (loading) return <div className="loading">Cargando categorías...</div>;

  return (
    <div className="crud-container">
      <div className="content-title">
        <FaTags className="title-icon" />
        <h2>Gestión de Categorías</h2>
      </div>

      <div className="entity-content">
        <div className="crud-header">
          <button className="btn-primary" onClick={() => setShowModal(true)}>
            <FaPlus /> Nueva Categoría
          </button>
        </div>

        {error && <div className="error-message">{error}</div>}

        <div className="departamentos-list">
          {categorias.map((categoria) => (
            <div className="departamento-card" key={categoria.id}>
              <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                <FaTags className="departamento-icon" />
                <span className="departamento-nombre">{categoria.nombre}</span>
              </div>
              <div className="actions">
                <button className="btn-view" onClick={() => handleView(categoria)}>
                  <FaEye />
                </button>
                <button className="btn-edit" onClick={() => handleEdit(categoria)}>
                  <FaEdit />
                </button>
                <button className="btn-delete" onClick={() => handleDelete(categoria.id)}>
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
              <h3>{editMode ? 'Editar Categoría' : 'Nueva Categoría'}</h3>
              <button className="modal-close" onClick={() => { setShowModal(false); resetForm(); }}>
                ×
              </button>
            </div>
            <form onSubmit={handleSubmit} className="modal-form">
              <div className="form-group">
                <label>Nombre de la Categoría:</label>
                <input
                  type="text"
                  value={formData.nombre}
                  onChange={(e) => setFormData({ ...formData, nombre: e.target.value })}
                  required
                  placeholder="Ingrese el nombre de la categoría"
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
      {showViewModal && selectedCategory && (
        <div className="modal-overlay">
          <div className="modal">
            <div className="modal-header">
              <h3>Detalles de la Categoría</h3>
              <button className="modal-close" onClick={() => setShowViewModal(false)}>
                ×
              </button>
            </div>
            <div className="modal-content">
              <div className="detail-item">
                <strong>ID:</strong> {selectedCategory.id}
              </div>
              <div className="detail-item">
                <strong>Nombre:</strong> {selectedCategory.nombre}
              </div>
            </div>
            <div className="modal-actions">
              <button className="btn-secondary" onClick={() => setShowViewModal(false)}>
                Cerrar
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default Categoria;
