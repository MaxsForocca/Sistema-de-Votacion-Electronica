package com.sistema.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import org.springframework.ui.ModelMap;

import com.sistema.auth.repository.UsuarioRepository;
import com.sistema.auth.dto.RegisterDTO;
import com.sistema.auth.dto.UsuarioDTO;
import com.sistema.auth.dto.LoginDTO;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import com.sistema.auth.model.Rol;
import com.sistema.auth.model.Usuario;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.sistema.auth.repository.RolRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    private final RolRepository rolRepository;
    
    private final ModelMapper modelMapper;
    
    private final PasswordEncoder passwordEncoder; // Para codificar contraseñas

    /* Constructor */
    public UsuarioService(UsuarioRepository usuarioRepository, RolRepository rolRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder; // Inicializar el codificador de contraseñas
    }

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * @param dto Datos del usuario a registrar.
     */
    public void registrar(RegisterDTO dto) {
        try {
            System.out.println("DTO recibido: " + dto);

            if(usuarioRepository.existsByUsername(dto.getUsername())){
                throw new RuntimeException("El usuario " + dto.getUsername() + " ya existe");
            }
            // Asignar el rol VOTANTE por defecto
            Rol rol = rolRepository.findByRol("VOTANTE")
                .orElseThrow(() -> new RuntimeException("Rol no disponible"));
            
            Usuario usuario = modelMapper.map(dto, Usuario.class);
            usuario.setPassword(passwordEncoder.encode(dto.getPassword())); // Codificar la contraseña
            usuario.setRol(rol);
            usuarioRepository.save(usuario);

        } catch (Exception e) {
            throw new RuntimeException("Error al registrar el usuario: " + e.getMessage());
        }
    }

    /*
     * Autentica un usuario con sus credenciales.
     * 
     * @param dto Datos de inicio de sesión.
     * @return true si las credenciales son válidas, false en caso contrario.
     */
    public boolean autenticar(LoginDTO dto) {
        try {
            return usuarioRepository.findByUsername(dto.getUsername())
                .map(usuario -> passwordEncoder.matches(dto.getPassword(), usuario.getPassword())).orElse(false);
        } catch (Exception e) {
            throw new RuntimeException("Error al iniciar sesión: " + e.getMessage());
        }
    }

    /* SERVICES PARA ADMIN (CRUD) */
    public Optional<UsuarioDTO> obtenerPorId(Long id) {
        return usuarioRepository.findById(id)
            .map(usuario -> modelMapper.map(usuario, UsuarioDTO.class));
    }

    public List<UsuarioDTO> listarTodos() {
        return usuarioRepository.findAll().stream()
            .map(usuario -> modelMapper.map(usuario, UsuarioDTO.class))
            .collect(Collectors.toList());
    }

    public UsuarioDTO crear(UsuarioDTO dto) {
        if (usuarioRepository.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("Usuario ya existe");
        }

        Rol rol = rolRepository.findById(dto.getIdRol())
            .orElseThrow(() -> new RuntimeException("Rol no válido"));

        Usuario usuario = modelMapper.map(dto, Usuario.class);
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        usuario.setRol(rol);

        Usuario guardado = usuarioRepository.save(usuario);
        return modelMapper.map(guardado, UsuarioDTO.class);
    }
    
    public UsuarioDTO actualizar(Long id, UsuarioDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setUsername(dto.getUsername());

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        if (dto.getIdRol() != null) {
            Rol rol = rolRepository.findById(dto.getIdRol())
                .orElseThrow(() -> new RuntimeException("Rol no válido"));
            usuario.setRol(rol);
        }

        Usuario actualizado = usuarioRepository.save(usuario);
        return modelMapper.map(actualizado, UsuarioDTO.class);
    }

    public void eliminar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado");
        }
        usuarioRepository.deleteById(id);
    }
}
