// src/pages/HomePage.jsx
import { useEffect } from "react";
import { useNavigate } from "react-router-dom";

export default function HomePage() {
  const navigate = useNavigate();

  useEffect(() => {
    // Verificar si hay un usuario logueado
    const usuario = JSON.parse(localStorage.getItem('usuario'));
    
    if (!usuario) {
      // Si no hay usuario, redirigir al login
      navigate('/login');
      return;
    }

    // Redirigir según el rol del usuario
    if (usuario.rol === 'ADMIN') {
      navigate('/dashboard');
    } else if (usuario.rol === 'VOTANTE') {
      navigate('/mis-votaciones');
    } else {
      // Rol no reconocido, redirigir al login
      navigate('/login');
    }
  }, [navigate]);

  // Mostrar una pantalla de carga mientras se redirige
  return (
    <div style={{ 
      display: "flex", 
      justifyContent: "center", 
      alignItems: "center", 
      height: "100vh",
      background: "linear-gradient(135deg, #667eea 0%, #764ba2 100%)",
      color: "white",
      fontSize: "1.2rem"
    }}>
      <div style={{ textAlign: "center" }}>
        <div style={{ 
          width: "50px", 
          height: "50px", 
          border: "3px solid rgba(255,255,255,0.3)", 
          borderTop: "3px solid white", 
          borderRadius: "50%", 
          animation: "spin 1s linear infinite",
          margin: "0 auto 1rem"
        }}></div>
        <p>Redirigiendo...</p>
        <style>{`
          @keyframes spin {
            0% { transform: rotate(0deg); }
            100% { transform: rotate(360deg); }
          }
        `}</style>
      </div>
    </div>
  );
}