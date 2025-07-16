package com.sistema.auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.sistema.auth.dto.UsuarioDTO;
import com.sistema.auth.dto.UsuarioResponseDTO;
import com.sistema.auth.dto.UsuarioVoting;
import com.sistema.auth.model.Rol;
import com.sistema.auth.model.Usuario;
import com.sistema.auth.repository.UsuarioRepository;
import com.sistema.auth.dto.LoginDTO;
import com.sistema.auth.dto.LoginResponseDTO;
//import com.sistema.auth.model.Usuario;
import com.sistema.auth.service.UsuarioService;


import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {
    
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @PostMapping("/login")
public ResponseEntity<?> login(@RequestBody LoginDTO credenciales) {
    try {
        System.out.println("Username recibido: " + credenciales.getUsername());
        System.out.println("Password recibido: " + credenciales.getPassword());

        boolean autenticado = usuarioService.autenticar(credenciales);

        if (!autenticado) {
            return ResponseEntity.status(401).body("Credenciales incorrectas");
        }

        // Obtener el usuario autenticado desde base de datos
        Usuario usuario = usuarioService.buscarPorUsername(credenciales.getUsername());

        if (usuario == null) {
            return ResponseEntity.status(404).body("Usuario no encontrado");
        }

        String rol = (usuario.getRol() != null) ? usuario.getRol().getRol() : "SIN_ROL";

        LoginResponseDTO respuesta = new LoginResponseDTO(
            usuario.getId(),
            usuario.getUsername(),
            rol,
            "Inicio de sesión exitoso"
        );

        return ResponseEntity.ok(respuesta);

    } catch (Exception e) {
        return ResponseEntity.status(500).body("Error al iniciar sesión: " + e.getMessage());
    }
}

    
    // obtener usuario por id para Votacion
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioVoting> obtenerUsuario(@PathVariable Long id) {
        Optional<Usuario> user = usuarioRepository.findById(id);
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        Rol rol = user.get().getRol();

        UsuarioVoting dto = new UsuarioVoting(
            user.get().getId(),
            user.get().getUsername(),
            rol != null ? rol.getRol() : "SIN_ROL"
        );
        return ResponseEntity.ok(dto);
    }

    //// Controller para ADMIN 
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    @GetMapping("/specific/{id}")
    public ResponseEntity<UsuarioResponseDTO> obtenerPorId(@PathVariable Long id) {
        return usuarioService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO> crear(@RequestBody UsuarioDTO dto) {
        return ResponseEntity.ok(usuarioService.crear(dto));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> actualizar(@PathVariable Long id, @RequestBody UsuarioDTO dto) {
        return ResponseEntity.ok(usuarioService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        usuarioService.eliminar(id);
        return ResponseEntity.ok("Usuario eliminado");
    }
}
