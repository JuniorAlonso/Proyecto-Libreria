package com.proyecto.Libreria.service;

import com.proyecto.Libreria.entidad.Libro;
import java.util.List;

public interface LibroService {

    List<Libro> obtenerTodosLosLibros();

    Libro obtenerLibroPorId(Long id);

    Libro guardarLibro(Libro libro);

    void eliminarLibro(Long id);

    void registrarPrestamo(Long libroId, Long usuarioId);
    void registrarPrestamo(Long libroId, Long usuarioId, java.time.LocalDate fechaDevolucion);
}