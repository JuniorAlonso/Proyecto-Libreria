package com.proyecto.Libreria.service.impl;

import com.proyecto.Libreria.model.Prestamo;
import com.proyecto.Libreria.repository.PrestamoRepository;
import com.proyecto.Libreria.service.PrestamoService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PrestamoServiceImpl implements PrestamoService {

    private final PrestamoRepository prestamoRepository;

    public PrestamoServiceImpl(PrestamoRepository prestamoRepository) {
        this.prestamoRepository = prestamoRepository;
    }

    @Override
    public List<Prestamo> obtenerPrestamosPorUsuario(Long usuarioId) {
        return prestamoRepository.findAll().stream()
                .filter(p -> p.getUsuario().getId().equals(usuarioId))
                .toList();
    }

    @Override
    public List<Prestamo> obtenerPrestamosActivos() {
        return prestamoRepository.findByEstado("ACTIVO");
    }

    @Override
    public List<Prestamo> obtenerPrestamosVencidos() {
        return prestamoRepository.findPrestamosVencidos(LocalDate.now());
    }

    @Override
    public void eliminarPrestamo(Long id) {
        if (!prestamoRepository.existsById(id)) {
            throw new RuntimeException("Préstamo no encontrado");
        }
        prestamoRepository.deleteById(id);
    }
}
