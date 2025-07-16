package com.sistema.voting.dto;

import lombok.Data;
import java.util.List;

@Data
public class PreguntaDTO {
    private Long id;
    private String texto;
    private Long votacionId;
    private List<OpcionDTO> opciones;
}
