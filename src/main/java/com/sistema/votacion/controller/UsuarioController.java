package com.sistema.votacion.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.sistema.votacion.dto.UsuarioDTO;
import com.sistema.votacion.model.Usuario;
import com.sistema.votacion.service.UsuarioService;

@RestController
@RequestMapping("/api")
public class UsuarioController {
    
    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/register")
    public ResponseEntity<?> registrarUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        //Logica de registro de usuario
        usuarioService.registrar(usuarioDTO);
        return ResponseEntity.ok("Usuario registrado exitosamente");
    }
    
    /* 
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO credenciales) {
        //Logica de inicio de sesion
        
        return ResponseEntity.ok("Inicio de sesión exitoso");
    }  
    */ 
}
