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

    // REGISTRO 
    // formulario
    @GetMapping("/registro")
    public String mostrarRegistroPaso1(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "usuario/registro"; // registro.html
    }

    // recibe los datos del primer paso
    @PostMapping("/registro-pago")
    public String procesarRegistroPaso1(@ModelAttribute("usuario") Usuario usuario, Model model, HttpSession session) {
        // Guardar temporalmente en sesión
        session.setAttribute("usuarioTemporal", usuario);
        model.addAttribute("usuario", usuario);
        return "usuario/registro-pago"; 
    }

    // recibe datos de pago y finaliza el registro
    @PostMapping("/registro-finalizar")
    public String registrarUsuario(@ModelAttribute("usuario") Usuario usuario, HttpSession session, RedirectAttributes redirect) {
        // Recuperar datos del primer paso
        Usuario usuarioTemporal = (Usuario) session.getAttribute("usuarioTemporal");
        
        if (usuarioTemporal != null) {
            // Combinar datos
            usuarioTemporal.setNumeroTarjeta(usuario.getNumeroTarjeta());
            usuarioTemporal.setFechaExpiracion(usuario.getFechaExpiracion());
            usuarioTemporal.setCvv(usuario.getCvv());
            
            // Registrar usuario
            usuarioService.registrar(usuarioTemporal);
            
            // Limpiar sesión
            session.removeAttribute("usuarioTemporal");
            
            redirect.addFlashAttribute("success", "Registro exitoso. Ahora puedes iniciar sesión.");
        }

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
    public String mostrarPerfil(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || usuario.getId() == null) {
            return "redirect:/"; 
        }
        model.addAttribute("usuario", usuario);
        return "usuario/perfil";
    }

}
