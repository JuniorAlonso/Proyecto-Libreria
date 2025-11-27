package com.proyecto.Libreria.controlador;

import com.proyecto.Libreria.entidad.Usuario;
import com.proyecto.Libreria.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus; // Importar SessionStatus
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UsuarioControlador {

    private final UsuarioService usuarioService;

    public UsuarioControlador(UsuarioService usuarioService) {
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
            Usuario u = usuario.get();
            session.setAttribute("usuario", u);

            if ("ADMIN".equalsIgnoreCase(u.getRol())) {
                return "redirect:/admin/dashboard";
            } else if ("BIBLIOTECARIO".equalsIgnoreCase(u.getRol())) {
                return "redirect:/dashboardBibliotecario";
            } else {
                return "redirect:/usuario/inicio";
            }
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
        return "usuario/suscripcion";
    }

    // PÁGINA DE SELECCIÓN DE PLANES
    @GetMapping("/suscripcion")
    public String mostrarPlanesSuscripcion() {
        return "usuario/suscripcion";
    }

    // REGISTRO - PASO 2 (Muestra el formulario de pago con el plan seleccionado)
    @GetMapping("/registro-pago")
    public String mostrarRegistroPaso2(@RequestParam String plan, Model model, HttpSession session,
            RedirectAttributes redirect) {
        Usuario usuarioTemporal = (Usuario) session.getAttribute("usuarioTemporal");

        if (usuarioTemporal == null) {
            redirect.addFlashAttribute("error", "Por favor, complete primero sus datos personales.");
            return "redirect:/registro";
        }

        model.addAttribute("planSeleccionado", plan);
        model.addAttribute("usuario", new Usuario()); // Objeto para el formulario de pago
        return "usuario/registro-pago";
    }

    // recibe datos de pago y finaliza el registro
    @PostMapping("/registro-finalizar")
    public String registrarUsuario(@ModelAttribute("usuario") Usuario usuario, HttpSession session,
            RedirectAttributes redirect) {
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
