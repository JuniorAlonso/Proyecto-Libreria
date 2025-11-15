package com.proyecto.Libreria.service.impl;

import com.proyecto.Libreria.entidad.Libro;
import com.proyecto.Libreria.service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import com.proyecto.Libreria.repositorio.LibroRepositorio;

@Service
public class LibroServiceImpl implements LibroService {

    @Autowired
    private LibroRepositorio libroRepository;

    @Override
    public List<Libro> obtenerTodosLosLibros() {
        // En un entorno trae todos los libros de la base de datos
        return libroRepository.findAll();
    }

    @Override
    public Libro obtenerLibroPorId(Long id) {
        // Busca el libro por ID.
        Optional<Libro> libroOpt = libroRepository.findById(id);
        return libroOpt.orElse(null);
    }

    @Override
    public Libro guardarLibro(Libro libro) {
        return libroRepository.save(libro);
    }

    @Override
    public void eliminarLibro(Long id) {
        try {
            // Verificar si hay préstamos activos con este libro
            List<com.proyecto.Libreria.entidad.Prestamo> prestamosActivos = prestamoRepository.findAll().stream()
                    .filter(p -> p.getLibro().getId().equals(id) && "ACTIVO".equals(p.getEstado()))
                    .toList();
            
            if (!prestamosActivos.isEmpty()) {
                throw new RuntimeException("No se puede eliminar el libro porque tiene préstamos activos");
            }
            
            libroRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar el libro: " + e.getMessage());
        }
    }

    @Autowired
    private com.proyecto.Libreria.repositorio.PrestamoRepositorio prestamoRepository;
    
    @Autowired
    private com.proyecto.Libreria.repositorio.UsuarioRepositorio usuarioRepository;

    @Override
    public void registrarPrestamo(Long libroId, Long usuarioId) {
        registrarPrestamo(libroId, usuarioId, java.time.LocalDate.now().plusDays(15));
    }

    @Override
    public void registrarPrestamo(Long libroId, Long usuarioId, java.time.LocalDate fechaDevolucion) {
        // Busca el libro
        Libro libro = obtenerLibroPorId(libroId);

        if (libro != null && libro.getStock() > 0) {
            // Decrementa el stock
            libro.setStock(libro.getStock() - 1);
            libroRepository.save(libro); 

            // Crear el registro del préstamo
            com.proyecto.Libreria.entidad.Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
            if (usuario != null) {
                com.proyecto.Libreria.entidad.Prestamo prestamo = new com.proyecto.Libreria.entidad.Prestamo();
                prestamo.setLibro(libro);
                prestamo.setUsuario(usuario);
                prestamo.setFechaPrestamo(java.time.LocalDate.now());
                prestamo.setFechaDevolucion(fechaDevolucion);
                prestamo.setEstado("ACTIVO");
                
                prestamoRepository.save(prestamo);
                System.out.println("Préstamo registrado: Libro ID " + libroId + " a Usuario ID " + usuarioId + " hasta " + fechaDevolucion);
            }
        } else {
            throw new RuntimeException("Libro no disponible para préstamo.");
        }
    }
}
