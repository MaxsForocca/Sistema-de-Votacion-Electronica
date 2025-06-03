package com.sistema.votacion.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.sistema.votacion.repository.UsuarioRepository;
import com.sistema.votacion.dto.UsuarioDTO;
import org.modelmapper.ModelMapper;
import com.sistema.votacion.model.Usuario;

@Service
public class UsuarioService {

    @Autowired
    private final UsuarioRepository usuarioRepository;
    @Autowired
    private final ModelMapper modelMapper;

    /* Constructor */
    public UsuarioService(UsuarioRepository usuarioRepository, ModelMapper modelMapper) {
        this.usuarioRepository = usuarioRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * @param dto Datos del usuario a registrar.
     */
    public void registrar(UsuarioDTO dto) {
        try {
            Usuario usuario = modelMapper.map(dto, Usuario.class);
            usuarioRepository.save(usuario);
        } catch (Exception e) {
            throw new RuntimeException("Error al registrar el usuario: " + e.getMessage());
        }
    }
}
