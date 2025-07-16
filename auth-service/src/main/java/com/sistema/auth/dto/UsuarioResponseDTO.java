package com.sistema.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponseDTO {
    private Long id;
    private String username;
    private String password; // hash incluido
    private Long idRol;
    private Long idDepartamento;
}
