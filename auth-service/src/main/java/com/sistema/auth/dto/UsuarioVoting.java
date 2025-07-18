package com.sistema.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO para representar o voto de um usurio (No T)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioVoting {
    private Long id;
    private String username;
    private String rol; // Representa o papel do usurio (VOTANTE, ADMIN, etc.)
}
