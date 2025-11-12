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
@SessionAttributes("usuario") 
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // LOGIN 
    @GetMapping("/")
    public String mostrarLogin(Model model) {
        return "usuario/index";
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

    // REGISTRO 
    // formulario
    @GetMapping("/registro")
    public String mostrarRegistroPaso1(Model model) {
        return "usuario/registro"; // registro.html
    }

    // recibe los datos d
    @PostMapping("/registro-pago")
    public String procesarRegistroPaso1(@ModelAttribute("usuario") Usuario usuario, Model model) {
        model.addAttribute("usuario", usuario);
        return "usuario/registro-pago"; 
    }

    // recibe datos de pago
    @PostMapping("/registro-finalizar")
    public String registrarUsuario(@ModelAttribute("usuario") Usuario usuario) {

        usuarioService.registrar(usuario);

        return "redirect:/";
    }

    // DASHBOARD
    @GetMapping("/usuario/inicio")
    public String mostrarDashboardInicio(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || usuario.getId() == null) {
            return "redirect:/"; 
        }
        model.addAttribute("usuario", usuario);
        return "usuario/inicio";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/usuario/perfil")
    public String mostrarPerfil() {
        return "usuario/perfil";
    }

}
