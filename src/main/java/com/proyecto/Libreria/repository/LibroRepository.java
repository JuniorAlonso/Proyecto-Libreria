package com.proyecto.Libreria.repository;

import com.proyecto.Libreria.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Hereda de JpaRepository para obtener todos los métodos CRUD básicos
@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {

    // Aquí puedes añadir métodos de búsqueda personalizados si los necesitas,
    // por ejemplo: Libro findByTitulo(String titulo);
}