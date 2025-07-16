// src/components/Login.jsx
import { useState } from "react";
import { loginUsuario } from "../services/usuarioService";
import "../styles/login.css";
import { useNavigate } from "react-router-dom";

export const Login = () => {
  const [showPassword, setShowPassword] = useState(false);
  const [formData, setFormData] = useState({
    username: "",
    password: "",
  });

  const [mensaje, setMensaje] = useState(null); // Para mostrar errores o confirmación
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
  e.preventDefault();
  try {
    const resultado = await loginUsuario(formData.username, formData.password);
    console.log("✅ Sesión iniciada:", resultado);

    // Guarda en localStorage si deseas mantener sesión
    localStorage.setItem("usuario", JSON.stringify(resultado));

    // Redirige según el rol
    if (resultado.rol === "ADMIN") {
      navigate("/dashboard");
    } else if (resultado.rol === "VOTANTE") {
      navigate("/home"); // o crea una ruta específica como /votar
    } else {
      setMensaje("Rol no autorizado");
    }
  } catch (error) {
    setMensaje(error);
  }
};

  const handleInputChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  return (
    <div className="login-container">
      <div className="login-card">
        {/* Left Panel */}
        <div className="left-panel">
          <div className="brand-content">
            <div className="brand-logo">VOTE NOW</div>
          </div>
          <div className="brand-message">
            <h2 className="brand-title">Sistema de Votación</h2>
            <p className="brand-subtitle">Vota seguro</p>
          </div>
        </div>

        {/* Right Panel */}
        <div className="right-panel">
          <div className="header">
            <h1 className="title">Bienvenido a VOTE NOW!</h1>
            <p className="subtitle">Ingresa tus datos para iniciar sesión</p>
          </div>

          {/* Login Form */}
          <form onSubmit={handleSubmit} className="form">
            <div className="form-group">
              <label htmlFor="username" className="label">Username</label>
              <input
                id="username"
                name="username"
                type="text"
                required
                value={formData.username}
                onChange={handleInputChange}
                className="input"
                placeholder="Enter your username"
              />
            </div>

            <div className="form-group">
              <label htmlFor="password" className="label">Password</label>
              <div className="password-container">
                <input
                  id="password"
                  name="password"
                  type={showPassword ? "text" : "password"}
                  required
                  value={formData.password}
                  onChange={handleInputChange}
                  className="input"
                  placeholder="Enter your password"
                />
                <button
                  type="button"
                  onClick={() => setShowPassword(!showPassword)}
                  className="password-toggle"
                >
                  {/* SVG íconos */}
                </button>
              </div>
            </div>

            {mensaje && <p className="login-message">{mensaje}</p>}

            <button type="submit" className="submit-button">Sign In</button>
          </form>
        </div>
      </div>
    </div>
  );
};
