// src/pages/dashboard/Votaciones.jsx
import React, { useState, useEffect } from 'react';
import { FaPlus, FaEdit, FaTrash, FaEye, FaVoteYea, FaQuestion, FaTimes } from 'react-icons/fa';
import { 
  listarVotaciones, 
  crearVotacion, 
  actualizarVotacion, 
  eliminarVotacion 
} from '../../services/votacionService';
import { listarCategorias } from '../../services/categoriaService';
import '../../styles/crud.css';

const Votaciones = () => {
  const [votaciones, setVotaciones] = useState([]);
  const [categorias, setCategorias] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [showViewModal, setShowViewModal] = useState(false);
  const [editMode, setEditMode] = useState(false);
  const [selectedVotacion, setSelectedVotacion] = useState(null);
  const [formData, setFormData] = useState({
    titulo: '',
    descripcion: '',
    activa: true,
    categoriaId: '',
    preguntas: []
  });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    cargarDatos();
  }, []);

  const cargarDatos = async () => {
    try {
      setLoading(true);
      const [votacionesData, categoriasData] = await Promise.all([
        listarVotaciones(),
        listarCategorias()
      ]);
      setVotaciones(votacionesData);
      setCategorias(categoriasData);
    } catch (error) {
      setError('Error al cargar los datos');
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  const resetForm = () => {
    setFormData({
      titulo: '',
      descripcion: '',
      activa: true,
      categoriaId: '',
      preguntas: []
    });
    setEditMode(false);
    setSelectedVotacion(null);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editMode) {
        await actualizarVotacion(selectedVotacion.id, formData);
      } else {
        await crearVotacion(formData);
      }
      setShowModal(false);
      resetForm();
      cargarDatos();
    } catch (error) {
      setError(error);
    }
  };

  const handleEdit = (votacion) => {
    setFormData({
      titulo: votacion.titulo,
      descripcion: votacion.descripcion,
      activa: votacion.activa,
      categoriaId: votacion.categoriaId,
      preguntas: votacion.preguntas?.map(p => ({
        texto: p.texto,
        opciones: p.opciones?.map(o => o.texto) || []
      })) || []
    });
    setSelectedVotacion(votacion);
    setEditMode(true);
    setShowModal(true);
  };

  const handleDelete = async (id) => {
    if (window.confirm('¿Está seguro de eliminar esta votación?')) {
      try {
        await eliminarVotacion(id);
        cargarDatos();
      } catch (error) {
        setError(error);
      }
    }
  };

  const handleView = (votacion) => {
    setSelectedVotacion(votacion);
    setShowViewModal(true);
  };

  const getCategoryName = (categoriaId) => {
    const categoria = categorias.find(c => c.id === categoriaId);
    return categoria ? categoria.nombre : 'N/A';
  };

  // Funciones para manejar preguntas
  const addPregunta = () => {
    setFormData({
      ...formData,
      preguntas: [...formData.preguntas, { texto: '', opciones: ['', ''] }]
    });
  };

  const removePregunta = (index) => {
    const newPreguntas = formData.preguntas.filter((_, i) => i !== index);
    setFormData({ ...formData, preguntas: newPreguntas });
  };

  const updatePregunta = (index, texto) => {
    const newPreguntas = [...formData.preguntas];
    newPreguntas[index].texto = texto;
    setFormData({ ...formData, preguntas: newPreguntas });
  };

  const addOpcion = (preguntaIndex) => {
    const newPreguntas = [...formData.preguntas];
    newPreguntas[preguntaIndex].opciones.push('');
    setFormData({ ...formData, preguntas: newPreguntas });
  };

  const removeOpcion = (preguntaIndex, opcionIndex) => {
    const newPreguntas = [...formData.preguntas];
    newPreguntas[preguntaIndex].opciones = newPreguntas[preguntaIndex].opciones.filter((_, i) => i !== opcionIndex);
    setFormData({ ...formData, preguntas: newPreguntas });
  };

  const updateOpcion = (preguntaIndex, opcionIndex, texto) => {
    const newPreguntas = [...formData.preguntas];
    newPreguntas[preguntaIndex].opciones[opcionIndex] = texto;
    setFormData({ ...formData, preguntas: newPreguntas });
  };

  if (loading) return <div className="loading">Cargando votaciones...</div>;

  return (
    <div className="crud-container">
      <div className="content-title">
        <FaVoteYea className="title-icon" />
        <h2>Gestión de Votaciones</h2>
      </div>

      <div className="entity-content">
        <div className="crud-header">
          <button 
            className="btn-primary"
            onClick={() => setShowModal(true)}
          >
            <FaPlus /> Nueva Votación
          </button>
        </div>

        {error && <div className="error-message">{error}</div>}

        <div className="card-grid">
          {votaciones.map(votacion => (
            <div className="card-item" key={votacion.id}>
              <div className="card-header">
                <div className="card-icon"><FaVoteYea className="icon-purple" /></div>
                <span className="card-title">{votacion.titulo}</span>
                <span className={`status ${votacion.activa ? 'active' : 'inactive'}`}>
                  {votacion.activa ? 'Activa' : 'Inactiva'}
                </span>
              </div>

              <div className="card-body no-margin">
                <p><strong>Categoría:</strong> {getCategoryName(votacion.categoriaId)}</p>
                <p><strong>Descripción:</strong> {votacion.descripcion}</p>

                {votacion.preguntas && votacion.preguntas.length > 0 && (
                  <div className="preguntas-preview-scroll">
                    {votacion.preguntas.map((pregunta, pIndex) => (
                      <div key={pIndex} className="pregunta-card black">
                        <p className="pregunta-text">
                          <FaQuestion className="icon-purple" /> {pregunta.texto}
                        </p>
                        <div className="opciones-botones">
                          {pregunta.opciones?.map((opcion, oIndex) => (
                            <div key={oIndex} className="opcion-btn">
                              {opcion.texto}
                            </div>
                          ))}
                        </div>
                      </div>
                    ))}
                  </div>
                )}
              </div>

              <div className="card-actions">
                <button className="btn-view" onClick={() => handleView(votacion)}><FaEye /></button>
                <button className="btn-edit" onClick={() => handleEdit(votacion)}><FaEdit /></button>
                <button className="btn-delete" onClick={() => handleDelete(votacion.id)}><FaTrash /></button>
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* Modal Crear/Editar */}
      {showModal && (
        <div className="modal-overlay">
          <div className="modal modal-large">
            <div className="modal-header">
              <h3>{editMode ? 'Editar Votación' : 'Nueva Votación'}</h3>
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
              <div className="form-row">
                <div className="form-group">
                  <label>Título:</label>
                  <input
                    type="text"
                    value={formData.titulo}
                    onChange={(e) => setFormData({...formData, titulo: e.target.value})}
                    required
                  />
                </div>
                
                <div className="form-group">
                  <label>Categoría:</label>
                  <select
                    value={formData.categoriaId}
                    onChange={(e) => setFormData({...formData, categoriaId: parseInt(e.target.value)})}
                    required
                  >
                    <option value="">Seleccionar categoría</option>
                    {categorias.map(categoria => (
                      <option key={categoria.id} value={categoria.id}>{categoria.nombre}</option>
                    ))}
                  </select>
                </div>
              </div>

              <div className="form-group">
                <label>Descripción:</label>
                <textarea
                  value={formData.descripcion}
                  onChange={(e) => setFormData({...formData, descripcion: e.target.value})}
                  rows="3"
                />
              </div>

              <div className="form-group">
                <label className="checkbox-label">
                  <input
                    type="checkbox"
                    checked={formData.activa}
                    onChange={(e) => setFormData({...formData, activa: e.target.checked})}
                  />
                  Votación activa
                </label>
              </div>

              {/* Sección de Preguntas */}
              <div className="preguntas-section">
                <div className="section-header">
                  <h4><FaQuestion /> Preguntas</h4>
                  <button type="button" className="btn-secondary" onClick={addPregunta}>
                    <FaPlus /> Agregar Pregunta
                  </button>
                </div>

                {formData.preguntas.map((pregunta, preguntaIndex) => (
                  <div key={preguntaIndex} className="pregunta-card">
                    <div className="pregunta-header">
                      <h5>Pregunta {preguntaIndex + 1}</h5>
                      <button 
                        type="button" 
                        className="btn-delete-small"
                        onClick={() => removePregunta(preguntaIndex)}
                      >
                        <FaTimes />
                      </button>
                    </div>
                    
                    <div className="form-group">
                      <input
                        type="text"
                        placeholder="Texto de la pregunta"
                        value={pregunta.texto}
                        onChange={(e) => updatePregunta(preguntaIndex, e.target.value)}
                        required
                      />
                    </div>

                    <div className="opciones-section">
                      <label>Opciones:</label>
                      {pregunta.opciones.map((opcion, opcionIndex) => (
                        <div key={opcionIndex} className="opcion-input">
                          <input
                            type="text"
                            placeholder={`Opción ${opcionIndex + 1}`}
                            value={opcion}
                            onChange={(e) => updateOpcion(preguntaIndex, opcionIndex, e.target.value)}
                            required
                          />
                          {pregunta.opciones.length > 2 && (
                            <button 
                              type="button" 
                              className="btn-delete-small"
                              onClick={() => removeOpcion(preguntaIndex, opcionIndex)}
                            >
                              <FaTimes />
                            </button>
                          )}
                        </div>
                      ))}
                      <button 
                        type="button" 
                        className="btn-secondary btn-small"
                        onClick={() => addOpcion(preguntaIndex)}
                      >
                        <FaPlus /> Agregar Opción
                      </button>
                    </div>
                  </div>
                ))}
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
      {showViewModal && selectedVotacion && (
        <div className="modal-overlay">
          <div className="modal modal-large">
            <div className="modal-header">
              <h3>Detalles de la Votación</h3>
              <button 
                className="modal-close"
                onClick={() => setShowViewModal(false)}
              >
                ×
              </button>
            </div>
            <div className="modal-content">
              <div className="detail-item">
                <strong>ID:</strong> {selectedVotacion.id}
              </div>
              <div className="detail-item">
                <strong>Título:</strong> {selectedVotacion.titulo}
              </div>
              <div className="detail-item">
                <strong>Descripción:</strong> {selectedVotacion.descripcion}
              </div>
              <div className="detail-item">
                <strong>Categoría:</strong> {getCategoryName(selectedVotacion.categoriaId)}
              </div>
              <div className="detail-item">
                <strong>Estado:</strong> 
                <span className={`status ${selectedVotacion.activa ? 'active' : 'inactive'}`}>
                  {selectedVotacion.activa ? 'Activa' : 'Inactiva'}
                </span>
              </div>
              
              {selectedVotacion.preguntas && selectedVotacion.preguntas.length > 0 && (
                <div className="detail-item">
                  <strong>Preguntas:</strong>
                  <div className="preguntas-list">
                    {selectedVotacion.preguntas.map((pregunta, index) => (
                      <div key={index} className="pregunta-detail">
                        <h5>Pregunta {index + 1}: {pregunta.texto}</h5>
                        <div className="opciones-list">
                          {pregunta.opciones?.map((opcion, opIndex) => (
                            <div key={opIndex} className="opcion-tag">
                              {opcion.texto}
                            </div>
                          ))}
                        </div>
                      </div>
                    ))}
                  </div>
                </div>
              )}

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

export default Votaciones;