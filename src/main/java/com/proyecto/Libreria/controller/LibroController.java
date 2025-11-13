package com.proyecto.Libreria.controller;

import com.proyecto.Libreria.model.Libro;
import com.proyecto.Libreria.model.Usuario;
import com.proyecto.Libreria.service.LibroService;
import jakarta.servlet.http.HttpSession;
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
    public String mostrarBiblioteca(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || usuario.getId() == null) {
            return "redirect:/"; 
        }

        List<Libro> libros = libroService.obtenerTodosLosLibros();
        model.addAttribute("libros", libros);
        model.addAttribute("usuario", usuario);

        // Retorna la vista: 
        return "usuario/biblioteca";
    }

    // 2. Método para mostrar préstamo 
    @GetMapping("/prestamos/solicitar")
    public String mostrarFormularioPrestamo(@RequestParam("libroId") Long idLibro, HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || usuario.getId() == null) {
            return "redirect:/"; 
        }

        Libro libroSeleccionado = libroService.obtenerLibroPorId(idLibro);

        if (libroSeleccionado == null) {
            return "redirect:/usuario/biblioteca?error=noencontrado";
        }

        model.addAttribute("libro", libroSeleccionado);
        model.addAttribute("usuario", usuario);

        return "usuario/prestamos";
    }

    // 3. Método para procesar la confirmación del préstamo
    @PostMapping("/prestamos/confirmar")
    public String confirmarPrestamo(@RequestParam("libroId") Long idLibro, 
                                   @RequestParam("fechaDevolucion") String fechaDevolucion,
                                   HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || usuario.getId() == null) {
            return "redirect:/"; 
        }

        try {
            // Validar que la fecha no exceda 15 días
            java.time.LocalDate fechaDev = java.time.LocalDate.parse(fechaDevolucion);
            java.time.LocalDate hoy = java.time.LocalDate.now();
            java.time.LocalDate maxFecha = hoy.plusDays(15);
            
            if (fechaDev.isBefore(hoy) || fechaDev.isAfter(maxFecha)) {
                return "redirect:/usuario/prestamos/solicitar?libroId=" + idLibro + "&error=La fecha debe estar entre hoy y máximo 15 días";
            }
            
            libroService.registrarPrestamo(idLibro, usuario.getId(), fechaDev);

            return "redirect:/usuario/biblioteca?success=prestamo";

        } catch (Exception e) {
            return "redirect:/usuario/prestamos/solicitar?libroId=" + idLibro + "&error=" + e.getMessage();
        }
    }

    @Autowired
    private com.proyecto.Libreria.service.PrestamoService prestamoService;

    // 4. Método para mostrar la vista principal de Préstamos 
    @GetMapping("/prestamos")
    public String mostrarListaPrestamos(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || usuario.getId() == null) {
            return "redirect:/"; 
        }
        
        // Obtener préstamos del usuario
        List<com.proyecto.Libreria.model.Prestamo> prestamos = prestamoService.obtenerPrestamosPorUsuario(usuario.getId());
        
        model.addAttribute("usuario", usuario);
        model.addAttribute("prestamos", prestamos);
        return "usuario/prestamos";
    }
}