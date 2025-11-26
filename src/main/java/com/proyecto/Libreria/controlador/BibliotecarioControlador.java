package com.proyecto.Libreria.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BibliotecarioControlador {

    @Autowired
    private com.proyecto.Libreria.service.LibroService libroService;

    @Autowired
    private com.proyecto.Libreria.service.PrestamoService prestamoService;

    @GetMapping("/dashboardBibliotecario")
    public String showDashboardBibliotecario(Model model) {
        // Cargar datos para el dashboard
        // 1. Catálogo
        model.addAttribute("libros", libroService.obtenerTodosLosLibros());

        // 2. Licencias Activas (Préstamos activos)
        model.addAttribute("prestamos", prestamoService.obtenerPrestamosActivos());

        return "bibliotecario/dashboardBibliotecario";
    }

    // --- Gestión de Catálogo ---

    @GetMapping("/libro/nuevo")
    public String nuevoLibro(Model model) {
        model.addAttribute("libro", new com.proyecto.Libreria.entidad.Libro());
        return "bibliotecario/formLibro";
    }

    @PostMapping("/libro/guardar")
    public String guardarLibro(
            @org.springframework.web.bind.annotation.ModelAttribute com.proyecto.Libreria.entidad.Libro libro) {
        libroService.guardarLibro(libro);
        return "redirect:/dashboardBibliotecario";
    }

    @GetMapping("/libro/editar/{id}")
    public String editarLibro(@org.springframework.web.bind.annotation.PathVariable Long id, Model model) {
        com.proyecto.Libreria.entidad.Libro libro = libroService.obtenerLibroPorId(id);
        model.addAttribute("libro", libro);
        return "bibliotecario/formLibro";
    }

    @GetMapping("/libro/eliminar/{id}")
    public String eliminarLibro(@org.springframework.web.bind.annotation.PathVariable Long id) {
        libroService.eliminarLibro(id);
        return "redirect:/dashboardBibliotecario";
    }

    // --- Licencias y Reportes ---

    @GetMapping("/licencias")
    public String licenciasActivas(Model model) {
        model.addAttribute("prestamos", prestamoService.obtenerPrestamosActivos());
        return "bibliotecario/licencias";
    }

    @GetMapping("/reportes")
    public String reportes(Model model) {
        // Simulación de datos para el reporte
        model.addAttribute("librosMasLeidos", libroService.obtenerTodosLosLibros());
        return "bibliotecario/reportes";
    }

    // --- Notificaciones ---

    @GetMapping("/notificar")
    public String notificarDisponibilidad(@RequestParam Long libroId, Model model) {
        // Simulación de envío de correo
        System.out.println("Enviando correo de disponibilidad para el libro ID: " + libroId);
        return "redirect:/dashboardBibliotecario?success=notificacion";
    }
}
