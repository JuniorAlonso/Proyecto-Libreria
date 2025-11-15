package com.proyecto.Libreria.service;

import com.proyecto.Libreria.entidad.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    Usuario registrar(Usuario usuario);
    Optional<Usuario> iniciarSesion(String correo, String contrasena);
    List<Usuario> listarUsuarios();
}
