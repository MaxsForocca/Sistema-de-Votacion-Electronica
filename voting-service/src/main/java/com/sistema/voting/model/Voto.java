package com.sistema.voting.model;

//import jakarta.annotation.Generated;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "votos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Voto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private Long usuarioId;  // Puede ser el username o id externo, referenciando al usuario del auth-service

    @NotBlank
    private String opcion; // Por ejemplo, candidato o respuesta a la votación

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "votacion_id")
    private Votacion votacion;
}
