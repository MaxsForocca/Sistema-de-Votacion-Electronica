package com.sistema.voting.controller;

import com.sistema.voting.dto.InvitacionDTO;
import com.sistema.voting.dto.VotacionDTO;
import com.sistema.voting.service.InvitacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/invitaciones")
public class InvitacionController {

    private final InvitacionService invitacionService;

    public InvitacionController(InvitacionService invitacionService) {
        this.invitacionService = invitacionService;
    }

    @PostMapping
    public ResponseEntity<InvitacionDTO> crear(@RequestBody InvitacionDTO dto) {
        return ResponseEntity.ok(invitacionService.crearInvitacion(dto));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<VotacionDTO>> listarVotacionesPorUsuario(@PathVariable Long usuarioId) {
        List<VotacionDTO> lista = invitacionService.obtenerVotacionesInvitadas(usuarioId);
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }
}
