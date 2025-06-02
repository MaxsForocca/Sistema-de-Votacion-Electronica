package com.sistema.votacion.model;

import jakarta.persistence.Entity;

@Entity
public class Usuario {
    private Long id;
    private String nombre;
    private String contrasena;
}
