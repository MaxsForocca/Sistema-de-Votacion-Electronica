package com.sistema.voting.service;

import com.sistema.voting.model.Auditoria;
import com.sistema.voting.repository.AuditoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditoriaService {

    private final AuditoriaRepository auditoriaRepository;

    public AuditoriaService(AuditoriaRepository auditoriaRepository) {
        this.auditoriaRepository = auditoriaRepository;
    }

    public void registrar(String accion, String detalle, Long usuarioId) {
        Auditoria auditoria = Auditoria.builder()
            .accion(accion)
            .detalle(detalle)
            .usuarioId(usuarioId)
            .build();
        auditoriaRepository.save(auditoria);
    }

    public List<Auditoria> obtenerTodas() {
        List<Auditoria> auditorias = auditoriaRepository.findAll();
        if (auditorias.isEmpty()) {
            throw new RuntimeException("No hay auditorías registradas.");
        }
        return auditorias;
    }
}