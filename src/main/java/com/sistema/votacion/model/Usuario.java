package com.sistema.votacion.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Usuario {
    @Id
    private Long id;
    private String nombre;
    private String contrasena;
}
