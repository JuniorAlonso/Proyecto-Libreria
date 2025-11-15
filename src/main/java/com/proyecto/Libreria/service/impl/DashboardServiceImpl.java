package com.proyecto.Libreria.service.impl;

import com.proyecto.Libreria.service.DashboardService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import com.proyecto.Libreria.repositorio.LibroRepositorio;
import com.proyecto.Libreria.repositorio.PrestamoRepositorio;
import com.proyecto.Libreria.repositorio.UsuarioRepositorio;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final LibroRepositorio libroRepository;
    private final UsuarioRepositorio usuarioRepository;
    private final PrestamoRepositorio prestamoRepository;

    public DashboardServiceImpl(LibroRepositorio libroRepository, 
                                UsuarioRepositorio usuarioRepository,
                                PrestamoRepositorio prestamoRepository) {
        this.libroRepository = libroRepository;
        this.usuarioRepository = usuarioRepository;
        this.prestamoRepository = prestamoRepository;
    }

    @Override
    public Map<String, Long> obtenerEstadisticas() {
        Map<String, Long> estadisticas = new HashMap<>();
        
        // Total de libros
        estadisticas.put("totalLibros", libroRepository.count());
        
        // Total de usuarios
        estadisticas.put("totalUsuarios", usuarioRepository.count());
        
        // Préstamos activos
        estadisticas.put("prestamosActivos", prestamoRepository.countByEstado("ACTIVO"));
        
        // Préstamos vencidos
        estadisticas.put("prestamosVencidos", prestamoRepository.countPrestamosVencidos(LocalDate.now()));
        
        return estadisticas;
    }
}
