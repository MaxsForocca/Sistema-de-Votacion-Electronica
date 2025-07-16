package com.sistema.voting.service;

import com.sistema.voting.dto.OpcionDTO;
import com.sistema.voting.model.Opcion;
import com.sistema.voting.model.Votacion;
import com.sistema.voting.repository.OpcionRepository;
import com.sistema.voting.repository.VotacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OpcionService {

    @Autowired
    private OpcionRepository opcionRepository;

    @Autowired
    private VotacionRepository votacionRepository;

    @Autowired
    private UsuarioAuthClient usuarioAuthClient;

    public OpcionDTO crearOpcion(OpcionDTO dto) {
        Opcion opcion = new Opcion();
        opcion.setTexto(dto.getTexto());

        if (dto.getVotacionId() != null) {
            Optional<Votacion> votacion = votacionRepository.findById(dto.getVotacionId());
            votacion.ifPresent(opcion::setVotacion);
        }

        Opcion guardada = opcionRepository.save(opcion);

        dto.setId(guardada.getId());
        return dto;
    }

    public Optional<OpcionDTO> obtenerPorId(Long id) {
        return opcionRepository.findById(id)
                .map(o -> {
                    OpcionDTO dto = new OpcionDTO();
                    dto.setId(o.getId());
                    dto.setTexto(o.getTexto());
                    dto.setVotacionId(o.getVotacion() != null ? o.getVotacion().getId() : null);
                    return dto;
                });
    }

    public List<OpcionDTO> obtenerPorVotacionId(Long votacionId) {
        // Verifica que la votación exista
        if (!votacionRepository.existsById(votacionId)) {
            throw new RuntimeException("La votación con ID " + votacionId + " no existe.");
        }

        List<Opcion> opciones = opcionRepository.findByVotacionId(votacionId);

        if (opciones.isEmpty()) {
            throw new RuntimeException("No hay opciones para esta votación.");
        }

        return opciones.stream().map(opcion -> {
            OpcionDTO dto = new OpcionDTO();
            dto.setId(opcion.getId());
            dto.setTexto(opcion.getTexto());
            dto.setVotacionId(votacionId);
            return dto;
        }).collect(Collectors.toList());
    }

    // OpcionService.java
    public List<Opcion> agregarOpcionesALaVotacion(Long votacionId, List<String> textos) {
        Votacion votacion = votacionRepository.findById(votacionId)
            .orElseThrow(() -> new RuntimeException("Votación no encontrada con ID: " + votacionId));

        List<Opcion> opciones = textos.stream().map(texto -> {
            Opcion opcion = new Opcion();
            opcion.setTexto(texto);
            opcion.setVotacion(votacion);
            return opcion;
        }).collect(Collectors.toList());

        return opcionRepository.saveAll(opciones);
    }
}
