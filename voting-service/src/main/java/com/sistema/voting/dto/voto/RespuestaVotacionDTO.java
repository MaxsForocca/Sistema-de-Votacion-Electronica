package com.sistema.voting.dto.voto;

import lombok.Data;
import java.util.List;

@Data
public class RespuestaVotacionDTO {
    private Long usuarioId;
    private Long votacionId;
    private List<RespuestaPreguntaDTO> respuestas;
}
