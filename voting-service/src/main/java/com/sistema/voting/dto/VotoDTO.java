/* Transporte de datos entre capas para Voto */
package com.sistema.voting.dto;

import com.sistema.voting.model.Votacion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VotoDTO {
    private Long usuario;
    private Votacion votacion;
    private String opcion; 
}
