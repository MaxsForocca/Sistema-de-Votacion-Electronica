package com.sistema.voting.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "invitaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long usuarioId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "votacion_id")
    private Votacion votacion;
}
