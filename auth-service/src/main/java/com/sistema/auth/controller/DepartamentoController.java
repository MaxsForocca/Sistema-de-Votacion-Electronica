package com.sistema.auth.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sistema.auth.dto.DepartamentoDTO;
import com.sistema.auth.service.DepartamentoService;

@RestController
@RequestMapping("/departamentos")
public class DepartamentoController {
    @Autowired
    private DepartamentoService departamentoService;

    public DepartamentoController(DepartamentoService departamentoService) {
        this.departamentoService = departamentoService;
    }

    @PostMapping
    public ResponseEntity<DepartamentoDTO> crear(@RequestBody DepartamentoDTO dto) {
        return ResponseEntity.ok(departamentoService.crear(dto));
    }

    @GetMapping
    public ResponseEntity<List<DepartamentoDTO>> listar() {
        return ResponseEntity.ok(departamentoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartamentoDTO> obtener(@PathVariable Long id) {
        return departamentoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartamentoDTO> actualizar(@PathVariable Long id, @RequestBody DepartamentoDTO dto) {
        return ResponseEntity.ok(departamentoService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        departamentoService.eliminar(id);
        return ResponseEntity.ok("Departamento eliminado");
    }
}
