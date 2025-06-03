package com.sistema.votacion.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Voto {
    @Id
    private Long id;
    private Long idUsuario;
    private Long idVotacion;
    private String opcion;
}
