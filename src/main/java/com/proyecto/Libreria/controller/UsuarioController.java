package com.proyecto.Libreria.controller;

import com.proyecto.Libreria.model.Usuario;
import com.proyecto.Libreria.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus; // Importar SessionStatus
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@SessionAttributes("usuario") // Mantiene el objeto Usuario en sesión entre pasos
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // --- 1. LOGIN ---
    @GetMapping("/")
    public String mostrarLogin(Model model) {
        return "usuario/index"; // index.html
    }

    @PostMapping("/iniciar-sesion")
    public String iniciarSesion(@RequestParam String correo,
            @RequestParam String contrasena,
            RedirectAttributes redirect,
            HttpSession session) {
        correo = correo.toLowerCase().trim();
        contrasena = contrasena.trim();

        var usuario = usuarioService.iniciarSesion(correo, contrasena);
        if (usuario.isPresent()) {
            session.setAttribute("usuario", usuario.get());
            return "redirect:/usuario/inicio";
        } else {
            redirect.addFlashAttribute("error", "Correo o contraseña incorrectos");
            return "redirect:/";
        }
    }

    // Método para inicializar el objeto Usuario para la sesión
    @ModelAttribute("usuario")
    public Usuario setupUsuario() {
        return new Usuario();
    }

    // --- 2. REGISTRO (Flujo de 2 Pasos) ---
    // Paso 1: formulario de datos personales
    @GetMapping("/registro")
    public String mostrarRegistroPaso1(Model model) {
        // La anotación @ModelAttribute("usuario") arriba ya asegura que haya un objeto Usuario nuevo en el modelo.
        return "usuario/registro"; // registro.html
    }

    // Paso 2: recibe los datos de registro (datos personales) y los mantiene en sesión
    @PostMapping("/registro-pago")
    public String procesarRegistroPaso1(@ModelAttribute("usuario") Usuario usuario, Model model) {
        // El objeto 'usuario' se mantiene en la sesión gracias a @SessionAttributes
        model.addAttribute("usuario", usuario);
        return "usuario/registro-pago"; // registro-pago.html
    }

    // Paso 3: recibe datos de pago, finaliza el registro y GUARDA
    @PostMapping("/registro-finalizar")
    public String registrarUsuario(@ModelAttribute("usuario") Usuario usuario) {

        usuarioService.registrar(usuario);

        return "redirect:/"; // vuelve al login
    }

    // --- 3. DASHBOARD Y OTROS ---
    @GetMapping("/usuario/inicio")
    public String mostrarDashboardInicio(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || usuario.getId() == null) {
            return "redirect:/"; // no hay sesión
        }
        model.addAttribute("usuario", usuario);
        return "usuario/inicio";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // termina sesión
        return "redirect:/";
    }

    @GetMapping("/usuario/biblioteca")
    public String mostrarBiblioteca(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || usuario.getId() == null) {
            return "redirect:/";
        }
        model.addAttribute("usuario", usuario);
        return "usuario/biblioteca";
    }

    @GetMapping("/usuario/perfil")
    public String mostrarPerfil() {
        return "usuario/perfil";
    }
}
