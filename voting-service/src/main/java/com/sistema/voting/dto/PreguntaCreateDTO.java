package com.sistema.voting.dto;

import lombok.Data;
import java.util.List;

@Data
public class PreguntaCreateDTO {
    private String texto;
    private Long votacionId;
    private List<String> opciones;
}
