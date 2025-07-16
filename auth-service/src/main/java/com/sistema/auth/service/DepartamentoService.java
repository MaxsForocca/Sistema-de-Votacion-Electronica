package com.sistema.auth.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.sistema.auth.dto.DepartamentoDTO;
import com.sistema.auth.model.Departamento;
import com.sistema.auth.repository.DepartamentoRepository;

@Service
public class DepartamentoService {
    private final DepartamentoRepository departamentoRepository;

    public DepartamentoService(DepartamentoRepository departamentoRepository) {
        this.departamentoRepository = departamentoRepository;
    }

    public DepartamentoDTO crear(DepartamentoDTO dto) {
        if (departamentoRepository.existsByNombre(dto.getNombre())) {
            throw new RuntimeException("Ya existe un departamento con ese nombre");
        }

        Departamento depto = new Departamento();
        depto.setNombre(dto.getNombre());

        Departamento guardado = departamentoRepository.save(depto);
        return new DepartamentoDTO(guardado.getId(), guardado.getNombre());
    }

    public List<DepartamentoDTO> listarTodos() {
        return departamentoRepository.findAll()
                .stream()
                .map(dep -> new DepartamentoDTO(dep.getId(), dep.getNombre()))
                .collect(Collectors.toList());
    }

    public Optional<DepartamentoDTO> obtenerPorId(Long id) {
        return departamentoRepository.findById(id)
                .map(dep -> new DepartamentoDTO(dep.getId(), dep.getNombre()));
    }

    public DepartamentoDTO actualizar(Long id, DepartamentoDTO dto) {
        Departamento existente = departamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Departamento no encontrado"));

        existente.setNombre(dto.getNombre());

        Departamento actualizado = departamentoRepository.save(existente);
        return new DepartamentoDTO(actualizado.getId(), actualizado.getNombre());
    }

    public void eliminar(Long id) {
        if (!departamentoRepository.existsById(id)) {
            throw new RuntimeException("Departamento no encontrado");
        }
        departamentoRepository.deleteById(id);
    }
}
