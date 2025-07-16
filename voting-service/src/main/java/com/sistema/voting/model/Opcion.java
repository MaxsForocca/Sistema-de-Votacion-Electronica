package com.sistema.voting.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "opciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Opcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String texto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "votacion_id")
    private Votacion votacion;

    @OneToMany(mappedBy = "opcion", cascade = CascadeType.ALL)
    private List<Voto> votos;
}
