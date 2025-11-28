package com.proyecto.Libreria;

import com.proyecto.Libreria.entidad.Libro;
import com.proyecto.Libreria.entidad.Usuario;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import com.proyecto.Libreria.repositorio.LibroRepositorio;
import com.proyecto.Libreria.repositorio.UsuarioRepositorio;

import java.time.LocalDateTime;

@Component
public class InicializadorDatos implements CommandLineRunner {

    private final LibroRepositorio libroRepository;
    private final UsuarioRepositorio usuarioRepository;

    public InicializadorDatos(LibroRepositorio libroRepository, UsuarioRepositorio usuarioRepository) {
        this.libroRepository = libroRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        // 1. Inicializar Usuario Administrador
        if (usuarioRepository.findByCorreo("admin@libreria.com").isEmpty()) {
            System.out.println("--- Creando usuario administrador ---");

            Usuario admin = new Usuario();
            admin.setNombreCompleto("Administrador");
            admin.setPrimerApellido("Sistema");
            admin.setCorreo("admin@libreria.com");
            admin.setContrasena(encoder.encode("admin123"));
            admin.setRol("ADMIN");
            admin.setActivo(true);
            admin.setFechaCreacion(LocalDateTime.now());
            admin.setTelefono("0000-0000");

            usuarioRepository.save(admin);
            System.out.println("Usuario administrador creado:");
            System.out.println("  Correo: admin@libreria.com");
            System.out.println("  Contrasena: admin123");
        }
        
        // 2. Inicializar Usuario de Prueba
        if (usuarioRepository.findByCorreo("maria.lopez@gmail.com").isEmpty()) {
            System.out.println("--- Creando usuario de prueba ---");

            Usuario usuario = new Usuario();
            usuario.setNombreCompleto("Maria Lopez");
            usuario.setPrimerApellido("Lopez");
            usuario.setSegundoApellido("Garcia");
            usuario.setCorreo("maria.lopez@gmail.com");
            usuario.setContrasena(encoder.encode("maria123"));
            usuario.setRol("USUARIO");
            usuario.setActivo(true);
            usuario.setFechaCreacion(LocalDateTime.now());
            usuario.setTelefono("7890-1234");
            usuario.setDireccion("Colonia Escalon, San Salvador");
            usuario.setDui("12345678-9");

            usuarioRepository.save(usuario);
            System.out.println("Usuario de prueba creado:");
            System.out.println("  Correo: maria.lopez@gmail.com");
            System.out.println("  Contrasena: maria123");
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
    }
}