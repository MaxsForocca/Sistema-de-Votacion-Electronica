// src/pages/DashboardPage.jsx
import React from 'react';
import Sidebar from '../components/Sidebar';
import '../styles/dashboard.css';
import { Outlet } from 'react-router-dom';

const DashboardPage = () => {
  return (
    <div className="dashboard-container">
      <Sidebar />
      <div className="dashboard-content">
        <Outlet /> {/* Aquí se inyectarán las subrutas */}
      </div>
    </div>
  );
};

export default DashboardPage;
