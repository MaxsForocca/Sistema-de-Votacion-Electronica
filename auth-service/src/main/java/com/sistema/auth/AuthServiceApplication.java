package com.sistema.auth;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.sistema.auth.model.Usuario;
import com.sistema.auth.model.Departamento;
import com.sistema.auth.model.Rol;
import com.sistema.auth.repository.UsuarioRepository;
import com.sistema.auth.repository.DepartamentoRepository;
import com.sistema.auth.repository.RolRepository;

@SpringBootApplication
public class AuthServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner init(UsuarioRepository usuarioRepository, PasswordEncoder encoder, RolRepository rolRepository, DepartamentoRepository departamentoRepository) {
        return args -> {
            if(usuarioRepository.findByUsername("admin").isEmpty()){
                // Buscar rol ADMIN sino crearlo
                // crear DEPARTAMENTO POR DEFECTO
                Departamento departamento = departamentoRepository.findByNombre("DEPT")
                                .orElse(null);
                if(departamento == null){
                    departamento = new Departamento();
                    departamento.setNombre("DEPT");
                    departamento = departamentoRepository.save(departamento);  
                }
                // Crear roles por defecto
                // Si no existen los roles ADMIN y VOTANTE, crearlos
                Rol rolAdmin = rolRepository.findByRol("ADMIN")
                                .orElse(null);
                if(rolAdmin == null){
                    rolAdmin = new Rol();
                    rolAdmin.setRol("ADMIN");
                    rolAdmin = rolRepository.save(rolAdmin);  
                } 
                Rol rolVotante = rolRepository.findByRol("VOTANTE")
                                .orElse(null);
                if(rolVotante == null){
                    rolVotante = new Rol();
                    rolVotante.setRol("VOTANTE");
                    rolVotante = rolRepository.save(rolVotante);  
                } 
                // Usuario admin por defecto    
                Usuario admin = new Usuario();
                admin.setUsername("admin");
                admin.setPassword(encoder.encode("admin"));
                

                admin.setRol(rolAdmin);
                admin.setDepartamento(departamento);
                usuarioRepository.save(admin);
                
                // Usuario por defecto
                Usuario user = new Usuario();
                user.setUsername("user");
                user.setPassword(encoder.encode("user"));

                user.setRol(rolVotante);
                user.setDepartamento(departamento);
                usuarioRepository.save(user);

                System.out.println("✔ Usuario admin creado por defecto.");
            }
        };
    }
}
