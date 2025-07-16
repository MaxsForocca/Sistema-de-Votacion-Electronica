/* Transporte de datos entre capas para usuario */

package com.sistema.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDTO {
    private String username;
    private String password;
    //private Long idDepartamento; // Representa el departamento al que pertenece el usuario
}
