package com.sistema.voting.controller;

import com.sistema.voting.dto.VotacionDTO;
import com.sistema.voting.dto.votacion.VotacionCompletaDTO;
import com.sistema.voting.service.VotacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/votaciones")
public class VotacionController {

    @Autowired
    private VotacionService votacionService;

    @PostMapping
    public ResponseEntity<VotacionDTO> crear(@RequestBody VotacionDTO dto) {
        return ResponseEntity.ok(votacionService.crearVotacion(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtener(@PathVariable Long id) {
        return votacionService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/completas")
    public ResponseEntity<?> listarCompletas() {
        try {
            return ResponseEntity.ok(votacionService.listarTodasCompletas());
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error inesperado al listar votaciones completas.");
        }
    }

    @PostMapping("/completa")
    public ResponseEntity<VotacionDTO> crearCompleta(@RequestBody VotacionCompletaDTO dto) {
        try {
            VotacionDTO votacionCreada = votacionService.crearVotacionCompleta(dto);
            return ResponseEntity.ok(votacionCreada);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            votacionService.eliminarVotacion(id);
            return ResponseEntity.ok("Votación eliminada correctamente.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody VotacionCompletaDTO dto) {
        try {
            VotacionDTO actualizada = votacionService.actualizarVotacion(id, dto);
            return ResponseEntity.ok(actualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
