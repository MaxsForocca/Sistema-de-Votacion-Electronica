package com.sistema.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sistema.auth.model.Departamento;

public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {
    Optional<Departamento> findById(Long id);
    Optional<Departamento> findByNombre(String nombre);
    boolean existsByNombre(String nombre);
    boolean existsById(Long id);
}
