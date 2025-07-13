package com.sistema.auth;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.sistema.auth.model.Usuario;
import com.sistema.auth.model.Rol;
import com.sistema.auth.repository.UsuarioRepository;
import com.sistema.auth.repository.RolRepository;

@SpringBootApplication
public class AuthServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner init(UsuarioRepository usuarioRepository, PasswordEncoder encoder, RolRepository rolRepository){
        return args -> {
            if(usuarioRepository.findByUsername("admin").isEmpty()){
                // Buscar rol ADMIN sino crearlo
                Rol rolAdmin = rolRepository.findByRol("ADMIN")
                                .orElse(null);
                if(rolAdmin == null){
                    rolAdmin = new Rol();
                    rolAdmin.setRol("ADMIN");
                    rolAdmin = rolRepository.save(rolAdmin);  
                }

                Usuario admin = new Usuario();
                admin.setUsername("admin");
                admin.setPassword(encoder.encode("admin"));
                

                admin.setRol(rolAdmin);
                usuarioRepository.save(admin);
                System.out.println("✔ Usuario admin creado por defecto.");
            }
        };
    }
}
