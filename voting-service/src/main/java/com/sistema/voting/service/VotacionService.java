package com.sistema.voting.service;

import com.sistema.voting.dto.VotacionDTO;

import com.sistema.voting.model.Categoria;
import com.sistema.voting.model.Votacion;

import com.sistema.voting.repository.CategoriaRepository;
import com.sistema.voting.repository.VotacionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VotacionService {

    @Autowired
    private VotacionRepository votacionRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    public VotacionDTO crearVotacion(VotacionDTO dto) {
        Votacion votacion = new Votacion();
        votacion.setTitulo(dto.getTitulo());
        votacion.setDescripcion(dto.getDescripcion());
        votacion.setActiva(dto.getActiva());

        // Relacionar con categoría si hay ID
        if (dto.getCategoriaId() != null) {
            Optional<Categoria> categoria = categoriaRepository.findById(dto.getCategoriaId());
            categoria.ifPresent(votacion::setCategoria);
        }

        Votacion guardada = votacionRepository.save(votacion);

        dto.setId(guardada.getId());
        return dto;
    }

    public Optional<VotacionDTO> obtenerPorId(Long id) {
        return votacionRepository.findById(id)
                .map(v -> {
                    VotacionDTO dto = new VotacionDTO();
                    dto.setId(v.getId());
                    dto.setTitulo(v.getTitulo());
                    dto.setDescripcion(v.getDescripcion());
                    dto.setActiva(v.getActiva());
                    dto.setCategoriaId(v.getCategoria() != null ? v.getCategoria().getId() : null);
                    return dto;
                });
    }

    public List<VotacionDTO> listarTodas() {
        List<VotacionDTO> votaciones = votacionRepository.findAll().stream().map(v -> {
            VotacionDTO dto = new VotacionDTO();
            dto.setId(v.getId());
            dto.setTitulo(v.getTitulo());
            dto.setDescripcion(v.getDescripcion());
            dto.setActiva(v.getActiva());
            dto.setCategoriaId(v.getCategoria() != null ? v.getCategoria().getId() : null);
            return dto;
        }).collect(Collectors.toList());

        if (votaciones.isEmpty()) {
            throw new RuntimeException("No hay votaciones disponibles en el sistema.");
        }

        return votaciones;
    }
}
