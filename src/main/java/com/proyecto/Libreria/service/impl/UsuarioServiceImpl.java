package com.proyecto.Libreria.service.impl;

import com.proyecto.Libreria.model.Usuario;
import com.proyecto.Libreria.repository.UsuarioRepository;
import com.proyecto.Libreria.service.UsuarioService;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Usuario registrar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Override
    public Optional<Usuario> iniciarSesion(String correo, String contrasena) {
        Optional<Usuario> usuario = usuarioRepository.findByCorreo(correo.toLowerCase().trim());
        if (usuario.isPresent() && usuario.get().getContrasena().trim().equals(contrasena.trim())) {
            return usuario;
        }
        return Optional.empty();
    }

    @Override
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }
}
