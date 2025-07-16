// src/pages/dashboard/Usuarios.jsx
import React, { useState, useEffect } from 'react';
import { FaUserPlus, FaEdit, FaTrash, FaEye, FaUsers } from 'react-icons/fa';
import { 
  listarUsuarios, 
  crearUsuario, 
  actualizarUsuario, 
  eliminarUsuario 
} from '../../services/usuarioService';
import { listarDepartamentos } from '../../services/departamentoService';
import '../../styles/crud.css';

const Usuarios = () => {
  const [usuarios, setUsuarios] = useState([]);
  const [departamentos, setDepartamentos] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [showViewModal, setShowViewModal] = useState(false);
  const [editMode, setEditMode] = useState(false);
  const [selectedUser, setSelectedUser] = useState(null);
  const [formData, setFormData] = useState({
    username: '',
    password: '',
    idRol: '',
    idDepartamento: ''
  });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const roles = [
    { id: 1, nombre: 'ADMIN' },
    { id: 2, nombre: 'VOTANTE' }
  ];

  useEffect(() => {
    cargarDatos();
  }, []);

  const cargarDatos = async () => {
    try {
      setLoading(true);
      const [usuariosData, departamentosData] = await Promise.all([
        listarUsuarios(),
        listarDepartamentos()
      ]);
      setUsuarios(usuariosData);
      setDepartamentos(departamentosData);
    } catch (error) {
      setError('Error al cargar los datos');
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  const resetForm = () => {
    setFormData({
      username: '',
      password: '',
      idRol: '',
      idDepartamento: ''
    });
    setEditMode(false);
    setSelectedUser(null);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editMode) {
        await actualizarUsuario(selectedUser.id, formData);
      } else {
        await crearUsuario(formData);
      }
      setShowModal(false);
      resetForm();
      cargarDatos();
    } catch (error) {
      setError(error);
    }
  };

  const handleEdit = (usuario) => {
    setFormData({
      username: usuario.username,
      password: '', // No mostrar password actual
      idRol: usuario.idRol,
      idDepartamento: usuario.idDepartamento
    });
    setSelectedUser(usuario);
    setEditMode(true);
    setShowModal(true);
  };

  const handleDelete = async (id) => {
    if (window.confirm('¿Está seguro de eliminar este usuario?')) {
      try {
        await eliminarUsuario(id);
        cargarDatos();
      } catch (error) {
        setError(error);
      }
    }
  };

  const handleView = (usuario) => {
    setSelectedUser(usuario);
    setShowViewModal(true);
  };

  const getRoleName = (idRol) => {
    const rol = roles.find(r => r.id === idRol);
    return rol ? rol.nombre : 'N/A';
  };

  const getDepartmentName = (idDepartamento) => {
    const dept = departamentos.find(d => d.id === idDepartamento);
    return dept ? dept.nombre : 'N/A';
  };

  if (loading) return <div className="loading">Cargando usuarios...</div>;

  return (
    <div className="crud-container">
      <div className="content-title">
        <FaUsers className="title-icon" />
        <h2>Gestión de Usuarios</h2>
      </div>

      <div className="entity-content">
        <div className="crud-header">
          <button 
            className="btn-primary"
            onClick={() => setShowModal(true)}
          >
            <FaUserPlus /> Nuevo Usuario
          </button>
        </div>

        {error && <div className="error-message">{error}</div>}
        <div className="departamentos-list">
        {usuarios.map(usuario => (
          <div className="departamento-card" key={usuario.id}>
            <div className="departamento-info">
              <FaUsers className="departamento-icon" />
              <div>
                <span className="departamento-nombre">{usuario.username}</span>
                <p className="departamento-descripcion">
                  {getRoleName(usuario.idRol)} - {getDepartmentName(usuario.idDepartamento)}
                </p>
              </div>
            </div>
            <div className="actions">
              <button className="btn-view" onClick={() => handleView(usuario)}>
                <FaEye />
              </button>
              <button className="btn-edit" onClick={() => handleEdit(usuario)}>
                <FaEdit />
              </button>
              <button className="btn-delete" onClick={() => handleDelete(usuario.id)}>
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
              <h3>{editMode ? 'Editar Usuario' : 'Nuevo Usuario'}</h3>
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
                <label>Username:</label>
                <input
                  type="text"
                  value={formData.username}
                  onChange={(e) => setFormData({...formData, username: e.target.value})}
                  required
                />
              </div>
              
              <div className="form-group">
                <label>Password:</label>
                <input
                  type="password"
                  value={formData.password}
                  onChange={(e) => setFormData({...formData, password: e.target.value})}
                  required={!editMode}
                  placeholder={editMode ? "Dejar vacío para mantener actual" : ""}
                />
              </div>

              <div className="form-group">
                <label>Rol:</label>
                <select
                  value={formData.idRol}
                  onChange={(e) => setFormData({...formData, idRol: parseInt(e.target.value)})}
                  required
                >
                  <option value="">Seleccionar rol</option>
                  {roles.map(rol => (
                    <option key={rol.id} value={rol.id}>{rol.nombre}</option>
                  ))}
                </select>
              </div>

              <div className="form-group">
                <label>Departamento:</label>
                <select
                  value={formData.idDepartamento}
                  onChange={(e) => setFormData({...formData, idDepartamento: parseInt(e.target.value)})}
                  required
                >
                  <option value="">Seleccionar departamento</option>
                  {departamentos.map(dept => (
                    <option key={dept.id} value={dept.id}>{dept.nombre}</option>
                  ))}
                </select>
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
      {showViewModal && selectedUser && (
        <div className="modal-overlay">
          <div className="modal">
            <div className="modal-header">
              <h3>Detalles del Usuario</h3>
              <button 
                className="modal-close"
                onClick={() => setShowViewModal(false)}
              >
                ×
              </button>
            </div>
            <div className="modal-content">
              <div className="detail-item">
                <strong>ID:</strong> {selectedUser.id}
              </div>
              <div className="detail-item">
                <strong>Username:</strong> {selectedUser.username}
              </div>
              <div className="detail-item">
                <strong>Rol:</strong> {getRoleName(selectedUser.idRol)}
              </div>
              <div className="detail-item">
                <strong>Departamento:</strong> {getDepartmentName(selectedUser.idDepartamento)}
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

export default Usuarios;