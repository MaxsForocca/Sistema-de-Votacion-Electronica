package com.sistema.auth.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.sistema.auth.dto.DepartamentoDTO;
import com.sistema.auth.model.Departamento;
import com.sistema.auth.repository.DepartamentoRepository;

@Service
public class DepartamentoService {
    private final DepartamentoRepository departamentoRepository;  
    private final ModelMapper modelMapper;
    
    public DepartamentoService(DepartamentoRepository departamentoRepository, ModelMapper modelMapper) {
        this.departamentoRepository = departamentoRepository;
        this.modelMapper = modelMapper;
    }

    public DepartamentoDTO crear(DepartamentoDTO dto) {
        if (departamentoRepository.existsByNombre(dto.getNombre())) {
            throw new RuntimeException("Ya existe un departamento con ese nombre");
        }
        Departamento depto = modelMapper.map(dto, Departamento.class);
        return modelMapper.map(departamentoRepository.save(depto), DepartamentoDTO.class);
    }

    public List<DepartamentoDTO> listarTodos() {
        return departamentoRepository.findAll()
                .stream()
                .map(dep -> modelMapper.map(dep, DepartamentoDTO.class))
                .collect(Collectors.toList());
    }

    public Optional<DepartamentoDTO> obtenerPorId(Long id) {
        return departamentoRepository.findById(id)
                .map(dep -> modelMapper.map(dep, DepartamentoDTO.class));
    }

    public DepartamentoDTO actualizar(Long id, DepartamentoDTO dto) {
        Departamento existente = departamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Departamento no encontrado"));
        existente.setNombre(dto.getNombre());
        return modelMapper.map(departamentoRepository.save(existente), DepartamentoDTO.class);
    }

    public void eliminar(Long id) {
        if (!departamentoRepository.existsById(id)) {
            throw new RuntimeException("Departamento no encontrado");
        }
        departamentoRepository.deleteById(id);
    }
    
}
