package com.sistema.voting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.sistema.voting.model.Votacion;
import com.sistema.voting.model.Voto;

import java.util.List;
@Repository
public interface VotoRepository extends JpaRepository<Voto, Long> {

    List<Voto> findByVotacionId(Long votacionId);
}