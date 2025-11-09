package com.proyecto.Libreria.controller;

import com.proyecto.Libreria.model.Usuario;
import com.proyecto.Libreria.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/login")
    public String mostrarLogin() {
        return "usuario/login";
    }

    @GetMapping("/registro")
    public String mostrarRegistro() {
        return "usuario/registro";
    }

    @PostMapping("/registrar")
    public String registrarUsuario(@ModelAttribute Usuario usuario) {
        usuarioService.registrar(usuario);
        return "redirect:/usuario/login";
    }

    @PostMapping("/iniciar")
    public String iniciarSesion(@RequestParam String correo, @RequestParam String contraseña, Model model) {
        var usuario = usuarioService.iniciarSesion(correo, contraseña);
        if (usuario.isPresent()) {
            model.addAttribute("usuario", usuario.get());
            return "usuario/perfil";
        } else {
            model.addAttribute("error", "Correo o contraseña incorrectos");
            return "usuario/login";
        }
    }
}
