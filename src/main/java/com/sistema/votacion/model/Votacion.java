package com.sistema.votacion.model;

import jakarta.persistence.Entity;

@Entity
public class Votacion {
    private Long id;
    private String pregunta;
    private Boolean activa;
}
