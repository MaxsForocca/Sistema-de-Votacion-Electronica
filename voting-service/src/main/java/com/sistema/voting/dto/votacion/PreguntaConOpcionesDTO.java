package com.sistema.voting.dto.votacion;

import lombok.Data;
import java.util.List;

@Data
public class PreguntaConOpcionesDTO {
    private String texto;
    private List<String> opciones;
}
