package com.proyecto.Libreria.controlador;

import com.proyecto.Libreria.entidad.Usuario;
import com.proyecto.Libreria.service.DashboardService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminControlador {

    private final DashboardService dashboardService;

    public AdminControlador(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/dashboard")
    public String mostrarDashboard(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || usuario.getId() == null || 
            !("ADMIN".equalsIgnoreCase(usuario.getRol()))) {
            return "redirect:/usuario/inicio";
        }
        
        // Obtener estadísticas desde la base de datos
        Map<String, Long> estadisticas = dashboardService.obtenerEstadisticas();
        
        model.addAttribute("usuario", usuario);
        model.addAttribute("totalLibros", estadisticas.get("totalLibros"));
        model.addAttribute("totalUsuarios", estadisticas.get("totalUsuarios"));
        model.addAttribute("prestamosActivos", estadisticas.get("prestamosActivos"));
        model.addAttribute("prestamosVencidos", estadisticas.get("prestamosVencidos"));
        
        return "admin/dashboard";
    }

    @GetMapping("/libros")
    public String mostrarGestionLibros(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || usuario.getId() == null || 
            !("ADMIN".equalsIgnoreCase(usuario.getRol()))) {
            return "redirect:/usuario/inicio";
        }
        model.addAttribute("usuario", usuario);
        return "admin/libros";
    }

    @GetMapping("/usuarios")
    public String mostrarGestionUsuarios(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || usuario.getId() == null || 
            !("ADMIN".equalsIgnoreCase(usuario.getRol()))) {
            return "redirect:/usuario/inicio";
        }
        model.addAttribute("usuario", usuario);
        return "admin/usuarios";
    }

    @Autowired
    private com.proyecto.Libreria.service.PrestamoService prestamoService;

    @GetMapping("/reportes")
    public String mostrarReportes(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || usuario.getId() == null || 
            !("ADMIN".equalsIgnoreCase(usuario.getRol()))) {
            return "redirect:/usuario/inicio";
        }
        
        // Obtener todos los préstamos
        java.util.List<com.proyecto.Libreria.entidad.Prestamo> prestamos = prestamoService.obtenerPrestamosActivos();
        
        // También obtener todos los préstamos (activos y devueltos)
        java.util.List<com.proyecto.Libreria.entidad.Prestamo> todosPrestamos = 
            prestamoRepository.findAll();
        
        model.addAttribute("usuario", usuario);
        model.addAttribute("prestamos", todosPrestamos);
        return "admin/reportes";
    }
    
    @Autowired
    private com.proyecto.Libreria.repositorio.PrestamoRepositorio prestamoRepository;
}
