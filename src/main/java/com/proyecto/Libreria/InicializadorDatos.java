package com.proyecto.Libreria;

import com.proyecto.Libreria.entidad.Libro;
import com.proyecto.Libreria.entidad.Usuario;
import com.proyecto.Libreria.service.UsuarioService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.proyecto.Libreria.repositorio.LibroRepositorio;
import com.proyecto.Libreria.repositorio.UsuarioRepositorio;

import java.time.LocalDateTime;

@Component
public class InicializadorDatos implements CommandLineRunner {

    private final LibroRepositorio libroRepository;
    private final UsuarioRepositorio usuarioRepository;
    private final UsuarioService usuarioService;

    public InicializadorDatos(LibroRepositorio libroRepository, UsuarioRepositorio usuarioRepository,
            UsuarioService usuarioService) {
        this.libroRepository = libroRepository;
        this.usuarioRepository = usuarioRepository;
        this.usuarioService = usuarioService;
    }

    @Override
    public void run(String... args) throws Exception {
        // 1. Crear nuevo administrador si no existe
        System.out.println("--- Verificando administrador del sistema ---");
        try {
            String adminEmail = "admin@biblioteca.com";
            java.util.Optional<Usuario> adminExistente = usuarioRepository.findByCorreo(adminEmail);

            if (adminExistente.isEmpty()) {
                Usuario nuevoAdmin = new Usuario();
                nuevoAdmin.setNombreCompleto("Administrador");
                nuevoAdmin.setPrimerApellido("Sistema");
                nuevoAdmin.setCorreo(adminEmail);
                nuevoAdmin.setContrasena("admin123"); // El servicio lo encriptará
                nuevoAdmin.setRol("ADMIN");
                nuevoAdmin.setActivo(true);
                nuevoAdmin.setFechaCreacion(LocalDateTime.now());

                // Usar el servicio en lugar del repositorio
                Usuario adminGuardado = usuarioService.registrar(nuevoAdmin);
                System.out.println("✓ Nuevo administrador creado: " + adminEmail);
                System.out.println("  ID: " + adminGuardado.getId());
                System.out.println("  Contraseña: admin123");
            } else {
                System.out.println("✓ Administrador ya existe: " + adminEmail);
            }
        } catch (Exception e) {
            System.err.println("Error al crear administrador: " + e.getMessage());
            e.printStackTrace();
        }

        // 2. Convertir a todos en ADMIN PRIMERO
        System.out.println("--- Actualizando todos los usuarios a ADMIN ---");
        try {
            java.util.List<Usuario> todos = usuarioRepository.findAll();
            for (Usuario u : todos) {
                if (!"ADMIN".equals(u.getRol())) {
                    u.setRol("ADMIN");
                    u.setActivo(true);
                    usuarioRepository.save(u);
                    System.out.println("Usuario actualizado a ADMIN: " + u.getCorreo());
                }
            }
        } catch (Exception e) {
            System.err.println("Error al actualizar usuarios: " + e.getMessage());
        }

        // 3. Inicializar Libros
        if (libroRepository.count() == 0) {
            System.out.println("--- Inicializando datos de libros ---");

            Libro l1 = new Libro();
            l1.setTitulo("Cien años de soledad");
            l1.setAutor("Gabriel García Márquez");
            l1.setStock(5);
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

            System.out.println("Libros iniciales cargados: " + libroRepository.count());
        }

        System.out.println("=== Inicialización completada ===");
    }
}