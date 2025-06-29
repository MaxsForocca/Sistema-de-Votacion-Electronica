package com.sistema.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import org.springframework.ui.ModelMap;

import com.sistema.auth.repository.UsuarioRepository;
import com.sistema.auth.dto.UsuarioDTO;
import com.sistema.auth.dto.LoginDTO;
import org.modelmapper.ModelMapper;
import com.sistema.auth.model.Usuario;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    
    private final ModelMapper modelMapper;
    
    private final PasswordEncoder passwordEncoder; // Para codificar contraseñas

    /* Constructor */
    public UsuarioService(UsuarioRepository usuarioRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder; // Inicializar el codificador de contraseñas
    }

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * @param dto Datos del usuario a registrar.
     */
    public void registrar(UsuarioDTO dto) {
        try {
            Usuario usuario = modelMapper.map(dto, Usuario.class);
            usuario.setPassword(passwordEncoder.encode(dto.getPassword())); // Codificar la contraseña
            usuarioRepository.save(usuario);
            /*
             * Usuario guardado = usuarioRepository.save(usuario);

                return modelMapper.map(guardado, UsuarioDTO.class);// retorna el usuario guardado DTO
             */

        } catch (Exception e) {
            throw new RuntimeException("Error al registrar el usuario: " + e.getMessage());
        }
    }

    public boolean autenticar(LoginDTO dto) {
        try {
            /*
            return usuarioRepository
                .findByUsernameAndPassword(dto.getUsername(), dto.getPassword())
                .isPresent();
            */
            return usuarioRepository.findByUsername(dto.getUsername())
                .map(usuario -> passwordEncoder.matches(dto.getPassword(), usuario.getPassword())).orElse(false);
        } catch (Exception e) {
            throw new RuntimeException("Error al iniciar sesión: " + e.getMessage());
        }
    }
}
