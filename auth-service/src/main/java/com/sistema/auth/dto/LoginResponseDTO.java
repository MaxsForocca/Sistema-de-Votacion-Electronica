// src/main/java/com/sistema/auth/dto/LoginResponseDTO.java
package com.sistema.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
    private Long id;
    private String username;
    private String rol;
    private String mensaje;
}
