package com.sistema.auth.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de usuario no puede estar vacío")
    @Column(unique = true)
    private String username;

    @NotBlank(message = "La contraseña no puede estar vacía")
    private String password;

    @NotNull(message = "El rol no puede estar vacío")
    @ManyToOne
    private Rol rol;

    @NotNull(message = "El departamento no puede estar vacío")
    @ManyToOne
    private Departamento departamento;
}
