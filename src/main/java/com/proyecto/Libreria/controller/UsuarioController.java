package com.proyecto.Libreria.controller;

import com.proyecto.Libreria.model.Usuario;
import com.proyecto.Libreria.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // --- 1. LOGIN ---
    
    // Muestra la página de login (index.html o login.html)
    // Se accede por la URL raíz (http://localhost:8080/)
    @GetMapping("/")
    public String mostrarLogin(Model model) {
        // Renombra tu index.html a login.html si no lo has hecho
        return "usuario/index";
    }

    @PostMapping("/iniciar-sesion") // Cambio de /iniciar a /iniciar-sesion para más claridad
    public String iniciarSesion(@RequestParam String correo, @RequestParam String contraseña, 
                                RedirectAttributes redirect, Model model) {
        
        var usuario = usuarioService.iniciarSesion(correo, contraseña);
        
        if (usuario.isPresent()) {
            // Éxito: Redirigimos al dashboard principal del usuario
            // Usamos redirect: para evitar problemas de caché y refresco
            model.addAttribute("usuario", usuario.get()); // Opcional, mejor usar HttpSession o Spring Security
            return "redirect:/usuario/inicio"; 
        } else {
            // Fallo: Volvemos al login con mensaje de error
            redirect.addFlashAttribute("error", "Correo o contraseña incorrectos");
            return "redirect:/"; // Redirige a la ruta principal que muestra el login
        }
    }
    
    // --- 2. REGISTRO (Flujo de 2 Pasos) ---
    
    // Paso 1: Muestra el formulario de datos personales
    @GetMapping("/registro")
    public String mostrarRegistroPaso1(Model model) {
        model.addAttribute("usuario", new Usuario()); // Objeto para el formulario
        return "usuario/registro"; // Corresponde a registro.html (Parte 1)
    }

    // Paso 2: Procesa el formulario de datos personales y muestra el formulario de pago
    @PostMapping("/registro-pago")
    public String procesarRegistroPaso1(@ModelAttribute Usuario usuario, Model model) {
        // Aquí guardarías temporalmente el objeto 'usuario' en la sesión HTTP
        // Para este ejemplo simple, solo pasamos el objeto a la siguiente vista
        model.addAttribute("usuario", usuario); 
        return "usuario/registro-pago"; // Corresponde a registro-pago.html (Parte 2)
    }

    // Paso 3: Procesa el formulario de pago y finaliza el registro
    @PostMapping("/registro-finalizar")
    public String registrarUsuario(@ModelAttribute Usuario usuario) {
        // En un caso real, recuperarías el objeto de la sesión, añadirías los datos de pago
        // y luego llamarías al servicio para persistir todo.
        usuarioService.registrar(usuario); // Guardar en la base de datos
        return "redirect:/"; // Vuelve a la página de login después de registrar
    }

    // --- 3. RUTAS DEL DASHBOARD (Una vez logueado) ---
    
    // La página principal después del login (Bienvenida/Términos y Condiciones)
    @GetMapping("/usuario/inicio")
    public String mostrarDashboardInicio() {
        // Esto cargará el menú lateral (usuario.html) y el contenido (inicio.html)
        return "usuario/inicio"; 
    }

    // Rutas para las demás secciones del menú (Ejemplo)
    @GetMapping("/usuario/biblioteca")
    public String mostrarBiblioteca() {
        return "usuario/biblioteca";
    }

    @GetMapping("/usuario/perfil")
    public String mostrarPerfil() {
        return "usuario/perfil";
    }
}