package com.proyecto.Libreria;

import com.proyecto.Libreria.entidad.Libro;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.proyecto.Libreria.repositorio.LibroRepositorio;

@Component
public class InicializadorDatos implements CommandLineRunner {

    private final LibroRepositorio libroRepository;

    public InicializadorDatos(LibroRepositorio libroRepository) {
        this.libroRepository = libroRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (libroRepository.count() == 0) {
            System.out.println("--- Inicializando datos de libros ---");

            // Los libros se insertarán 

            Libro l1 = new Libro();
            l1.setTitulo("Cien años de soledad");
            l1.setAutor("Gabriel García Márquez");
            l1.setStock(5); // Stock es clave
            libroRepository.save(l1);

            Libro l2 = new Libro();
            l2.setTitulo("El amor en los tiempos del cólera");
            l2.setAutor("Gabriel García Márquez");
            l2.setStock(3);
            libroRepository.save(l2);

            Libro l3 = new Libro();
            l3.setTitulo("Don Quijote de la Mancha");
            l3.setAutor("Miguel de Cervantes");
            l3.setStock(8);
            libroRepository.save(l3);

            System.out.println(" Libros iniciales cargados: " + libroRepository.count());
        }
    }
}