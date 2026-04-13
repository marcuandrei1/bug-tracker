package com.bugtracker.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final Path uploadDir = Paths.get("uploads").toAbsolutePath();

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Fisierul este gol"));
        }

        String originalName = file.getOriginalFilename();
        if (originalName == null) originalName = "file";

        // Sanitize: keep only alphanumeric, dots, hyphens, underscores
        String safeName = originalName.replaceAll("[^a-zA-Z0-9.\\-_]", "_");
        String filename = UUID.randomUUID() + "_" + safeName;

        try {
            Files.createDirectories(uploadDir);
            Path filePath = uploadDir.resolve(filename).normalize();

            // Prevent path traversal
            if (!filePath.startsWith(uploadDir)) {
                return ResponseEntity.badRequest().body(Map.of("error", "Nume fisier invalid"));
            }

            Files.copy(file.getInputStream(), filePath);

            String url = "/uploads/" + filename;
            return ResponseEntity.ok(Map.of("url", url));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Eroare la salvarea fisierului"));
        }
    }
}
