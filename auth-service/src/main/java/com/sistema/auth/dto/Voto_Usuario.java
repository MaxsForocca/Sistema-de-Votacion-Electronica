package com.sistema.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


// DTO para representar o voto de um usurio (No T)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Voto_Usuario {
    private String id_usuario;
    private String id_voto;
    private String voto;     
}
