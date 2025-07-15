package com.sistema.document.storage;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    public List<String> listFileNames() {
        List<String> filenames = new ArrayList<>();
        File folder = storagePath.toFile();

        if (folder.exists() && folder.isDirectory()) {
            for (File file : folder.listFiles()) {
                if (file.isFile()) {
                    filenames.add(file.getName());
                }
            }
        }
        return filenames;
    }
}