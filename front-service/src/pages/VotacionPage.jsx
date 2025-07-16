// src/pages/VotacionPage.jsx
import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { FaVoteYea, FaArrowLeft, FaCheckCircle, FaExclamationTriangle } from 'react-icons/fa';
import { obtenerVotacionDetalle } from '../services/votacionDetalleService';
import { emitirVoto } from '../services/votoService';
import '../styles/votante.css';

const VotacionPage = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [votacion, setVotacion] = useState(null);
  const [respuestas, setRespuestas] = useState({});
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState('');
  const [votoExitoso, setVotoExitoso] = useState(false);

  useEffect(() => {
    cargarVotacion();
  }, [id]);

  const cargarVotacion = async () => {
    try {
      setLoading(true);
      const data = await obtenerVotacionDetalle(id);
      setVotacion(data);
      
      // Inicializar respuestas vacías
      const respuestasIniciales = {};
      data.preguntas?.forEach(pregunta => {
        respuestasIniciales[pregunta.preguntaId] = null;
      });
      setRespuestas(respuestasIniciales);
    } catch (error) {
      setError('Error al cargar la votación');
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  const handleSeleccionarOpcion = (preguntaId, opcionId) => {
    setRespuestas(prev => ({
      ...prev,
      [preguntaId]: opcionId
    }));
  };

  const validarRespuestas = () => {
    const preguntasRequeridas = votacion.preguntas?.length || 0;
    const respuestasCompletas = Object.values(respuestas).filter(r => r !== null).length;
    return preguntasRequeridas === respuestasCompletas;
  };

  const handleSubmitVoto = async () => {
    if (!validarRespuestas()) {
      setError('Debes responder todas las preguntas antes de enviar tu voto');
      return;
    }

    try {
      setSubmitting(true);
      const usuario = JSON.parse(localStorage.getItem('usuario'));
      
      const respuestaVotacion = {
        usuarioId: usuario.id,
        votacionId: parseInt(id),
        respuestas: Object.entries(respuestas).map(([preguntaId, opcionId]) => ({
          preguntaId: parseInt(preguntaId),
          opcionId: parseInt(opcionId)
        }))
      };

      await emitirVoto(respuestaVotacion);
      setVotoExitoso(true);
      setError('');
    } catch (error) {
      setError(error);
    } finally {
      setSubmitting(false);
    }
  };

  const handleVolver = () => {
    navigate('/mis-votaciones');
  };

  if (loading) return <div className="loading-votante">Cargando votación...</div>;

  if (votoExitoso) {
    return (
      <div className="votante-container">
        <div className="votante-header">
          <div className="header-content">
            <h1 className="header-title">
              <FaVoteYea className="header-icon" />
              VOTE NOW
            </h1>
          </div>
        </div>
        
        <div className="votante-content">
          <div className="voto-exitoso">
            <FaCheckCircle size={64} className="success-icon" />
            <h2>¡Voto registrado exitosamente!</h2>
            <p>Tu voto ha sido registrado de forma segura y anónima.</p>
            <p>Gracias por participar en: <strong>{votacion?.titulo}</strong></p>
            <button className="btn-primary-votante" onClick={handleVolver}>
              <FaArrowLeft /> Volver a Mis Votaciones
            </button>
          </div>
        </div>
      </div>
    );
  }

  if (!votacion) {
    return (
      <div className="votante-container">
        <div className="error-message">Votación no encontrada</div>
      </div>
    );
  }

  const preguntasRespondidas = Object.values(respuestas).filter(r => r !== null).length;
  const totalPreguntas = votacion.preguntas?.length || 0;
  const progreso = totalPreguntas > 0 ? (preguntasRespondidas / totalPreguntas) * 100 : 0;

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
                {votacion.titulo}
              </h1>
              <p className="header-subtitle">{votacion.descripcion}</p>
            </div>
          </div>
        </div>
      </div>

      {/* Progress Bar */}
      <div className="progress-section">
        <div className="progress-info">
          <span>Progreso: {preguntasRespondidas} de {totalPreguntas} preguntas</span>
          <span>{Math.round(progreso)}% completado</span>
        </div>
        <div className="progress-bar">
          <div 
            className="progress-fill" 
            style={{ width: `${progreso}%` }}
          ></div>
        </div>
      </div>

      {/* Main Content */}
      <div className="votante-content">
        {error && <div className="error-message">{error}</div>}

        <div className="votacion-form">
          {votacion.preguntas?.map((pregunta, index) => (
            <div key={pregunta.preguntaId} className="pregunta-container">
              <div className="pregunta-header">
                <h3>Pregunta {index + 1}</h3>
                {respuestas[pregunta.preguntaId] && (
                  <FaCheckCircle className="pregunta-completada" />
                )}
              </div>
              
              <h4 className="pregunta-texto">{pregunta.texto}</h4>
              
              <div className="opciones-container">
                {pregunta.opciones?.map(opcion => (
                  <label 
                    key={opcion.id} 
                    className={`opcion-label ${respuestas[pregunta.preguntaId] === opcion.id ? 'selected' : ''}`}
                  >
                    <input
                      type="radio"
                      name={`pregunta-${pregunta.preguntaId}`}
                      value={opcion.id}
                      checked={respuestas[pregunta.preguntaId] === opcion.id}
                      onChange={() => handleSeleccionarOpcion(pregunta.preguntaId, opcion.id)}
                    />
                    <div className="opcion-content">
                      <div className="radio-custom"></div>
                      <span>{opcion.texto}</span>
                    </div>
                  </label>
                ))}
              </div>
            </div>
          ))}

          {/* Submit Section */}
          <div className="submit-section">
            <div className="submit-info">
              <FaExclamationTriangle className="warning-icon" />
              <div>
                <h4>Antes de enviar tu voto:</h4>
                <ul>
                  <li>Revisa todas tus respuestas</li>
                  <li>Una vez enviado, no podrás modificar tu voto</li>
                  <li>Tu voto será registrado de forma anónima</li>
                </ul>
              </div>
            </div>

            <button 
              className={`btn-submit-voto ${!validarRespuestas() || submitting ? 'disabled' : ''}`}
              onClick={handleSubmitVoto}
              disabled={!validarRespuestas() || submitting}
            >
              {submitting ? (
                <>Enviando voto...</>
              ) : (
                <><FaVoteYea /> Enviar Mi Voto</>
              )}
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default VotacionPage;