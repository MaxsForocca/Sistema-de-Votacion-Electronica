// src/pages/dashboard/Inicio.jsx
import React, { useState, useEffect } from 'react';
import { 
  FaUsers, 
  FaVoteYea, 
  FaMapMarkedAlt, 
  FaTags, 
  FaClipboardCheck,
  FaFilePdf,
  FaChartBar,
  FaClock,
  FaCheckCircle,
  FaExclamationTriangle
} from 'react-icons/fa';
import { listarUsuarios } from '../../services/usuarioService';
import { listarDepartamentos } from '../../services/departamentoService';
import { listarCategorias } from '../../services/categoriaService';
import { listarVotaciones } from '../../services/votacionService';
import { listarAuditorias } from '../../services/auditoriaService';
import { listarDocumentos } from '../../services/documentoService';
import '../../styles/crud.css';

const Inicio = () => {
  const [stats, setStats] = useState({
    usuarios: 0,
    departamentos: 0,
    categorias: 0,
    votaciones: 0,
    votacionesActivas: 0,
    auditorias: 0,
    documentos: 0
  });
  const [recentActivity, setRecentActivity] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    cargarDashboard();
  }, []);

  const cargarDashboard = async () => {
    try {
      setLoading(true);
      
      // Cargar datos en paralelo
      const [
        usuariosData,
        departamentosData,
        categoriasData,
        votacionesData,
        auditoriasData,
        documentosData
      ] = await Promise.all([
        listarUsuarios().catch(() => []),
        listarDepartamentos().catch(() => []),
        listarCategorias().catch(() => []),
        listarVotaciones().catch(() => []),
        listarAuditorias().catch(() => []),
        listarDocumentos().catch(() => [])
      ]);

      const votacionesActivas = votacionesData.filter(v => v.activa).length;

      setStats({
        usuarios: usuariosData.length,
        departamentos: departamentosData.length,
        categorias: categoriasData.length,
        votaciones: votacionesData.length,
        votacionesActivas,
        auditorias: auditoriasData.length,
        documentos: documentosData.length
      });

      // Actividad reciente de auditorías
      const actividadReciente = auditoriasData
        .sort((a, b) => new Date(b.fecha) - new Date(a.fecha))
        .slice(0, 5);
      
      setRecentActivity(actividadReciente);

    } catch (error) {
      setError('Error al cargar datos del dashboard');
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  const formatFecha = (fecha) => {
    return new Date(fecha).toLocaleString('es-ES', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  };

  const getActivityIcon = (accion) => {
    switch(accion) {
      case 'VOTO_EXITOSO': return <FaCheckCircle className="activity-icon success" />;
      case 'VOTO_RECHAZADO': return <FaExclamationTriangle className="activity-icon error" />;
      default: return <FaClock className="activity-icon default" />;
    }
  };

  if (loading) return <div className="loading">Cargando dashboard...</div>;

  return (
    <div className="crud-container">
      <div className="content-title">
        <FaChartBar className="title-icon" />
        <h2>Panel de Control</h2>
      </div>

      <div className="entity-content">
        {error && <div className="error-message">{error}</div>}
        
        {/* Métricas principales */}
        <div className="dashboard-metrics">
          <div className="metric-card">
            <div className="metric-icon users">
              <FaUsers />
            </div>
            <div className="metric-info">
              <h3>{stats.usuarios}</h3>
              <p>Usuarios Registrados</p>
            </div>
          </div>

          <div className="metric-card">
            <div className="metric-icon departments">
              <FaMapMarkedAlt />
            </div>
            <div className="metric-info">
              <h3>{stats.departamentos}</h3>
              <p>Departamentos</p>
            </div>
          </div>

          <div className="metric-card">
            <div className="metric-icon categories">
              <FaTags />
            </div>
            <div className="metric-info">
              <h3>{stats.categorias}</h3>
              <p>Categorías</p>
            </div>
          </div>

          <div className="metric-card">
            <div className="metric-icon votings">
              <FaVoteYea />
            </div>
            <div className="metric-info">
              <h3>{stats.votaciones}</h3>
              <p>Total Votaciones</p>
            </div>
          </div>

          <div className="metric-card active">
            <div className="metric-icon active-votings">
              <FaVoteYea />
            </div>
            <div className="metric-info">
              <h3>{stats.votacionesActivas}</h3>
              <p>Votaciones Activas</p>
            </div>
          </div>

          <div className="metric-card">
            <div className="metric-icon audits">
              <FaClipboardCheck />
            </div>
            <div className="metric-info">
              <h3>{stats.auditorias}</h3>
              <p>Registros de Auditoría</p>
            </div>
          </div>

          <div className="metric-card">
            <div className="metric-icon documents">
              <FaFilePdf />
            </div>
            <div className="metric-info">
              <h3>{stats.documentos}</h3>
              <p>Documentos</p>
            </div>
          </div>
        </div>

        {/* Panel de información */}
        <div className="dashboard-panels">
          {/* Estado del sistema */}
          <div className="panel">
            <div className="panel-header">
              <h3><FaCheckCircle /> Estado del Sistema</h3>
            </div>
            <div className="panel-content">
              <div className="system-status">
                <div className="status-item">
                  <div className="status-indicator active"></div>
                  <span>Servicio de Autenticación</span>
                  <span className="status-text active">Activo</span>
                </div>
                <div className="status-item">
                  <div className="status-indicator active"></div>
                  <span>Servicio de Votaciones</span>
                  <span className="status-text active">Activo</span>
                </div>
                <div className="status-item">
                  <div className="status-indicator active"></div>
                  <span>Servicio de Documentos</span>
                  <span className="status-text active">Activo</span>
                </div>
              </div>
            </div>
          </div>

          {/* Actividad reciente */}
          <div className="panel">
            <div className="panel-header">
              <h3><FaClock /> Actividad Reciente</h3>
            </div>
            <div className="panel-content">
              {recentActivity.length > 0 ? (
                <div className="activity-list">
                  {recentActivity.map((activity, index) => (
                    <div key={index} className="activity-item">
                      {getActivityIcon(activity.accion)}
                      <div className="activity-info">
                        <p className="activity-action">{activity.accion}</p>
                        <p className="activity-detail">{activity.detalle}</p>
                        <p className="activity-time">{formatFecha(activity.fecha)}</p>
                      </div>
                    </div>
                  ))}
                </div>
              ) : (
                <div className="no-activity">
                  <p>No hay actividad reciente</p>
                </div>
              )}
            </div>
          </div>
        </div>

        {/* Resumen de votaciones */}
        <div className="dashboard-summary">
          <div className="summary-card">
            <h4>Resumen de Votaciones</h4>
            <div className="summary-stats">
              <div className="summary-item">
                <span className="summary-number">{stats.votacionesActivas}</span>
                <span className="summary-label">Activas</span>
              </div>
              <div className="summary-item">
                <span className="summary-number">{stats.votaciones - stats.votacionesActivas}</span>
                <span className="summary-label">Inactivas</span>
              </div>
              <div className="summary-item">
                <span className="summary-number">{((stats.votacionesActivas / (stats.votaciones || 1)) * 100).toFixed(1)}%</span>
                <span className="summary-label">Tasa de Actividad</span>
              </div>
            </div>
          </div>

          <div className="summary-card">
            <h4>Distribución por Departamentos</h4>
            <div className="summary-content">
              <p>Total de departamentos configurados: <strong>{stats.departamentos}</strong></p>
              <p>Usuarios distribuidos en el sistema: <strong>{stats.usuarios}</strong></p>
              <p>Promedio de usuarios por departamento: <strong>{(stats.usuarios / (stats.departamentos || 1)).toFixed(1)}</strong></p>
            </div>
          </div>
        </div>

        {/* Acciones rápidas */}
        <div className="quick-actions">
          <h3>Acciones Rápidas</h3>
          <div className="actions-grid">
            <button className="action-btn" onClick={() => window.location.href = '#/dashboard/usuarios'}>
              <FaUsers />
              <span>Gestionar Usuarios</span>
            </button>
            <button className="action-btn" onClick={() => window.location.href = '#/dashboard/votaciones'}>
              <FaVoteYea />
              <span>Nueva Votación</span>
            </button>
            <button className="action-btn" onClick={() => window.location.href = '#/dashboard/auditoria'}>
              <FaClipboardCheck />
              <span>Ver Auditoría</span>
            </button>
            <button className="action-btn" onClick={() => window.location.href = '#/dashboard/documentos'}>
              <FaFilePdf />
              <span>Subir Documento</span>
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Inicio;