// src/AppRouter.jsx
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import LoginPage from "./pages/LoginPage";
import HomePage from "./pages/HomePage";
import DashboardPage from "./pages/DashboardPage";
import MisVotacionesPage from "./pages/MisVotacionesPage";
import VotacionPage from "./pages/VotacionPage";
import VotacionDetallesPage from "./pages/VotacionDetallesPage";

// Subpáginas del dashboard
import Inicio from "./pages/dashboard/Inicio";
import Usuarios from "./pages/dashboard/Usuarios";
import Departamentos from "./pages/dashboard/Departamentos";
import Categoria from "./pages/dashboard/Categoria";
import Votaciones from "./pages/dashboard/Votaciones";
import Invitaciones from "./pages/dashboard/Invitaciones";
import Auditoria from "./pages/dashboard/Auditoria";
import Documentos from "./pages/dashboard/Documentos";

export default function AppRouter() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Navigate to="/login" replace />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/home" element={<HomePage />} />
        
        {/* Rutas para VOTANTES */}
        <Route path="/mis-votaciones" element={<MisVotacionesPage />} />
        <Route path="/votacion/:id" element={<VotacionPage />} />
        <Route path="/votacion/:id/detalles" element={<VotacionDetallesPage />} />

        {/* Dashboard para ADMIN */}
        <Route path="/dashboard" element={<DashboardPage />}>
          <Route path="inicio" element={<Inicio />} />
          <Route path="usuarios" element={<Usuarios />} />
          <Route path="departamentos" element={<Departamentos />} />
          <Route path="categoria" element={<Categoria />} />
          <Route path="votaciones" element={<Votaciones />} />
          <Route path="invitaciones" element={<Invitaciones />} />
          <Route path="auditoria" element={<Auditoria />} />
          <Route path="documentos" element={<Documentos />} />
          <Route index element={<Inicio />} /> {/* dashboard/ por defecto muestra Inicio */}
        </Route>
      </Routes>
    </Router>
  );
}