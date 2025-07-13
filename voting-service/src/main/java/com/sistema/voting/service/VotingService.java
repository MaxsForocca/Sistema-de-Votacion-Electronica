package com.sistema.voting.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sistema.voting.dto.UsuarioVoting;
import com.sistema.voting.model.Votacion;
import com.sistema.voting.model.Voto;
import com.sistema.voting.repository.VotacionRepository;
import com.sistema.voting.repository.VotoRepository;

@Service
@Transactional
public class VotingService {
    private final VotacionRepository votacionRepository;
    private final VotoRepository votoRepository;
    private final UsuarioAuthClient usuarioAuthClient;

    public VotingService(VotacionRepository votacionRepository, VotoRepository votoRepository, UsuarioAuthClient usuarioAuthClient) {
        this.votacionRepository = votacionRepository;
        this.votoRepository = votoRepository;
        this.usuarioAuthClient = usuarioAuthClient;
    }

    public Votacion crearVotacion(Votacion votacion) {
        return votacionRepository.save(votacion);
    }

    public Optional<Votacion> obtenerVotacion(Long id) {
        return votacionRepository.findById(id);
    }

    public List<Votacion> listarVotaciones() {
        return votacionRepository.findAll();
    }

    public Voto emitirVoto(Voto voto) {
        Optional<UsuarioVoting> usuario = usuarioAuthClient.obtenerUsuario(voto.getUsuarioId());

        if (usuario.isEmpty()) {
            throw new RuntimeException("Usuario no válido");
        }
        // Aquí podrías validar que no haya votado ya, etc.
        return votoRepository.save(voto);
    }

    public List<Voto> listarVotosPorVotacion(Long votacionId) {
        return votoRepository.findByVotacionId(votacionId);
    }
}
