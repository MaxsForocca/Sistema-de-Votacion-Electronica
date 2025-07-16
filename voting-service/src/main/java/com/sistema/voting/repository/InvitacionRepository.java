package com.sistema.voting.repository;

import com.sistema.voting.model.Invitacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InvitacionRepository extends JpaRepository<Invitacion, Long> {

    @Query("SELECT i.votacion FROM Invitacion i WHERE i.usuarioId = :usuarioId AND i.votacion.activa = true")
    List<com.sistema.voting.model.Votacion> findVotacionesInvitadasPorUsuario(Long usuarioId);

    boolean existsByUsuarioIdAndVotacion_Id(Long usuarioId, Long votacionId);
}
