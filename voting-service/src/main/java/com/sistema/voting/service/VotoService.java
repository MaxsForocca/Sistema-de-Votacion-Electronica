package com.sistema.voting.service;

import com.sistema.voting.dto.VotoDTO;
import com.sistema.voting.model.Opcion;
import com.sistema.voting.model.Votacion;
import com.sistema.voting.model.Voto;
import com.sistema.voting.repository.OpcionRepository;
import com.sistema.voting.repository.VotoRepository;
import com.sistema.voting.repository.InvitacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VotoService {

    @Autowired
    private VotoRepository votoRepository;

    @Autowired
    private OpcionRepository opcionRepository;

    @Autowired
    private InvitacionRepository invitacionRepository;
    
    @Autowired
    private AuditoriaService auditoriaService;

    public VotoDTO emitirVoto(VotoDTO dto) {
        // 1. Validar que la opción exista
        Opcion opcion = opcionRepository.findById(dto.getOpcionId())
                .orElseThrow(() -> new RuntimeException("Opción no encontrada"));

        Votacion votacion = opcion.getVotacion();

        // 2. Validar que la votación esté activa
        if (!votacion.getActiva()) {
            auditoriaService.registrar("VOTO_RECHAZADO", "La votación no está activa", dto.getUsuarioId());
            throw new RuntimeException("La votación no está activa.");
        }

        // 3. Validar que el usuario esté invitado a esa votación
        boolean estaInvitado = invitacionRepository
            .existsByUsuarioIdAndVotacion_Id(dto.getUsuarioId(), votacion.getId());

        if (!estaInvitado) {
            auditoriaService.registrar("VOTO_RECHAZADO", "Usuario no invitado", dto.getUsuarioId());
            throw new RuntimeException("El usuario no está invitado a esta votación.");
        }

        // 4. Validar que no haya votado antes en esta votación
        boolean yaVoto = votoRepository.existsByUsuarioIdAndOpcion_Votacion_Id(
                dto.getUsuarioId(), votacion.getId());

        if (yaVoto) {
            auditoriaService.registrar("VOTO_RECHAZADO", "Usuario ya ha votado", dto.getUsuarioId());
            throw new RuntimeException("El usuario ya ha votado en esta votación.");
        }

        // 5. Registrar el voto
        Voto voto = new Voto();
        voto.setUsuarioId(dto.getUsuarioId());
        voto.setOpcion(opcion);

        Voto guardado = votoRepository.save(voto);
        auditoriaService.registrar("VOTO_EXITOSO", "Voto registrado correctamente", dto.getUsuarioId());

        dto.setId(guardado.getId());
        return dto;
    }

    public Optional<VotoDTO> obtenerPorId(Long id) {
        return votoRepository.findById(id)
                .map(v -> {
                    VotoDTO dto = new VotoDTO();
                    dto.setId(v.getId());
                    dto.setUsuarioId(v.getUsuarioId());
                    dto.setOpcionId(v.getOpcion() != null ? v.getOpcion().getId() : null);
                    return dto;
                });
    }
}
