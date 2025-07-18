package com.sistema.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    private String username;
    private String password;
    private Long idRol; // Representa o papel do usurio (VOTANTE, ADMIN, etc.)
    private Long idDepartamento; // Representa o departamento ao que pertence o usurio
}
