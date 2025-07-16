package com.sistema.voting.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AuditoriaVotoDTO {
    private Long id;
    private Long usuarioId;
    private Long votacionId;
    private Long opcionId;
    private Boolean exito;
    private LocalDateTime fecha;
    private String ip;
}
