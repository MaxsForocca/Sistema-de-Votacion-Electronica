package com.sistema.votacion.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Votacion {
    @Id
    private Long id;
    private String pregunta;
    private Boolean activa;
}
