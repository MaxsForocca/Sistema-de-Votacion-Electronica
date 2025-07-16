package com.sistema.voting.repository;

import com.sistema.voting.model.Voto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VotoRepository extends JpaRepository<Voto, Long> {
    boolean existsByUsuarioIdAndPregunta_Id(Long usuarioId, Long preguntaId);
}
