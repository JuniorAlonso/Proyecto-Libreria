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

    // 1. Método para mostrar la biblioteca 
    @GetMapping("/biblioteca")
    public String mostrarBiblioteca(Model model) {

        List<Libro> libros = libroService.obtenerTodosLosLibros();
        model.addAttribute("libros", libros);

        // Retorna la vista: 
        return "usuario/biblioteca";
    }

    // 2. Método para mostrar préstamo 
    @GetMapping("/prestamos/solicitar")
    public String mostrarFormularioPrestamo(@RequestParam("libroId") Long idLibro, Model model) {

        Libro libroSeleccionado = libroService.obtenerLibroPorId(idLibro);

        if (libroSeleccionado == null) {
            return "redirect:/usuario/biblioteca?error=noencontrado";
        }

        model.addAttribute("libro", libroSeleccionado);

        return "usuario/prestamos";
    }

    // 3. Método para procesar la confirmación del préstamo
    @PostMapping("/prestamos/confirmar")
    public String confirmarPrestamo(@RequestParam("libroId") Long idLibro, Model model) {

        Long usuarioId = 1L; 

        try {
            libroService.registrarPrestamo(idLibro, usuarioId);

            return "redirect:/usuario/prestamos?success=true";

        } catch (Exception e) {
            return "redirect:/usuario/prestamos/solicitar?libroId=" + idLibro + "&error=" + e.getMessage();
        }
    }

    // 4. Método para mostrar la vista principal de Préstamos 
    @GetMapping("/prestamos")
    public String mostrarListaPrestamos(Model model) {
        return "usuario/prestamos";
    }
}