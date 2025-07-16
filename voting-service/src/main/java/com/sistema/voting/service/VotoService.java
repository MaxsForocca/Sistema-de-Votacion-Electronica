package com.sistema.voting.service;

import com.sistema.voting.dto.voto.RespuestaPreguntaDTO;
import com.sistema.voting.dto.voto.RespuestaVotacionDTO;
import com.sistema.voting.dto.voto.VotoDTO;
import com.sistema.voting.model.Opcion;
import com.sistema.voting.model.Votacion;
import com.sistema.voting.model.Voto;
import com.sistema.voting.repository.OpcionRepository;
import com.sistema.voting.repository.VotacionRepository;
import com.sistema.voting.repository.VotoRepository;
import com.sistema.voting.repository.InvitacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    private VotacionRepository votacionRepository;


    public List<VotoDTO> emitirVotoLote(RespuestaVotacionDTO dto) {
        Long usuarioId = dto.getUsuarioId();
        Long votacionId = dto.getVotacionId();

        // 1. Validar invitación
        boolean invitado = invitacionRepository.existsByUsuarioIdAndVotacion_Id(usuarioId, votacionId);
        if (!invitado) {
            auditoriaService.registrar("VOTO_RECHAZADO", "Usuario no invitado", usuarioId);
            throw new IllegalArgumentException("El usuario no está invitado a esta votación.");
        }

        // 2. Validar estado de la votación
        Votacion votacion = votacionRepository.findById(votacionId)
                .orElseThrow(() -> new IllegalArgumentException("Votación no encontrada."));

        if (!votacion.getActiva()) {
            auditoriaService.registrar("VOTO_RECHAZADO", "Votación inactiva", usuarioId);
            throw new IllegalArgumentException("La votación no está activa.");
        }

        List<VotoDTO> resultados = new ArrayList<>();

        for (RespuestaPreguntaDTO respuesta : dto.getRespuestas()) {
            Long preguntaId = respuesta.getPreguntaId();
            Long opcionId = respuesta.getOpcionId();

            // 3. Validar que opción pertenezca a la pregunta
            Opcion opcion = opcionRepository.findById(opcionId)
                    .orElseThrow(() -> new IllegalArgumentException("Opción no encontrada: " + opcionId));
            if (!opcion.getPregunta().getId().equals(preguntaId)) {
                throw new IllegalArgumentException("La opción " + opcionId + " no pertenece a la pregunta " + preguntaId);
            }

            // 4. Validar que pregunta pertenece a la votación
            if (!opcion.getPregunta().getVotacion().getId().equals(votacionId)) {
                throw new IllegalArgumentException("La pregunta no pertenece a la votación.");
            }

            // 5. Validar que no haya votado ya por esa pregunta
            if (votoRepository.existsByUsuarioIdAndPregunta_Id(usuarioId, preguntaId)) {
                throw new IllegalArgumentException("Ya votaste en la pregunta ID: " + preguntaId);
            }

            // 6. Registrar voto
            Voto voto = new Voto();
            voto.setUsuarioId(usuarioId);
            voto.setPregunta(opcion.getPregunta());
            voto.setOpcion(opcion);

            Voto guardado = votoRepository.save(voto);
            auditoriaService.registrar("VOTO_EXITOSO", "Voto registrado", usuarioId);

            VotoDTO votoDTO = new VotoDTO();
            votoDTO.setId(guardado.getId());
            votoDTO.setUsuarioId(usuarioId);
            votoDTO.setPreguntaId(preguntaId);
            votoDTO.setOpcionId(opcionId);

            resultados.add(votoDTO);
        }

        return resultados;
    }
}
