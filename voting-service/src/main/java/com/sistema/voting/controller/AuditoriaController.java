package com.sistema.voting.controller;

import com.sistema.voting.model.Auditoria;
import com.sistema.voting.service.AuditoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auditorias")
public class AuditoriaController {

    private final AuditoriaService auditoriaService;

    @Autowired
    public AuditoriaController(AuditoriaService auditoriaService) {
        this.auditoriaService = auditoriaService;
    }

    /**
     * Endpoint para obtener todas las auditorías registradas en el sistema.
     *
     * @return Lista de objetos Auditoria
     */
    @GetMapping
    public ResponseEntity<?> listarAuditorias() {
        try {
            List<Auditoria> auditorias = auditoriaService.obtenerTodas();
            return ResponseEntity.ok(auditorias);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
