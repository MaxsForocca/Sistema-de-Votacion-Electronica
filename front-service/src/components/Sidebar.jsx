// src/components/Sidebar.jsx
import React from 'react';
import {
  FaHome,
  FaVoteYea,
  FaUsers,
  FaMapMarkedAlt,
  FaTags,
  FaClipboardCheck,
  FaFilePdf
} from 'react-icons/fa';
import { NavLink } from 'react-router-dom';
import '../styles/sidebar.css';

const Sidebar = () => {
  return (
    <aside className="sidebar">
      <div className="sidebar-title">VOTE NOW</div>
      <nav className="sidebar-nav">
        <NavLink
          to="/dashboard/inicio"
          className={({ isActive }) =>
            isActive ? 'sidebar-item selected' : 'sidebar-item'
          }
        >
          <FaHome className="sidebar-icon" />
          <span>Inicio</span>
        </NavLink>

        <div className="category">Auth</div>
        <NavLink
          to="/dashboard/usuarios"
          className={({ isActive }) =>
            isActive ? 'sidebar-item selected' : 'sidebar-item'
          }
        >
          <FaUsers className="sidebar-icon" />
          <span>Usuarios</span>
        </NavLink>
        <NavLink
          to="/dashboard/departamentos"
          className={({ isActive }) =>
            isActive ? 'sidebar-item selected' : 'sidebar-item'
          }
        >
          <FaMapMarkedAlt className="sidebar-icon" />
          <span>Departamentos</span>
        </NavLink>

        <div className="category">Voting</div>
        <NavLink
          to="/dashboard/categoria"
          className={({ isActive }) =>
            isActive ? 'sidebar-item selected' : 'sidebar-item'
          }
        >
          <FaTags className="sidebar-icon" />
          <span>Categoría</span>
        </NavLink>
        <NavLink
          to="/dashboard/votaciones"
          className={({ isActive }) =>
            isActive ? 'sidebar-item selected' : 'sidebar-item'
          }
        >
          <FaVoteYea className="sidebar-icon" />
          <span>Votaciones</span>
        </NavLink>
        <NavLink
          to="/dashboard/auditoria"
          className={({ isActive }) =>
            isActive ? 'sidebar-item selected' : 'sidebar-item'
          }
        >
          <FaClipboardCheck className="sidebar-icon" />
          <span>Auditoría</span>
        </NavLink>

        <div className="category">Documents</div>
        <NavLink
          to="/dashboard/documentos"
          className={({ isActive }) =>
            isActive ? 'sidebar-item selected' : 'sidebar-item'
          }
        >
          <FaFilePdf className="sidebar-icon" />
          <span>Documentos</span>
        </NavLink>
      </nav>
    </aside>
  );
};

export default Sidebar;
