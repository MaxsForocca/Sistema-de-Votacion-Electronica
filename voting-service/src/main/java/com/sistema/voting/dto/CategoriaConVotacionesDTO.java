package com.sistema.voting.dto;

import lombok.Data;

import java.util.List;

@Data
public class CategoriaConVotacionesDTO {
    private Long id;
    private String nombre;
    private List<VotacionDTO> votaciones;
}
