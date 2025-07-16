// src/pages/dashboard/Invitaciones.jsx
import React, { useState, useEffect } from 'react';
import { FaUserPlus, FaEnvelope, FaEye, FaTrash, FaUsers, FaVoteYea } from 'react-icons/fa';
import { listarUsuarios } from '../../services/usuarioService';
import { listarVotaciones } from '../../services/votacionService';
import { crearInvitacion, eliminarInvitacion } from '../../services/invitacionService';
import '../../styles/crud.css';

const Invitaciones = () => {
  const [usuarios, setUsuarios] = useState([]);
  const [votaciones, setVotaciones] = useState([]);
  const [showInviteModal, setShowInviteModal] = useState(false);
  const [showDetailsModal, setShowDetailsModal] = useState(false);
  const [selectedVotacion, setSelectedVotacion] = useState(null);
  const [invitacionForm, setInvitacionForm] = useState({
    usuarioId: '',
    votacionId: ''
  });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    cargarDatos();
  }, []);

  const cargarDatos = async () => {
    try {
      setLoading(true);
      const [usuariosData, votacionesData] = await Promise.all([
        listarUsuarios(),
        listarVotaciones()
      ]);
      setUsuarios(usuariosData);
      setVotaciones(votacionesData.filter(v => v.activa)); // Solo votaciones activas
    } catch (error) {
      setError('Error al cargar los datos');
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  const handleInvitar = async (e) => {
    e.preventDefault();
    try {
      await crearInvitacion({
        usuarioId: parseInt(invitacionForm.usuarioId),
        votacionId: parseInt(invitacionForm.votacionId)
      });
      setShowInviteModal(false);
      setInvitacionForm({ usuarioId: '', votacionId: '' });
      alert('Usuario invitado exitosamente');
    } catch (error) {
      setError(error);
    }
  };

  const handleVerDetalles = (votacion) => {
    setSelectedVotacion(votacion);
    setShowDetailsModal(true);
  };

  const getUsuarioNombre = (usuarioId) => {
    const usuario = usuarios.find(u => u.id === usuarioId);
    return usuario ? usuario.username : 'N/A';
  };

  const getUsersByRole = (role) => {
    return usuarios.filter(u => {
      const roleId = role === 'VOTANTE' ? 2 : 1;
      return u.idRol === roleId;
    });
  };

  if (loading) return <div className="loading">Cargando datos...</div>;

  return (
    <div className="crud-container">
      <div className="content-title">
        <FaEnvelope className="title-icon" />
        <h2>Gestión de Invitaciones</h2>
      </div>

      <div className="entity-content">
        <div className="crud-header">
          <button 
            className="btn-primary"
            onClick={() => setShowInviteModal(true)}
          >
            <FaUserPlus /> Invitar Usuario
          </button>
        </div>

        {error && <div className="error-message">{error}</div>}

        {/* Estadísticas */}
        <div className="stats-section">
          <div className="stat-card">
            <h4>Votaciones Activas</h4>
            <span>{votaciones.length}</span>
          </div>
          <div className="stat-card">
            <h4>Total Usuarios</h4>
            <span>{usuarios.length}</span>
          </div>
          <div className="stat-card">
            <h4>Votantes</h4>
            <span>{getUsersByRole('VOTANTE').length}</span>
          </div>
        </div>

        {/* Lista de Votaciones */}
        <div className="votaciones-section">
          <h3><FaVoteYea /> Votaciones Disponibles para Invitar</h3>
          <div className="votaciones-grid">
            {votaciones.map(votacion => (
              <div key={votacion.id} className="votacion-card">
                <div className="votacion-header">
                  <h4>{votacion.titulo}</h4>
                  <span className="status active">Activa</span>
                </div>
                <div className="votacion-body">
                  <p>{votacion.descripcion}</p>
                  <div className="votacion-stats">
                    <span>Preguntas: {votacion.preguntas?.length || 0}</span>
                  </div>
                </div>
                <div className="votacion-actions">
                  <button 
                    className="btn-view"
                    onClick={() => handleVerDetalles(votacion)}
                  >
                    <FaEye /> Ver Detalles
                  </button>
                  <button 
                    className="btn-primary btn-small"
                    onClick={() => {
                      setInvitacionForm({ ...invitacionForm, votacionId: votacion.id });
                      setShowInviteModal(true);
                    }}
                  >
                    <FaUserPlus /> Invitar
                  </button>
                </div>
              </div>
            ))}
          </div>
        </div>

        {/* Sección de Usuarios Votantes */}
        <div className="usuarios-section">
          <h3><FaUsers /> Usuarios Disponibles para Invitar</h3>
          <div className="table-container">
            <table className="crud-table">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Username</th>
                  <th>Rol</th>
                  <th>Acciones</th>
                </tr>
              </thead>
              <tbody>
                {getUsersByRole('VOTANTE').map(usuario => (
                  <tr key={usuario.id}>
                    <td>{usuario.id}</td>
                    <td>{usuario.username}</td>
                    <td><span className="role-badge votante">VOTANTE</span></td>
                    <td className="actions">
                      <button 
                        className="btn-primary btn-small"
                        onClick={() => {
                          setInvitacionForm({ ...invitacionForm, usuarioId: usuario.id });
                          setShowInviteModal(true);
                        }}
                      >
                        <FaEnvelope /> Invitar
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </div>

      {/* Modal Invitar Usuario */}
      {showInviteModal && (
        <div className="modal-overlay">
          <div className="modal">
            <div className="modal-header">
              <h3>Invitar Usuario a Votación</h3>
              <button 
                className="modal-close"
                onClick={() => {
                  setShowInviteModal(false);
                  setInvitacionForm({ usuarioId: '', votacionId: '' });
                }}
              >
                ×
              </button>
            </div>
            <form onSubmit={handleInvitar} className="modal-form">
              <div className="form-group">
                <label>Usuario:</label>
                <select
                  value={invitacionForm.usuarioId}
                  onChange={(e) => setInvitacionForm({...invitacionForm, usuarioId: e.target.value})}
                  required
                >
                  <option value="">Seleccionar usuario</option>
                  {getUsersByRole('VOTANTE').map(usuario => (
                    <option key={usuario.id} value={usuario.id}>{usuario.username}</option>
                  ))}
                </select>
              </div>

              <div className="form-group">
                <label>Votación:</label>
                <select
                  value={invitacionForm.votacionId}
                  onChange={(e) => setInvitacionForm({...invitacionForm, votacionId: e.target.value})}
                  required
                >
                  <option value="">Seleccionar votación</option>
                  {votaciones.map(votacion => (
                    <option key={votacion.id} value={votacion.id}>{votacion.titulo}</option>
                  ))}
                </select>
              </div>

              <div className="modal-actions">
                <button type="submit" className="btn-primary">
                  <FaEnvelope /> Enviar Invitación
                </button>
                <button 
                  type="button" 
                  className="btn-secondary"
                  onClick={() => {
                    setShowInviteModal(false);
                    setInvitacionForm({ usuarioId: '', votacionId: '' });
                  }}
                >
                  Cancelar
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* Modal Ver Detalles de Votación */}
      {showDetailsModal && selectedVotacion && (
        <div className="modal-overlay">
          <div className="modal modal-large">
            <div className="modal-header">
              <h3>Detalles de la Votación</h3>
              <button 
                className="modal-close"
                onClick={() => setShowDetailsModal(false)}
              >
                ×
              </button>
            </div>
            <div className="modal-content">
              <div className="detail-item">
                <strong>Título:</strong> {selectedVotacion.titulo}
              </div>
              <div className="detail-item">
                <strong>Descripción:</strong> {selectedVotacion.descripcion}
              </div>
              <div className="detail-item">
                <strong>Estado:</strong> 
                <span className="status active">Activa</span>
              </div>
              
              {selectedVotacion.preguntas && selectedVotacion.preguntas.length > 0 && (
                <div className="detail-item">
                  <strong>Preguntas:</strong>
                  <div className="preguntas-list">
                    {selectedVotacion.preguntas.map((pregunta, index) => (
                      <div key={index} className="pregunta-detail">
                        <h5>Pregunta {index + 1}: {pregunta.texto}</h5>
                        <ul className="opciones-list">
                          {pregunta.opciones?.map((opcion, opIndex) => (
                            <li key={opIndex}>{opcion.texto}</li>
                          ))}
                        </ul>
                      </div>
                    ))}
                  </div>
                </div>
              )}
            </div>
            <div className="modal-actions">
              <button 
                className="btn-secondary"
                onClick={() => setShowDetailsModal(false)}
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

export default Invitaciones;