package com.proyecto.Libreria.service;

import com.proyecto.Libreria.model.Prestamo;
import java.util.List;

public interface PrestamoService {
    List<Prestamo> obtenerPrestamosPorUsuario(Long usuarioId);
    List<Prestamo> obtenerPrestamosActivos();
    List<Prestamo> obtenerPrestamosVencidos();
    void eliminarPrestamo(Long id);
}
