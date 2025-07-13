package com.sistema.voting.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.GenerationType;
import java.util.List;

@Entity
@Table(name = "votaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Votacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String titulo;

    private String descripcion;

    
    @NotBlank
    private Boolean activa;

    @OneToMany(mappedBy = "votacion", cascade = CascadeType.ALL)
    private List<Voto> votos;
}
