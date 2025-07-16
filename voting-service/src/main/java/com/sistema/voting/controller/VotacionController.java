package com.sistema.voting.controller;

import com.sistema.voting.dto.VotacionDTO;
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
    public ResponseEntity<VotacionDTO> obtener(@PathVariable Long id) {
        return votacionService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<?> listar() {
        try {
            return ResponseEntity.ok(votacionService.listarTodas());
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error inesperado al listar votaciones.");
        }
    }
}
