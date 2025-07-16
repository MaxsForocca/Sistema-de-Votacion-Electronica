// src/pages/dashboard/Documentos.jsx
import React, { useState, useEffect } from 'react';
import { FaUpload, FaDownload, FaFile, FaFilePdf, FaFileWord, FaFileImage, FaFileAlt, FaSync } from 'react-icons/fa';
import { 
  listarDocumentos, 
  subirDocumento, 
  descargarDocumento 
} from '../../services/documentoService';
import '../../styles/crud.css';

const Documentos = () => {
  const [documentos, setDocumentos] = useState([]);
  const [showUploadModal, setShowUploadModal] = useState(false);
  const [selectedFile, setSelectedFile] = useState(null);
  const [uploadProgress, setUploadProgress] = useState(0);
  const [uploading, setUploading] = useState(false);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    cargarDocumentos();
  }, []);

  const cargarDocumentos = async () => {
    try {
      setLoading(true);
      const data = await listarDocumentos();
      setDocumentos(data);
    } catch (error) {
      setError('Error al cargar documentos');
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  const handleFileSelect = (event) => {
    const file = event.target.files[0];
    if (file) {
      setSelectedFile(file);
    }
  };

  const handleUpload = async () => {
    if (!selectedFile) {
      setError('Por favor seleccione un archivo');
      return;
    }

    try {
      setUploading(true);
      setUploadProgress(0);
      
      const formData = new FormData();
      formData.append('archivo', selectedFile);

      // Simular progreso de carga
      const progressInterval = setInterval(() => {
        setUploadProgress(prev => {
          if (prev >= 90) {
            clearInterval(progressInterval);
            return prev;
          }
          return prev + 10;
        });
      }, 200);

      await subirDocumento(formData);
      
      clearInterval(progressInterval);
      setUploadProgress(100);
      
      setTimeout(() => {
        setShowUploadModal(false);
        setSelectedFile(null);
        setUploadProgress(0);
        cargarDocumentos();
      }, 1000);

    } catch (error) {
      setError(error);
    } finally {
      setUploading(false);
    }
  };

  const handleDownload = async (filename) => {
    try {
      const blob = await descargarDocumento(filename);
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = filename;
      a.click();
      window.URL.revokeObjectURL(url);
    } catch (error) {
      setError('Error al descargar el archivo');
      console.error('Error en descarga:', error);
    }
  };

  const getFileIcon = (filename) => {
    const extension = filename.split('.').pop().toLowerCase();
    switch (extension) {
      case 'pdf':
        return <FaFilePdf className="file-icon pdf" />;
      case 'doc':
      case 'docx':
        return <FaFileWord className="file-icon word" />;
      case 'jpg':
      case 'jpeg':
      case 'png':
      case 'gif':
        return <FaFileImage className="file-icon image" />;
      case 'txt':
        return <FaFileAlt className="file-icon text" />;
      default:
        return <FaFile className="file-icon default" />;
    }
  };

  const formatFileSize = (bytes) => {
    if (!bytes) return 'N/A';
    const sizes = ['Bytes', 'KB', 'MB', 'GB'];
    const i = Math.floor(Math.log(bytes) / Math.log(1024));
    return Math.round(bytes / Math.pow(1024, i) * 100) / 100 + ' ' + sizes[i];
  };

  if (loading) return <div className="loading">Cargando documentos...</div>;

  return (
    <div className="crud-container">
      <div className="content-title">
        <FaFilePdf className="title-icon" />
        <h2>Gestión de Documentos</h2>
      </div>

      <div className="entity-content">
        <div className="crud-header">
          <button 
            className="btn-primary"
            onClick={() => setShowUploadModal(true)}
          >
            <FaUpload /> Subir Documento
          </button>
          
          <button 
            className="btn-secondary"
            onClick={cargarDocumentos}
          >
            <FaSync /> Actualizar Lista
          </button>
        </div>

        {error && <div className="error-message">{error}</div>}

        <div className="stats-section">
          <div className="stat-card">
            <h4>Total de Documentos</h4>
            <span>{documentos.length}</span>
          </div>
          <div className="stat-card">
            <h4>Documentos PDF</h4>
            <span>{documentos.filter(d => d.endsWith('.pdf')).length}</span>
          </div>
          <div className="stat-card">
            <h4>Imágenes</h4>
            <span>{documentos.filter(d => /\.(jpg|jpeg|png|gif)$/i.test(d)).length}</span>
          </div>
        </div>

        <div className="documents-grid">
          {documentos.map((filename, index) => (
            <div key={index} className="document-card">
              <div className="document-icon">
                {getFileIcon(filename)}
              </div>
              <div className="document-info">
                <h4>{filename}</h4>
                <p>Archivo distribuido</p>
              </div>
              <div className="document-actions">
                <button 
                  className="btn-download"
                  onClick={() => handleDownload(filename)}
                  title="Descargar"
                >
                  <FaDownload />
                </button>
              </div>
            </div>
          ))}
        </div>

        {documentos.length === 0 && !loading && (
          <div className="no-data">
            <FaFile size={48} />
            <p>No hay documentos disponibles</p>
            <button 
              className="btn-primary"
              onClick={() => setShowUploadModal(true)}
            >
              Subir primer documento
            </button>
          </div>
        )}
      </div>

      {/* Modal Subir Documento */}
      {showUploadModal && (
        <div className="modal-overlay">
          <div className="modal">
            <div className="modal-header">
              <h3>Subir Documento</h3>
              <button 
                className="modal-close"
                onClick={() => {
                  setShowUploadModal(false);
                  setSelectedFile(null);
                  setUploadProgress(0);
                }}
              >
                ×
              </button>
            </div>
            <div className="modal-content">
              <div className="upload-area">
                {!selectedFile ? (
                  <div className="upload-placeholder">
                    <FaUpload size={48} />
                    <p>Seleccione un archivo para subir</p>
                    <input
                      type="file"
                      onChange={handleFileSelect}
                      accept="*/*"
                      className="file-input"
                    />
                  </div>
                ) : (
                  <div className="file-selected">
                    {getFileIcon(selectedFile.name)}
                    <div className="file-details">
                      <h4>{selectedFile.name}</h4>
                      <p>{formatFileSize(selectedFile.size)}</p>
                    </div>
                  </div>
                )}
              </div>

              {uploading && (
                <div className="progress-section">
                  <div className="progress-bar">
                    <div 
                      className="progress-fill"
                      style={{ width: `${uploadProgress}%` }}
                    ></div>
                  </div>
                  <p>Subiendo... {uploadProgress}%</p>
                </div>
              )}
            </div>
            <div className="modal-actions">
              <button 
                className="btn-primary"
                onClick={handleUpload}
                disabled={!selectedFile || uploading}
              >
                {uploading ? 'Subiendo...' : 'Subir Archivo'}
              </button>
              <button 
                className="btn-secondary"
                onClick={() => {
                  setShowUploadModal(false);
                  setSelectedFile(null);
                  setUploadProgress(0);
                }}
                disabled={uploading}
              >
                Cancelar
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default Documentos;