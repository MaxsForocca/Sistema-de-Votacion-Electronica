package com.sistema.auth.service;

import org.springframework.stereotype.Service;

import com.sistema.auth.repository.UsuarioRepository;
import com.sistema.auth.dto.RegisterDTO;
import com.sistema.auth.dto.UsuarioDTO;
import com.sistema.auth.dto.UsuarioResponseDTO;
import com.sistema.auth.dto.LoginDTO;
import com.sistema.auth.model.Departamento;
import com.sistema.auth.model.Rol;
import com.sistema.auth.model.Usuario;
import com.sistema.auth.repository.RolRepository;
import com.sistema.auth.repository.DepartamentoRepository;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final DepartamentoRepository departamentoRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          RolRepository rolRepository,
                          DepartamentoRepository departamentoRepository,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.departamentoRepository = departamentoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registrar(RegisterDTO dto) {
        if (usuarioRepository.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("El usuario " + dto.getUsername() + " ya existe");
        }

        Rol rol = rolRepository.findByRol("VOTANTE")
                .orElseThrow(() -> new RuntimeException("Rol no disponible"));

        Usuario usuario = new Usuario();
        usuario.setUsername(dto.getUsername());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        usuario.setRol(rol);

        usuarioRepository.save(usuario);
    }

    public boolean autenticar(LoginDTO dto) {
        return usuarioRepository.findByUsername(dto.getUsername())
                .map(usuario -> passwordEncoder.matches(dto.getPassword(), usuario.getPassword()))
                .orElse(false);
    }

    public Optional<UsuarioResponseDTO> obtenerPorId(Long id) {
        return usuarioRepository.findById(id).map(this::convertirAUsuarioResponseDTO);
    }

    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(this::convertirAUsuarioResponseDTO)
                .collect(Collectors.toList());
    }

    public UsuarioDTO crear(UsuarioDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setUsername(dto.getUsername());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));

        Rol rol = rolRepository.findById(dto.getIdRol())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        usuario.setRol(rol);

        Departamento departamento = departamentoRepository.findById(dto.getIdDepartamento())
                .orElseThrow(() -> new RuntimeException("Departamento no encontrado"));
        usuario.setDepartamento(departamento);

        usuarioRepository.save(usuario);

        return convertirAUsuarioDTO(usuario);
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

        if (dto.getIdDepartamento() != null) {
            Departamento departamento = departamentoRepository.findById(dto.getIdDepartamento())
                    .orElseThrow(() -> new RuntimeException("Departamento no válido"));
            usuario.setDepartamento(departamento);
        }

        Usuario actualizado = usuarioRepository.save(usuario);
        return convertirAUsuarioDTO(actualizado);
    }

    public void eliminar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado");
        }
        usuarioRepository.deleteById(id);
    }

    // Utilidad para convertir Entity → DTO
    private UsuarioDTO convertirAUsuarioDTO(Usuario usuario) {
        return new UsuarioDTO(
                usuario.getUsername(),
                usuario.getPassword(), // No devolvemos password por seguridad
                usuario.getRol() != null ? usuario.getRol().getId() : null,
                usuario.getDepartamento() != null ? usuario.getDepartamento().getId() : null
        );
    }

    // Convierte Usuario → UsuarioResponseDTO
    private UsuarioResponseDTO convertirAUsuarioResponseDTO(Usuario usuario) {
        return new UsuarioResponseDTO(
            usuario.getId(),
            usuario.getUsername(),
            usuario.getPassword(),
            usuario.getRol() != null ? usuario.getRol().getId() : null,
            usuario.getDepartamento() != null ? usuario.getDepartamento().getId() : null
        );
    }

        // UsuarioService.java
    public Usuario buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username).orElse(null);
    }

}

