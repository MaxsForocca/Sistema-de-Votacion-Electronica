package com.sistema.voting.service;

import com.sistema.voting.dto.VotacionDTO;
import com.sistema.voting.dto.votacion.PreguntaConOpcionesDTO;
import com.sistema.voting.dto.votacion.PreguntaDetalleDTO;
import com.sistema.voting.dto.votacion.VotacionCompletaDTO;
import com.sistema.voting.dto.votacion.VotacionDetalleDTO;
import com.sistema.voting.dto.votacion.OpcionDTO;
import com.sistema.voting.model.Categoria;
import com.sistema.voting.model.Opcion;
import com.sistema.voting.model.Pregunta;
import com.sistema.voting.model.Votacion;

import com.sistema.voting.repository.CategoriaRepository;
import com.sistema.voting.repository.VotacionRepository;
import com.sistema.voting.repository.PreguntaRepository;
import com.sistema.voting.repository.OpcionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VotacionService {

    @Autowired
    private VotacionRepository votacionRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private PreguntaRepository preguntaRepository;

    @Autowired
    private OpcionRepository opcionRepository;

    public VotacionDTO crearVotacion(VotacionDTO dto) {
        Votacion votacion = new Votacion();
        votacion.setTitulo(dto.getTitulo());
        votacion.setDescripcion(dto.getDescripcion());
        votacion.setActiva(dto.getActiva());

        // Relacionar con categoría si hay ID
        if (dto.getCategoriaId() != null) {
            Optional<Categoria> categoria = categoriaRepository.findById(dto.getCategoriaId());
            categoria.ifPresent(votacion::setCategoria);
        }

        Votacion guardada = votacionRepository.save(votacion);

        dto.setId(guardada.getId());
        return dto;
    }

    public Optional<VotacionDetalleDTO> obtenerPorId(Long id) {
        return votacionRepository.findById(id)
                .map(v -> {
                    VotacionDetalleDTO dto = new VotacionDetalleDTO();
                    dto.setId(v.getId());
                    dto.setTitulo(v.getTitulo());
                    dto.setDescripcion(v.getDescripcion());
                    dto.setActiva(v.getActiva());
                    dto.setCategoriaId(v.getCategoria() != null ? v.getCategoria().getId() : null);

                    List<PreguntaDetalleDTO> preguntas = v.getPreguntas().stream().map(p -> {
                        PreguntaDetalleDTO preguntaDTO = new PreguntaDetalleDTO();
                        preguntaDTO.setPreguntaId(p.getId());
                        preguntaDTO.setTexto(p.getTexto());

                        List<OpcionDTO> opciones = p.getOpciones().stream().map(o -> {
                            OpcionDTO opcionDTO = new OpcionDTO();
                            opcionDTO.setId(o.getId());
                            opcionDTO.setTexto(o.getTexto());
                            return opcionDTO;
                        }).collect(Collectors.toList());

                        preguntaDTO.setOpciones(opciones);
                        return preguntaDTO;
                    }).collect(Collectors.toList());

                    dto.setPreguntas(preguntas);
                    return dto;
                });
    }


    public List<VotacionDetalleDTO> listarTodasCompletas() {
        List<Votacion> votaciones = votacionRepository.findAll();

        return votaciones.stream().map(votacion -> {
            VotacionDetalleDTO dto = new VotacionDetalleDTO();
            dto.setId(votacion.getId());
            dto.setTitulo(votacion.getTitulo());
            dto.setDescripcion(votacion.getDescripcion());
            dto.setActiva(votacion.getActiva());
            dto.setCategoriaId(votacion.getCategoria() != null ? votacion.getCategoria().getId() : null);

            List<PreguntaDetalleDTO> preguntas = votacion.getPreguntas().stream().map(pregunta -> {
                PreguntaDetalleDTO preguntaDTO = new PreguntaDetalleDTO();
                preguntaDTO.setPreguntaId(pregunta.getId());
                preguntaDTO.setTexto(pregunta.getTexto());

                List<OpcionDTO> opciones = pregunta.getOpciones().stream().map(opcion -> {
                    OpcionDTO opcionDTO = new OpcionDTO();
                    opcionDTO.setId(opcion.getId());
                    opcionDTO.setTexto(opcion.getTexto());
                    return opcionDTO;
                }).collect(Collectors.toList());

                preguntaDTO.setOpciones(opciones);
                return preguntaDTO;
            }).collect(Collectors.toList());

            dto.setPreguntas(preguntas);
            return dto;
        }).collect(Collectors.toList());
    }

    public VotacionDTO crearVotacionCompleta(VotacionCompletaDTO dto) {
        // Crear votación
        Votacion votacion = new Votacion();
        votacion.setTitulo(dto.getTitulo());
        votacion.setDescripcion(dto.getDescripcion());
        votacion.setActiva(dto.getActiva());

        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        votacion.setCategoria(categoria);

        votacion = votacionRepository.save(votacion);

        // Crear preguntas y opciones
        for (PreguntaConOpcionesDTO preguntaDTO : dto.getPreguntas()) {
            Pregunta pregunta = new Pregunta();
            pregunta.setTexto(preguntaDTO.getTexto());
            pregunta.setVotacion(votacion);
            pregunta = preguntaRepository.save(pregunta);

            for (String textoOpcion : preguntaDTO.getOpciones()) {
                Opcion opcion = new Opcion();
                opcion.setTexto(textoOpcion);
                opcion.setPregunta(pregunta);
                opcionRepository.save(opcion);
            }
        }

        // Construcción manual de respuesta
        // Construcción manual de respuesta con categoriaId
        VotacionDTO response = new VotacionDTO();
        response.setId(votacion.getId());
        response.setTitulo(votacion.getTitulo());
        response.setDescripcion(votacion.getDescripcion());
        response.setActiva(votacion.getActiva());
        response.setCategoriaId(votacion.getCategoria() != null ? votacion.getCategoria().getId() : null);
        
        return response;
    }

    public VotacionDTO actualizarVotacion(Long id, VotacionCompletaDTO dto) {
        Votacion votacion = votacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Votación no encontrada"));

        votacion.setTitulo(dto.getTitulo());
        votacion.setDescripcion(dto.getDescripcion());
        votacion.setActiva(dto.getActiva());

        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        votacion.setCategoria(categoria);

        // Borrar preguntas anteriores
        List<Pregunta> preguntasExistentes = preguntaRepository.findByVotacionId(votacion.getId());
        preguntaRepository.deleteAll(preguntasExistentes);

        votacion = votacionRepository.save(votacion); // Guardar primero sin preguntas

        // Crear nuevas preguntas y opciones
        for (PreguntaConOpcionesDTO preguntaDTO : dto.getPreguntas()) {
            Pregunta nuevaPregunta = new Pregunta();
            nuevaPregunta.setTexto(preguntaDTO.getTexto());
            nuevaPregunta.setVotacion(votacion);

            nuevaPregunta = preguntaRepository.save(nuevaPregunta); // ¡Importante: guarda antes de agregar opciones!

            for (String textoOpcion : preguntaDTO.getOpciones()) {
                Opcion nuevaOpcion = new Opcion();
                nuevaOpcion.setTexto(textoOpcion);
                nuevaOpcion.setPregunta(nuevaPregunta); // ASIGNACIÓN NECESARIA
                opcionRepository.save(nuevaOpcion);
            }
        }

        // Respuesta
        VotacionDTO response = new VotacionDTO();
        response.setId(votacion.getId());
        response.setTitulo(votacion.getTitulo());
        response.setDescripcion(votacion.getDescripcion());
        response.setActiva(votacion.getActiva());
        response.setCategoriaId(categoria.getId());

        return response;
    }


    public void eliminarVotacion(Long id) {
        Votacion votacion = votacionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Votación no encontrada"));

        votacionRepository.delete(votacion); // Respeta las relaciones en cascada
    }
}
