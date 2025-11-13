package com.proyecto.Libreria.service.impl;

import com.proyecto.Libreria.repository.LibroRepository;
import com.proyecto.Libreria.repository.PrestamoRepository;
import com.proyecto.Libreria.repository.UsuarioRepository;
import com.proyecto.Libreria.service.DashboardService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final LibroRepository libroRepository;
    private final UsuarioRepository usuarioRepository;
    private final PrestamoRepository prestamoRepository;

    public DashboardServiceImpl(LibroRepository libroRepository, 
                                UsuarioRepository usuarioRepository,
                                PrestamoRepository prestamoRepository) {
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
