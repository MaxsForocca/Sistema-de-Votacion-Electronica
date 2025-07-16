package com.sistema.voting.controller;

import com.sistema.voting.dto.UsuarioVoting;
import com.sistema.voting.dto.voto.VotoDTO;
import com.sistema.voting.dto.voto.RespuestaVotacionDTO;
import com.sistema.voting.service.UsuarioAuthClient;
import com.sistema.voting.service.VotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import java.util.List;

@RestController
@RequestMapping("/votos")
public class VotoController {

    @Autowired
    private UsuarioAuthClient usuarioAuthClient;

    @Autowired
    private VotoService votoService;

    @PostMapping
    public ResponseEntity<?> emitirLote(@RequestBody RespuestaVotacionDTO dto) {
        Optional<UsuarioVoting> usuario = usuarioAuthClient.obtenerUsuario(dto.getUsuarioId());
        if (usuario.isEmpty()) {
            return ResponseEntity.status(401).body("Usuario no válido o no existe.");
        }

        try {
            List<VotoDTO> resultados = votoService.emitirVotoLote(dto);
            return ResponseEntity.ok(resultados);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error interno al emitir los votos.");
        }
    }
}
