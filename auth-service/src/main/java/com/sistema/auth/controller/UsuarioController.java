package com.sistema.auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.sistema.auth.dto.UsuarioDTO;
import com.sistema.auth.dto.LoginDTO;
//import com.sistema.auth.model.Usuario;
import com.sistema.auth.service.UsuarioService;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {
    
    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/register")
    public ResponseEntity<?> registrarUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        try {
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
    
}
