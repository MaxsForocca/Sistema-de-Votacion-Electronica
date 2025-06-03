/* Transporte de datos entre capas para Voto */
package com.sistema.votacion.dto;

import com.sistema.votacion.model.Usuario;
import com.sistema.votacion.model.Votacion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VotoDTO {
    private Usuario usuario;
    private Votacion votacion;
    private String opcion; 
}
