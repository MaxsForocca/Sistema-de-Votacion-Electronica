package com.sistema.voting.repository;

import com.sistema.voting.model.Opcion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OpcionRepository extends JpaRepository<Opcion, Long> {
}
