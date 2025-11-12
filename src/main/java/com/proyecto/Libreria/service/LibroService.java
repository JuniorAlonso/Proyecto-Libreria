package com.proyecto.Libreria.service;

import com.proyecto.Libreria.model.Libro;
import java.util.List;

public interface LibroService {

    List<Libro> obtenerTodosLosLibros();

    Libro obtenerLibroPorId(Long id);

    void registrarPrestamo(Long libroId, Long usuarioId);
}