package com.sistema.voting.repository;

import com.sistema.voting.model.Opcion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OpcionRepository extends JpaRepository<Opcion, Long> {
    List<Opcion> findByVotacionId(Long votacionId);
}