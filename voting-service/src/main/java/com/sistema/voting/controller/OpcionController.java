package com.sistema.voting.controller;

import com.sistema.voting.dto.OpcionDTO;
import com.sistema.voting.dto.OpcionLoteDTO;
import com.sistema.voting.service.OpcionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/opciones")
public class OpcionController {

    @Autowired
    private OpcionService opcionService;

    @PostMapping
    public ResponseEntity<OpcionDTO> crear(@RequestBody OpcionDTO dto) {
        return ResponseEntity.ok(opcionService.crearOpcion(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OpcionDTO> obtener(@PathVariable Long id) {
        return opcionService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/votacion/{votacionId}")
    public ResponseEntity<?> listarPorVotacion(@PathVariable Long votacionId) {
        try {
            return ResponseEntity.ok(opcionService.obtenerPorVotacionId(votacionId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error inesperado al listar opciones.");
        }
    }

    @PostMapping("/votacion/{votacionId}")
    public ResponseEntity<?> agregarOpciones(
            @PathVariable Long votacionId,
            @RequestBody OpcionLoteDTO dto) {

        if (dto.getTextos() == null || dto.getTextos().isEmpty()) {
            return ResponseEntity.badRequest().body("La lista de opciones está vacía.");
        }

        try {
            return ResponseEntity.ok(opcionService.agregarOpcionesALaVotacion(votacionId, dto.getTextos()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
