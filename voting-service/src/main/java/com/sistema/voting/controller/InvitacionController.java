package com.sistema.voting.controller;

import com.sistema.voting.dto.InvitacionDTO;
import com.sistema.voting.dto.UsuarioVoting;
import com.sistema.voting.dto.VotacionDTO;
import com.sistema.voting.service.InvitacionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.sistema.voting.service.UsuarioAuthClient;
import java.util.Optional;
import java.util.List;

@RestController
@RequestMapping("/invitaciones")
public class InvitacionController {

    @Autowired
    private UsuarioAuthClient usuarioAuthClient;

    private final InvitacionService invitacionService;

    public InvitacionController(InvitacionService invitacionService) {
        this.invitacionService = invitacionService;
    }

    /**
     * Crea una nueva invitación para un usuario a una votación específica.
     *
     * @param dto Objeto InvitacionDTO que contiene el ID del usuario y el ID de la votación.
     * @return ResponseEntity con la invitación creada.
     */
    @PostMapping
    public ResponseEntity<InvitacionDTO> crear(@RequestBody InvitacionDTO dto) {
        return ResponseEntity.ok(invitacionService.crearInvitacion(dto));
    }

    /**
     * Obtiene una lista de votaciones a las que un usuario ha sido invitado.
     *
     * @param usuarioId ID del usuario.
     * @return ResponseEntity con la lista de VotacionDTO si existen, o status 204 si no hay contenido.
     */
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<?> listarVotacionesPorUsuario(@PathVariable Long usuarioId) {
        Optional<UsuarioVoting> usuario = usuarioAuthClient.obtenerUsuario(usuarioId);
        if (usuario.isEmpty()) {
            return ResponseEntity.status(401).body("Usuario no válido o no existe.");
        }

        List<VotacionDTO> lista = invitacionService.obtenerVotacionesInvitadas(usuarioId);
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    /**
     * Elimina una invitación específica por su ID.
     *
     * @param id ID de la invitación a eliminar.
     * @return ResponseEntity con mensaje de éxito o error si la invitación no existe.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            invitacionService.eliminarInvitacion(id);
            return ResponseEntity.ok("Invitación eliminada correctamente.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
