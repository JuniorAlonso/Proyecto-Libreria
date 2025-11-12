package com.proyecto.Libreria.service.impl;

import com.proyecto.Libreria.model.Libro;
import com.proyecto.Libreria.repository.LibroRepository;
import com.proyecto.Libreria.service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LibroServiceImpl implements LibroService {

    @Autowired
    private LibroRepository libroRepository;

    @Override
    public List<Libro> obtenerTodosLosLibros() {
        // En un entorno real, esto traería todos los libros de la base de datos
        return libroRepository.findAll();
    }

    @Override
    public Libro obtenerLibroPorId(Long id) {
        // Busca el libro por ID. Si no lo encuentra, devuelve null.
        Optional<Libro> libroOpt = libroRepository.findById(id);
        return libroOpt.orElse(null);
    }

    @Override
    public void registrarPrestamo(Long libroId, Long usuarioId) {
        // **LÓGICA REAL DE PRÉSTAMO**

        // 1. Busca el libro
        Libro libro = obtenerLibroPorId(libroId);

        if (libro != null && libro.getStock() > 0) {
            // 2. Decrementa el stock
            libro.setStock(libro.getStock() - 1);
            libroRepository.save(libro); // Guarda el cambio en el stock

            // 3. (FALTANTE) Registra el objeto Préstamo en su respectiva tabla
            System.out.println("Préstamo registrado: Libro ID " + libroId + " a Usuario ID " + usuarioId);
        } else {
            throw new RuntimeException("Libro no disponible para préstamo.");
        }
    }
}
