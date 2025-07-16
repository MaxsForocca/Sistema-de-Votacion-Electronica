package com.sistema.voting.dto;

import lombok.Data;

@Data
public class VotacionDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private Boolean activa;
    private Long categoriaId;
}
