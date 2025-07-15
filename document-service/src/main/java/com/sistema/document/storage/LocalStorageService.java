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
            Files.createDirectories(this.storagePath);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear el directorio de almacenamiento local", e);
        }
    }

    public void save(String filename, MultipartFile file) throws IOException {
        Path target = this.storagePath.resolve(filename);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
    }

    public Optional<Path> get(String filename) {
        Path target = this.storagePath.resolve(filename);
        return Files.exists(target) ? Optional.of(target) : Optional.empty();
    }
}
