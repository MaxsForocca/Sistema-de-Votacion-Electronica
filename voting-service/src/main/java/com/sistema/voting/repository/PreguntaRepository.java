package com.sistema.voting.repository;

import com.sistema.voting.model.Pregunta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PreguntaRepository extends JpaRepository<Pregunta, Long> {
    List<Pregunta> findByVotacionId(Long votacionId);
}
