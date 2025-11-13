package com.proyecto.Libreria.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@RestController
@RequestMapping("/api/imagenes")
public class ImagenController {

    private final String UPLOAD_DIR = "src/main/resources/static/uploads/libros/";

    @PostMapping("/upload")
    public ResponseEntity<String> subirImagen(@RequestParam("imagen") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("No se seleccionó ningún archivo");
            }

            // Crear directorio si no existe
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Generar nombre único para el archivo
            String extension = getFileExtension(file.getOriginalFilename());
            String nombreArchivo = UUID.randomUUID().toString() + extension;
            Path filePath = uploadPath.resolve(nombreArchivo);

            // Guardar archivo
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Retornar la URL relativa
            String imageUrl = "/api/imagenes/ver/" + nombreArchivo;
            return ResponseEntity.ok(imageUrl);

        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Error al subir la imagen: " + e.getMessage());
        }
    }

    @GetMapping("/ver/{filename:.+}")
    public ResponseEntity<Resource> verImagen(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(UPLOAD_DIR).resolve(filename);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                String contentType = Files.probeContentType(filePath);
                if (contentType == null) {
                    contentType = "application/octet-stream";
                }

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    private String getFileExtension(String filename) {
        if (filename == null) return "";
        int lastDot = filename.lastIndexOf('.');
        return (lastDot == -1) ? "" : filename.substring(lastDot);
    }
}
