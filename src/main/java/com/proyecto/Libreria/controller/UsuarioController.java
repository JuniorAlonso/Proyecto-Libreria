package com.proyecto.Libreria.controller;

import com.proyecto.Libreria.model.Usuario;
import com.proyecto.Libreria.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
    public String iniciarSesion(@RequestParam String correo, @RequestParam String contrasena,
                                RedirectAttributes redirect, Model model) {
        var usuario = usuarioService.iniciarSesion(correo, contrasena);
        if (usuario.isPresent()) {
            model.addAttribute("usuario", usuario.get());
            return "redirect:/usuario/inicio";
        } else {
            redirect.addFlashAttribute("error", "Correo o contraseña incorrectos");
            return "redirect:/";
        }
    }

    // --- 2. REGISTRO (Flujo de 2 Pasos) ---

    // Paso 1: formulario de datos personales
    @GetMapping("/registro")
    public String mostrarRegistroPaso1(Model model) {
        if (!model.containsAttribute("usuario")) {
            model.addAttribute("usuario", new Usuario());
        }
        return "usuario/registro"; // registro.html
    }

    // Paso 2: recibe los datos de registro (datos personales) y los mantiene en sesión
    @PostMapping("/registro-pago")
    public String procesarRegistroPaso1(@ModelAttribute("usuario") Usuario usuario, Model model) {
        // El objeto 'usuario' se mantiene en la sesión gracias a @SessionAttributes
        model.addAttribute("usuario", usuario);
        return "usuario/registro-pago"; // registro-pago.html
    }

    // Paso 3: recibe datos de pago y finaliza el registro
    @PostMapping("/registro-finalizar")
    public String registrarUsuario(@ModelAttribute("usuario") Usuario usuario) {
        // Aquí 'usuario' ya tiene todos los datos personales + datos de pago
        usuarioService.registrar(usuario);
        return "redirect:/"; // vuelve al login
    }

    // --- 3. DASHBOARD ---
    @GetMapping("/usuario/inicio")
    public String mostrarDashboardInicio() {
        return "usuario/inicio";
    }

    @GetMapping("/usuario/biblioteca")
    public String mostrarBiblioteca() {
        return "usuario/biblioteca";
    }

    @GetMapping("/usuario/perfil")
    public String mostrarPerfil() {
        return "usuario/perfil";
    }
}
