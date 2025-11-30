package com.proyecto.Libreria.service;

import com.proyecto.Libreria.entidad.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    Usuario registrar(Usuario usuario);

    Optional<Usuario> iniciarSesion(String correo, String contrasena);

    List<Usuario> listarUsuarios();

    Optional<Usuario> obtenerUsuarioPorId(Long id);

    Usuario actualizarUsuario(Long id, Usuario usuario, String modificadoPor);

    void desactivarUsuario(Long id, String modificadoPor);

    void activarUsuario(Long id, String modificadoPor);

    void eliminarUsuario(Long id);

    List<Usuario> listarUsuariosActivos();

    List<Usuario> listarUsuariosPorRol(String rol);

    void renovarMembresia(Long id);
}
