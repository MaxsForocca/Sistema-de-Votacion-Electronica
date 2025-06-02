package com.sistema.votacion.model;

import jakarta.persistence.Entity;

@Entity
public class Voto {
    private Long id;
    private Long idUsuario;
    private Long idVotacion;
    private String opcion;
}
