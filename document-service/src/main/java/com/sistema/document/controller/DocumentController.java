package com.sistema.document.controller;

import com.sistema.document.service.RemoteNodeService;
import com.sistema.document.storage.LocalStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/documentos")
public class DocumentController {

    @Value("${document.node.ip}")
    private String documentNodeIp;

    @Autowired
    private LocalStorageService localStorageService;

    @Autowired
    private RemoteNodeService remoteNodeService;

    @GetMapping
    public ResponseEntity<List<String>> listarArchivosLocalesYRemotos(
          @RequestHeader(value = "X-Visited-Nodes", required = false) String visitedHeader
    ) {
        System.out.println("Header X-Visited-Nodes recibido: " + visitedHeader);
        List<String> visitedNodes = new ArrayList<>();
        if (visitedHeader != null && !visitedHeader.isBlank()) {
            visitedNodes.addAll(Arrays.asList(visitedHeader.split(",")));
        }
        visitedNodes.add(documentNodeIp);

        List<String> archivosLocales = localStorageService.listFileNames();
        List<String> archivosRemotos = remoteNodeService.getAllFileNamesFromOtherNodes(visitedNodes);

        Set<String> todosLosArchivos = new HashSet<>();
        todosLosArchivos.addAll(archivosLocales);
        todosLosArchivos.addAll(archivosRemotos); // evitar duplicados

        return ResponseEntity.ok(new ArrayList<>(todosLosArchivos));
    }

    @PostMapping("/subir")
    public ResponseEntity<String> subir(@RequestParam("archivo") MultipartFile archivo) {
        System.out.print("Entrando a método subir.");
        try {
            localStorageService.save(archivo.getOriginalFilename(), archivo);
            return ResponseEntity.ok("Archivo guardado localmente");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error al guardar archivo: " + e.getMessage());
        }
    }

    @GetMapping("/{filename}")
    public ResponseEntity<?> descargar(
            @PathVariable String filename,
            @RequestHeader(value = "X-Visited-Nodes", required = false) String visitedHeader) {

    System.out.println("Header X-Visited-Nodes recibido: " + visitedHeader);

        try {
            var local = localStorageService.get(filename);
            if (local.isPresent()) {
                byte[] data = Files.readAllBytes(local.get());
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .body(new ByteArrayResource(data));
            }

            // Armar lista de nodos visitados
            List<String> visitedNodes = new ArrayList<>();
            if (visitedHeader != null && !visitedHeader.isBlank()) {
                visitedNodes.addAll(Arrays.asList(visitedHeader.split(",")));
            }
            visitedNodes.add(documentNodeIp); // Añadir este nodo a la lista

            byte[] remote = remoteNodeService.getFileFromOtherNodes(filename, visitedNodes);
            if (remote != null) {
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .body(new ByteArrayResource(remote));
            }

            return ResponseEntity.status(404).body("Archivo no encontrado en ningún nodo");

        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error al leer el archivo");
        }
    }
}
