package com.sistema.voting.dto.votacion;

import lombok.Data;
import java.util.List;

@Data
public class VotacionDetalleDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private Boolean activa;
    private Long categoriaId;
    private List<PreguntaDetalleDTO> preguntas;
}

