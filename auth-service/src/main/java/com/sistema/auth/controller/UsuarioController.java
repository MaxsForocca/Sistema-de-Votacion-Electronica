package com.sistema.auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.sistema.auth.dto.UsuarioDTO;
import com.sistema.auth.dto.UsuarioVoting;
import com.sistema.auth.model.Rol;
import com.sistema.auth.model.Usuario;
import com.sistema.auth.repository.UsuarioRepository;
import com.sistema.auth.dto.LoginDTO;
//import com.sistema.auth.model.Usuario;
import com.sistema.auth.service.UsuarioService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/usuario")
public class UsuarioController {
    
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/register")
    public ResponseEntity<?> registrarUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        try {
            System.out.println("Se ingreso al controller para registrar");
            System.out.println("DTO recibido: " + usuarioDTO);

            //Logica de registro de usuario
            usuarioService.registrar(usuarioDTO);
            return ResponseEntity.ok("Usuario registrado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al registrar el usuario: " + e.getMessage());
        }
    }
    
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO credenciales) {
        try{
            System.out.println("Username recibido: " + credenciales.getUsername());
            System.out.println("Password recibido: " + credenciales.getPassword());

            boolean result = usuarioService.autenticar(credenciales);
            if (!result) {
                // Si las credenciales son incorrectas, se retorna un mensaje de error
                return ResponseEntity.status(401).body("Credenciales incorrectas");
            } else {
                return ResponseEntity.ok("Inicio de sesión exitoso");
            }    
        } catch (Exception e) {
            // Si ocurre un error, se retorna un mensaje de error
            return ResponseEntity.status(401).body("Error al iniciar sesión: " + e.getMessage());
        }
    }  
    

    // obtener usuario por id
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
    
}
