package com.sistema.voting.dto.votacion;

import lombok.Data;
import java.util.List;

@Data
public class PreguntaDetalleDTO {
    private Long preguntaId;
    private String texto;
    private List<OpcionDTO> opciones;
}
