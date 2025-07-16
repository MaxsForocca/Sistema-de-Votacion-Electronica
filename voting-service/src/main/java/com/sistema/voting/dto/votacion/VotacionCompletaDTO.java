package com.sistema.voting.dto.votacion;

import lombok.Data;
import java.util.List;

@Data
public class VotacionCompletaDTO {
    private String titulo;
    private String descripcion;
    private Boolean activa;
    private Long categoriaId;
    private List<PreguntaConOpcionesDTO> preguntas;
}
