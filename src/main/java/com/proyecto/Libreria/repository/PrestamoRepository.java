package com.proyecto.Libreria.repository;

import com.proyecto.Libreria.model.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {
    
    // Contar préstamos activos
    long countByEstado(String estado);
    
    // Obtener préstamos activos
    List<Prestamo> findByEstado(String estado);
    
    // Obtener préstamos vencidos
    @Query("SELECT p FROM Prestamo p WHERE p.estado = 'ACTIVO' AND p.fechaDevolucion < :fecha")
    List<Prestamo> findPrestamosVencidos(LocalDate fecha);
    
    // Contar préstamos vencidos
    @Query("SELECT COUNT(p) FROM Prestamo p WHERE p.estado = 'ACTIVO' AND p.fechaDevolucion < :fecha")
    long countPrestamosVencidos(LocalDate fecha);
}
