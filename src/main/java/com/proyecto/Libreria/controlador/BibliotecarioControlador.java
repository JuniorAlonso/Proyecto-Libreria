package com.proyecto.Libreria.controlador;

import com.proyecto.Libreria.repositorio.BibliotecarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;



    @Controller
public class BibliotecarioControlador {

    @Autowired
    private BibliotecarioRepositorio bibliotecarioRepository;

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        // En una aplicación real, aquí podrías pasar un objeto Bibliotecario vacío al formulario
        return "login"; // Esto buscará un archivo 'login.html' o 'login.jsp' en tus vistas
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String usuario,
                               @RequestParam String contrasena,
                               Model model) {
        if (bibliotecarioRepository.validarCredenciales(usuario, contrasena)) {
            // Credenciales válidas
            model.addAttribute("mensaje", "¡Bienvenido, " + usuario + "!");
            // Redirigir a la página principal del bibliotecario o dashboard
            return "redirect:/dashboardBibliotecario";
        } else {
            // Credenciales inválidas
            model.addAttribute("error", "Usuario o contraseña incorrectos.");
            return "login"; // Volver al formulario de login
        }
    }

    @GetMapping("/dashboardBibliotecario")
    public String showDashboardBibliotecario() {
        return "dashboardBibliotecario"; // Vista para el dashboard del bibliotecario
    }
}

