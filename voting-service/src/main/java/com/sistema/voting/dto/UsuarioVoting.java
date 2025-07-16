package com.sistema.voting.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioVoting {
    private String username;
    private String password;
    private String rol; // Representa o papel do usurio (VOTANTE, ADMIN, etc.)
}
