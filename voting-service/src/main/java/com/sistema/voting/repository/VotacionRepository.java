package com.sistema.voting.repository;

import com.sistema.voting.model.Votacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VotacionRepository extends JpaRepository<Votacion, Long> {
}
