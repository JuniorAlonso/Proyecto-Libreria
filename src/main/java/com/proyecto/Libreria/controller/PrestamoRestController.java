package com.proyecto.Libreria.controller;

import com.proyecto.Libreria.service.PrestamoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/prestamos")
public class PrestamoRestController {

    private final PrestamoService prestamoService;

    public PrestamoRestController(PrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarPrestamo(@PathVariable Long id) {
        try {
            prestamoService.eliminarPrestamo(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
