package com.sistema.document.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.Optional;

@Service
public class LocalStorageService {

    private final Path storagePath;

    public LocalStorageService(@Value("${document.storage.path}") String path) {
        this.storagePath = Paths.get(path).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.storagePath); // crea carpeta si no existe
        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear el directorio de almacenamiento local", e);
        }
    }

    public void save(String filename, MultipartFile file) throws IOException {
        Path destination = storagePath.resolve(filename);
        file.transferTo(destination); // intenta guardar archivo físico
    }

    public Optional<Path> get(String filename) {
        Path filePath = storagePath.resolve(filename);
        return Files.exists(filePath) ? Optional.of(filePath) : Optional.empty();
    }
}