package com.sistema.voting.dto;

import lombok.Data;

import java.util.List;

@Data
public class OpcionLoteDTO {
    private List<String> textos; // Lista de textos para las opciones
}
