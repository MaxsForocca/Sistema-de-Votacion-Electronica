package com.sistema.votacion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VotacionDTO {
    private String pregunta;
    private Boolean activa;
}
