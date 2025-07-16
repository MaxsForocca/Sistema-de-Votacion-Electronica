package com.sistema.voting.dto.voto;

import lombok.Data;

@Data
public class VotoDTO {
    private Long id;
    private Long usuarioId;
    private Long preguntaId;
    private Long opcionId;
}
