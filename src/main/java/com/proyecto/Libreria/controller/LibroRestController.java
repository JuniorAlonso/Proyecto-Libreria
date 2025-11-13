package com.proyecto.Libreria.controller;

import com.proyecto.Libreria.model.Libro;
import com.proyecto.Libreria.service.LibroService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/libros")
public class LibroRestController {

    private final LibroService libroService;

    public LibroRestController(LibroService libroService) {
        this.libroService = libroService;
    }

    @GetMapping
    public ResponseEntity<List<Libro>> obtenerTodosLosLibros() {
        return ResponseEntity.ok(libroService.obtenerTodosLosLibros());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Libro> obtenerLibroPorId(@PathVariable Long id) {
        Libro libro = libroService.obtenerLibroPorId(id);
        if (libro != null) {
            return ResponseEntity.ok(libro);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Libro> crearLibro(@RequestBody Libro libro) {
        Libro nuevoLibro = libroService.guardarLibro(libro);
        return ResponseEntity.ok(nuevoLibro);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Libro> actualizarLibro(@PathVariable Long id, @RequestBody Libro libro) {
        libro.setId(id);
        Libro libroActualizado = libroService.guardarLibro(libro);
        return ResponseEntity.ok(libroActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarLibro(@PathVariable Long id) {
        try {
            libroService.eliminarLibro(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
