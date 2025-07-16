package com.sistema.voting.service;

import com.sistema.voting.dto.InvitacionDTO;
import com.sistema.voting.dto.VotacionDTO;
import com.sistema.voting.model.Invitacion;
import com.sistema.voting.model.Votacion;
import com.sistema.voting.repository.InvitacionRepository;
import com.sistema.voting.repository.VotacionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InvitacionService {

    private final InvitacionRepository invitacionRepository;
    private final VotacionRepository votacionRepository;

    public InvitacionService(InvitacionRepository invitacionRepository, VotacionRepository votacionRepository) {
        this.invitacionRepository = invitacionRepository;
        this.votacionRepository = votacionRepository;
    }

    public InvitacionDTO crearInvitacion(InvitacionDTO dto) {
        Optional<Votacion> votacionOpt = votacionRepository.findById(dto.getVotacionId());

        if (votacionOpt.isEmpty()) {
            throw new RuntimeException("Votación no encontrada.");
        }

        Invitacion invitacion = Invitacion.builder()
                .usuarioId(dto.getUsuarioId())
                .votacion(votacionOpt.get())
                .build();

        Invitacion guardada = invitacionRepository.save(invitacion);

        dto.setId(guardada.getId());
        return dto;
    }

    public List<VotacionDTO> obtenerVotacionesInvitadas(Long usuarioId) {
        List<Votacion> votaciones = invitacionRepository.findVotacionesInvitadasPorUsuario(usuarioId);

        return votaciones.stream().map(v -> {
            VotacionDTO dto = new VotacionDTO();
            dto.setId(v.getId());
            dto.setTitulo(v.getTitulo());
            dto.setDescripcion(v.getDescripcion());
            dto.setActiva(v.getActiva());
            return dto;
        }).collect(Collectors.toList());
    }
}
