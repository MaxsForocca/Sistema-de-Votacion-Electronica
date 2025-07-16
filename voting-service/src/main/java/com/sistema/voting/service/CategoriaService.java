package com.sistema.voting.service;

import com.sistema.voting.dto.CategoriaConVotacionesDTO;
import com.sistema.voting.dto.CategoriaDTO;
import com.sistema.voting.dto.VotacionDTO;
import com.sistema.voting.model.Categoria;
import com.sistema.voting.repository.CategoriaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<CategoriaDTO> obtenerTodas() {
        List<Categoria> categorias = categoriaRepository.findAll();
        if (categorias.isEmpty()) {
            throw new RuntimeException("No hay categorías registradas.");
        }

        return categorias.stream()
                .map(c -> {
                    CategoriaDTO dto = new CategoriaDTO();
                    dto.setId(c.getId());
                    dto.setNombre(c.getNombre());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public CategoriaDTO crearCategoria(CategoriaDTO dto) {
        Categoria categoria = new Categoria();
        categoria.setNombre(dto.getNombre());

        Categoria guardada = categoriaRepository.save(categoria);
        dto.setId(guardada.getId());
        return dto;
    }

    public List<CategoriaConVotacionesDTO> obtenerCategoriasConVotaciones() {
        List<Categoria> categorias = categoriaRepository.findAll();
        if (categorias.isEmpty()) {
            throw new RuntimeException("No hay categorías con votaciones.");
        }

        return categorias.stream().map(categoria -> {
            CategoriaConVotacionesDTO dto = new CategoriaConVotacionesDTO();
            dto.setId(categoria.getId());
            dto.setNombre(categoria.getNombre());

            List<VotacionDTO> votaciones = categoria.getVotaciones().stream().map(v -> {
                VotacionDTO vdto = new VotacionDTO();
                vdto.setId(v.getId());
                vdto.setTitulo(v.getTitulo());
                vdto.setDescripcion(v.getDescripcion());
                vdto.setActiva(v.getActiva());
                return vdto;
            }).collect(Collectors.toList());

            dto.setVotaciones(votaciones);
            return dto;
        }).collect(Collectors.toList());
    }

    public CategoriaDTO actualizarCategoria(Long id, CategoriaDTO dto) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id: " + id));

        categoria.setNombre(dto.getNombre());
        Categoria actualizada = categoriaRepository.save(categoria);

        CategoriaDTO actualizadoDTO = new CategoriaDTO();
        actualizadoDTO.setId(actualizada.getId());
        actualizadoDTO.setNombre(actualizada.getNombre());

        return actualizadoDTO;
    }

    public void eliminarCategoria(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id: " + id));

        categoriaRepository.delete(categoria);
    }
}
