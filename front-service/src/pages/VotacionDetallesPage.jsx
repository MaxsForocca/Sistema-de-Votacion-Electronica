// src/pages/VotacionDetallesPage.jsx
import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { FaVoteYea, FaArrowLeft, FaQuestion, FaList, FaClock, FaCheckCircle } from 'react-icons/fa';
import { obtenerVotacionDetalle } from '../services/votacionDetalleService';
import '../styles/votante.css';

const VotacionDetallesPage = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [votacion, setVotacion] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    cargarVotacion();
  }, [id]);

  const cargarVotacion = async () => {
    try {
      setLoading(true);
      const data = await obtenerVotacionDetalle(id);
      setVotacion(data);
    } catch (error) {
      setError('Error al cargar la votación');
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  const handleVolver = () => {
    navigate('/mis-votaciones');
  };

  const handleIrAVotar = () => {
    navigate(`/votacion/${id}`);
  };

  if (loading) return <div className="loading-votante">Cargando detalles...</div>;

  if (!votacion) {
    return (
      <div className="votante-container">
        <div className="error-message">Votación no encontrada</div>
      </div>
    );
  }

  return (
    <div className="votante-container">
      {/* Header */}
      <div className="votante-header">
        <div className="header-content">
          <div className="header-left">
            <button className="btn-back" onClick={handleVolver}>
              <FaArrowLeft /> Volver
            </button>
            <div>
              <h1 className="header-title">
                <FaVoteYea className="header-icon" />
                Detalles de Votación
              </h1>
              <p className="header-subtitle">Información completa sobre la votación</p>
            </div>
          </div>
        </div>
      </div>

      {/* Main Content */}
      <div className="votante-content">
        {error && <div className="error-message">{error}</div>}

        <div className="detalles-container">
          {/* Información General */}
          <div className="info-general">
            <div className="info-header">
              <h2>{votacion.titulo}</h2>
              <span className={`status-badge ${votacion.activa ? 'activa' : 'inactiva'}`}>
                {votacion.activa ? (
                  <><FaCheckCircle /> Activa</>
                ) : (
                  <><FaClock /> Inactiva</>
                )}
              </span>
            </div>
            
            <div className="info-descripcion">
              <h3>Descripción</h3>
              <p>{votacion.descripcion}</p>
            </div>

            <div className="info-estadisticas">
              <div className="stat-item">
                <FaQuestion className="stat-icon" />
                <div>
                  <span className="stat-number">{votacion.preguntas?.length || 0}</span>
                  <span className="stat-label">Preguntas</span>
                </div>
              </div>
              <div className="stat-item">
                <FaList className="stat-icon" />
                <div>
                  <span className="stat-number">
                    {votacion.preguntas?.reduce((total, p) => total + (p.opciones?.length || 0), 0) || 0}
                  </span>
                  <span className="stat-label">Opciones Total</span>
                </div>
              </div>
            </div>
          </div>

          {/* Preview de Preguntas */}
          <div className="preguntas-preview">
            <h3>Vista Previa de Preguntas</h3>
            
            {votacion.preguntas?.map((pregunta, index) => (
              <div key={pregunta.preguntaId} className="pregunta-preview">
                <div className="pregunta-preview-header">
                  <h4>Pregunta {index + 1}</h4>
                  <span className="opciones-count">{pregunta.opciones?.length || 0} opciones</span>
                </div>
                
                <div className="pregunta-preview-texto">
                  {pregunta.texto}
                </div>
                
                <div className="opciones-preview">
                  {pregunta.opciones?.map((opcion, opIndex) => (
                    <div key={opcion.id} className="opcion-preview">
                      <span className="opcion-letter">{String.fromCharCode(65 + opIndex)}</span>
                      <span className="opcion-texto">{opcion.texto}</span>
                    </div>
                  ))}
                </div>
              </div>
            ))}
          </div>

          {/* Información Importante */}
          <div className="info-importante">
            <h3>Información Importante</h3>
            <div className="info-grid">
              <div className="info-card">
                <h4>¿Cómo votar?</h4>
                <ul>
                  <li>Selecciona una opción para cada pregunta</li>
                  <li>Revisa tus respuestas antes de enviar</li>
                  <li>Haz clic en "Enviar Mi Voto"</li>
                </ul>
              </div>
              
              <div className="info-card">
                <h4>Ten en cuenta</h4>
                <ul>
                  <li>Solo puedes votar una vez</li>
                  <li>No podrás cambiar tu voto después de enviarlo</li>
                  <li>Tu voto es completamente anónimo</li>
                </ul>
              </div>
              
              <div className="info-card">
                <h4>Requisitos</h4>
                <ul>
                  <li>Debes responder todas las preguntas</li>
                  <li>La votación debe estar activa</li>
                  <li>Debes estar invitado a esta votación</li>
                </ul>
              </div>
            </div>
          </div>

          {/* Acciones */}
          <div className="acciones-container">
            <button className="btn-secondary-votante" onClick={handleVolver}>
              <FaArrowLeft /> Volver a Mis Votaciones
            </button>
            
            {votacion.activa && (
              <button className="btn-primary-votante" onClick={handleIrAVotar}>
                <FaVoteYea /> Comenzar a Votar
              </button>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default VotacionDetallesPage;