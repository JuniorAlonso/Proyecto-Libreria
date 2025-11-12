package com.proyecto.Libreria.controller;

import com.proyecto.Libreria.model.Libro;
import com.proyecto.Libreria.service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequestMapping("/usuario")
public class LibroController {

    @Autowired
    private LibroService libroService;

    // 1. Método para mostrar la biblioteca (URL: /usuario/biblioteca)
    @GetMapping("/biblioteca")
    public String mostrarBiblioteca(Model model) {
        // Usa el servicio real para obtener los libros de la base de datos
        List<Libro> libros = libroService.obtenerTodosLosLibros();
        model.addAttribute("libros", libros);

        // Retorna la vista: /templates/usuario/biblioteca.html
        return "usuario/biblioteca";
    }

    // 2. Método para mostrar la página de solicitud de préstamo (URL:
    // /usuario/prestamos/solicitar?libroId=X)
    @GetMapping("/prestamos/solicitar")
    public String mostrarFormularioPrestamo(@RequestParam("libroId") Long idLibro, Model model) {

        Libro libroSeleccionado = libroService.obtenerLibroPorId(idLibro);

        if (libroSeleccionado == null) {
            // Manejo de error si el ID no es válido
            return "redirect:/usuario/biblioteca?error=noencontrado";
        }

        model.addAttribute("libro", libroSeleccionado);

        // Retorna la vista: /templates/usuario/prestamos.html
        return "usuario/prestamos";
    }

    // 3. Método para procesar la confirmación del préstamo (POST
    // /usuario/prestamos/confirmar)
    @PostMapping("/prestamos/confirmar")
    public String confirmarPrestamo(@RequestParam("libroId") Long idLibro, Model model) {

        Long usuarioId = 1L; // ID de usuario temporal

        try {
            libroService.registrarPrestamo(idLibro, usuarioId);

            // Redirige a la lista de préstamos con un mensaje de éxito
            return "redirect:/usuario/prestamos?success=true";

        } catch (Exception e) {
            // Redirige a la página de préstamo con un error
            return "redirect:/usuario/prestamos/solicitar?libroId=" + idLibro + "&error=" + e.getMessage();
        }
    }

    // 4. Método para mostrar la vista principal de Préstamos (GET
    // /usuario/prestamos)
    @GetMapping("/prestamos")
    public String mostrarListaPrestamos(Model model) {
        // Retorna la vista: /templates/usuario/prestamos.html (o prestamos_lista.html)
        return "usuario/prestamos";
    }
}