// src/pages/dashboard/Auditoria.jsx
import React, { useState, useEffect } from 'react';
import { FaClipboardCheck, FaEye, FaFilter, FaDownload } from 'react-icons/fa';
import { listarAuditorias } from '../../services/auditoriaService';
import '../../styles/crud.css';

const Auditoria = () => {
  const [auditorias, setAuditorias] = useState([]);
  const [auditoriasFiltered, setAuditoriasFiltered] = useState([]);
  const [showViewModal, setShowViewModal] = useState(false);
  const [selectedAuditoria, setSelectedAuditoria] = useState(null);
  const [filters, setFilters] = useState({
    accion: '',
    usuarioId: '',
    fechaDesde: '',
    fechaHasta: ''
  });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    cargarAuditorias();
  }, []);

  useEffect(() => {
    aplicarFiltros();
  }, [auditorias, filters]);

  const cargarAuditorias = async () => {
    try {
      setLoading(true);
      const data = await listarAuditorias();
      setAuditorias(data);
    } catch (error) {
      setError('Error al cargar auditorías');
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  const aplicarFiltros = () => {
    let filtered = [...auditorias];

    if (filters.accion) {
      filtered = filtered.filter(a => 
        a.accion.toLowerCase().includes(filters.accion.toLowerCase())
      );
    }

    if (filters.usuarioId) {
      filtered = filtered.filter(a => 
        a.usuarioId?.toString().includes(filters.usuarioId)
      );
    }

    if (filters.fechaDesde) {
      filtered = filtered.filter(a => 
        new Date(a.fecha) >= new Date(filters.fechaDesde)
      );
    }

    if (filters.fechaHasta) {
      filtered = filtered.filter(a => 
        new Date(a.fecha) <= new Date(filters.fechaHasta)
      );
    }

    setAuditoriasFiltered(filtered);
  };

  const handleView = (auditoria) => {
    setSelectedAuditoria(auditoria);
    setShowViewModal(true);
  };

  const formatFecha = (fecha) => {
    return new Date(fecha).toLocaleString('es-ES', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit',
      second: '2-digit'
    });
  };

  const exportarCSV = () => {
    const headers = ['ID', 'Acción', 'Detalle', 'Usuario ID', 'Fecha'];
    const csvData = [
      headers.join(','),
      ...auditoriasFiltered.map(a => [
        a.id,
        `"${a.accion}"`,
        `"${a.detalle}"`,
        a.usuarioId || 'N/A',
        `"${formatFecha(a.fecha)}"`
      ].join(','))
    ].join('\n');

    const blob = new Blob([csvData], { type: 'text/csv' });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `auditoria_${new Date().toISOString().split('T')[0]}.csv`;
    a.click();
    window.URL.revokeObjectURL(url);
  };

  const limpiarFiltros = () => {
    setFilters({
      accion: '',
      usuarioId: '',
      fechaDesde: '',
      fechaHasta: ''
    });
  };

  const getAccionClass = (accion) => {
    switch(accion) {
      case 'VOTO_EXITOSO': return 'success';
      case 'VOTO_RECHAZADO': return 'error';
      case 'LOGIN': return 'info';
      case 'LOGOUT': return 'info';
      default: return 'default';
    }
  };

  if (loading) return <div className="loading">Cargando auditorías...</div>;

  return (
    <div className="crud-container">
      <div className="content-title">
        <FaClipboardCheck className="title-icon" />
        <h2>Auditoría del Sistema</h2>
      </div>

      <div className="entity-content">
        <div className="crud-header">
          <div className="filters-section">
            <div className="filter-group">
              <input
                type="text"
                placeholder="Filtrar por acción"
                value={filters.accion}
                onChange={(e) => setFilters({...filters, accion: e.target.value})}
              />
              <input
                type="text"
                placeholder="Usuario ID"
                value={filters.usuarioId}
                onChange={(e) => setFilters({...filters, usuarioId: e.target.value})}
              />
              <input
                type="date"
                placeholder="Fecha desde"
                value={filters.fechaDesde}
                onChange={(e) => setFilters({...filters, fechaDesde: e.target.value})}
              />
              <input
                type="date"
                placeholder="Fecha hasta"
                value={filters.fechaHasta}
                onChange={(e) => setFilters({...filters, fechaHasta: e.target.value})}
              />
              <button className="btn-secondary" onClick={limpiarFiltros}>
                Limpiar
              </button>
            </div>
          </div>
          
          <div className="actions-group">
            <button className="btn-primary" onClick={exportarCSV}>
              <FaDownload /> Exportar CSV
            </button>
          </div>
        </div>

        {error && <div className="error-message">{error}</div>}

        <div className="stats-section">
          <div className="stat-card">
            <h4>Total de Registros</h4>
            <span>{auditoriasFiltered.length}</span>
          </div>
          <div className="stat-card">
            <h4>Votos Exitosos</h4>
            <span>{auditoriasFiltered.filter(a => a.accion === 'VOTO_EXITOSO').length}</span>
          </div>
          <div className="stat-card">
            <h4>Votos Rechazados</h4>
            <span>{auditoriasFiltered.filter(a => a.accion === 'VOTO_RECHAZADO').length}</span>
          </div>
        </div>

        <div className="table-container">
          <table className="crud-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Acción</th>
                <th>Detalle</th>
                <th>Usuario ID</th>
                <th>Fecha</th>
                <th>Acciones</th>
              </tr>
            </thead>
            <tbody>
              {auditoriasFiltered.map(auditoria => (
                <tr key={auditoria.id}>
                  <td>{auditoria.id}</td>
                  <td>
                    <span className={`action-badge ${getAccionClass(auditoria.accion)}`}>
                      {auditoria.accion}
                    </span>
                  </td>
                  <td>{auditoria.detalle}</td>
                  <td>{auditoria.usuarioId || 'N/A'}</td>
                  <td>{formatFecha(auditoria.fecha)}</td>
                  <td className="actions">
                    <button 
                      className="btn-view"
                      onClick={() => handleView(auditoria)}
                    >
                      <FaEye />
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>

        {auditoriasFiltered.length === 0 && !loading && (
          <div className="no-data">
            <p>No se encontraron registros de auditoría con los filtros aplicados.</p>
          </div>
        )}
      </div>

      {/* Modal Ver Detalles */}
      {showViewModal && selectedAuditoria && (
        <div className="modal-overlay">
          <div className="modal">
            <div className="modal-header">
              <h3>Detalles de Auditoría</h3>
              <button 
                className="modal-close"
                onClick={() => setShowViewModal(false)}
              >
                ×
              </button>
            </div>
            <div className="modal-content">
              <div className="detail-item">
                <strong>ID:</strong> {selectedAuditoria.id}
              </div>
              <div className="detail-item">
                <strong>Acción:</strong> 
                <span className={`action-badge ${getAccionClass(selectedAuditoria.accion)}`}>
                  {selectedAuditoria.accion}
                </span>
              </div>
              <div className="detail-item">
                <strong>Detalle:</strong> {selectedAuditoria.detalle}
              </div>
              <div className="detail-item">
                <strong>Usuario ID:</strong> {selectedAuditoria.usuarioId || 'N/A'}
              </div>
              <div className="detail-item">
                <strong>Fecha y Hora:</strong> {formatFecha(selectedAuditoria.fecha)}
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

export default Auditoria;