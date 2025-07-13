package com.sistema.voting.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sistema.voting.model.Votacion;
import com.sistema.voting.model.Voto;
import com.sistema.voting.service.VotingService;
import java.util.List;

@RestController
@RequestMapping("/votaciones")
public class VotingController {
    private final VotingService votingService;

    public VotingController(VotingService votingService) {
        this.votingService = votingService;
    }

    @PostMapping
    public ResponseEntity<Votacion> crearVotacion(@RequestBody Votacion votacion) {
        Votacion created = votingService.crearVotacion(votacion);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Votacion> obtenerVotacion(@PathVariable Long id) {
        return votingService.obtenerVotacion(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Votacion> listarVotaciones() {
        return votingService.listarVotaciones();
    }

    @PostMapping("/{id}/votos")
    public ResponseEntity<Voto> emitirVoto(@PathVariable Long id, @RequestBody Voto voto) {
        voto.setVotacion(new Votacion());
        voto.getVotacion().setId(id);
        Voto saved = votingService.emitirVoto(voto);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{id}/votos")
    public List<Voto> listarVotos(@PathVariable Long id) {
        return votingService.listarVotosPorVotacion(id);
    }
}
