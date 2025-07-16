// src/pages/MisVotacionesPage.jsx
import React, { useState, useEffect } from 'react';
import { FaVoteYea, FaEye, FaClock, FaCheckCircle, FaExclamationTriangle } from 'react-icons/fa';
import { useNavigate } from 'react-router-dom';
import { listarVotacionesPorUsuario } from '../services/invitacionService';
import '../styles/votante.css';

const MisVotacionesPage = () => {
  const [votaciones, setVotaciones] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const getUsuario = () => {
    try {
      return JSON.parse(localStorage.getItem('usuario'));
    } catch {
      return null;
    }
  };

  useEffect(() => {
    cargarMisVotaciones();
  }, []);

  const cargarMisVotaciones = async () => {
    try {
      setLoading(true);
      const usuario = getUsuario();

      if (!usuario || !usuario.id) {
        setError('Usuario no encontrado. Por favor, inicie sesión nuevamente.');
        return;
      }

      const data = await listarVotacionesPorUsuario(usuario.id);
      // Asegurar que sea un array
      setVotaciones(Array.isArray(data) ? data : []);
    } catch (error) {
      console.error(error);
      setError('Error al cargar las votaciones');
    } finally {
      setLoading(false);
    }
  };

  const handleVotar = (votacionId) => {
    navigate(`/votacion/${votacionId}`);
  };

  const handleVerDetalles = (votacionId) => {
    navigate(`/votacion/${votacionId}/detalles`);
  };

  const handleLogout = () => {
    localStorage.removeItem('usuario');
    navigate('/login');
  };

  if (loading) return <div className="loading-votante">Cargando tus votaciones...</div>;

  const usuario = getUsuario();

  return (
    <div className="votante-container">
      {/* Header */}
      <div className="votante-header">
        <div className="header-content">
          <div className="header-left">
            <h1 className="header-title">
              <FaVoteYea className="header-icon" />
              VOTE NOW
            </h1>
            <p className="header-subtitle">Tus votaciones disponibles</p>
          </div>
          <div className="header-right">
            <span className="user-info">
              Bienvenido, {usuario?.username || 'Usuario'}
            </span>
            <button className="btn-logout" onClick={handleLogout}>
              Cerrar Sesión
            </button>
          </div>
        </div>
      </div>

      {/* Main Content */}
      <div className="votante-content">
        {error && <div className="error-message">{error}</div>}

        {/* Estadísticas */}
        <div className="stats-votante">
          <div className="stat-card-votante">
            <FaVoteYea className="stat-icon" />
            <div className="stat-info">
              <h3>{votaciones.length}</h3>
              <p>Votaciones Disponibles</p>
            </div>
          </div>
          <div className="stat-card-votante">
            <FaClock className="stat-icon" />
            <div className="stat-info">
              <h3>{votaciones.filter(v => v.activa).length}</h3>
              <p>Activas</p>
            </div>
          </div>
        </div>

        {/* Lista de Votaciones */}
        {votaciones.length === 0 ? (
          <div className="no-votaciones">
            <FaExclamationTriangle size={48} />
            <h3>No tienes votaciones disponibles</h3>
            <p>Cuando seas invitado a una votación, aparecerá aquí.</p>
          </div>
        ) : (
          <div className="votaciones-grid-votante">
            {votaciones.map(votacion => (
              <div key={votacion.id} className="votacion-card-votante">
                <div className="card-header.on-vote">
                  <h3>{votacion.titulo}</h3>
                  <span className={`status-badge ${votacion.activa ? 'activa' : 'inactiva'}`}>
                    {votacion.activa ? (
                      <><FaCheckCircle /> Activa</>
                    ) : (
                      <><FaClock /> Inactiva</>
                    )}
                  </span>
                </div>

                <div className="card-body">
                  <p className="votacion-descripcion">{votacion.descripcion}</p>
                </div>

                <div className="card-footer">
                  <button
                    className="btn-secondary-votante"
                    onClick={() => handleVerDetalles(votacion.id)}
                  >
                    <FaEye /> Ver Detalles
                  </button>

                  {votacion.activa && (
                    <button
                      className="btn-primary-votante"
                      onClick={() => handleVotar(votacion.id)}
                    >
                      <FaVoteYea /> Votar Ahora
                    </button>
                  )}
                </div>
              </div>
            ))}
          </div>
        )}

        {/* Información adicional */}
        <div className="info-section">
          <div className="info-card">
            <h4>¿Cómo funciona el sistema de votación?</h4>
            <ul>
              <li>Solo puedes votar en votaciones a las que hayas sido invitado</li>
              <li>Cada votación puede tener múltiples preguntas</li>
              <li>Solo puedes votar una vez por pregunta</li>
              <li>Tus votos son anónimos y seguros</li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  );
};

export default MisVotacionesPage;
