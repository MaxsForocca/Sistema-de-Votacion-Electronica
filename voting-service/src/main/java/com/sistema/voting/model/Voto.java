package com.sistema.voting.model;

//import jakarta.annotation.Generated;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;

@Entity
@Data
public class Voto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // Muchos votos pueden estar relacionados a un mismo usuario
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Long usuario;

    // Muchos votos pueden estar relacionados a una misma votación
    @ManyToOne
    @JoinColumn(name = "votacion_id", nullable = false)
    private Votacion votacion;

    @NotBlank(message = "La opción de voto no puede estar vacía")
    private String opcion;
}
