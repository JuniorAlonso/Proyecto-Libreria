package com.proyecto.Libreria.controlador;

import com.proyecto.Libreria.entidad.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BibliotecarioControlador {

    // Home Menu for Librarian
    @GetMapping("/dashboardBibliotecario")
    public String mostrarInicio(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        System.out.println("DEBUG: Accediendo a /dashboardBibliotecario");
        if (usuario == null) {
            System.out.println("DEBUG: Usuario es NULL en sesión");
            return "redirect:/";
        }
        System.out.println("DEBUG: Usuario en sesión: " + usuario.getCorreo());
        System.out.println("DEBUG: Rol del usuario: " + usuario.getRol());
        System.out.println("DEBUG: Nombre completo: " + usuario.getNombreCompleto());

        if (!"BIBLIOTECARIO".equalsIgnoreCase(usuario.getRol())) {
            System.out.println("DEBUG: Rol no es BIBLIOTECARIO, redirigiendo...");
            return "redirect:/";
        }
        model.addAttribute("usuario", usuario);
        return "bibliotecario/inicio";
    }

    // Book Management Page (View Only - Data loaded via JS)
    @GetMapping("/bibliotecario/gestion-libros")
    public String mostrarGestionLibros(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || !"BIBLIOTECARIO".equalsIgnoreCase(usuario.getRol())) {
            return "redirect:/";
        }

        model.addAttribute("usuario", usuario);
        return "bibliotecario/gestionLibros";
    }
}
