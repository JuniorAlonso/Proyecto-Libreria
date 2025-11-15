package com.proyecto.Libreria.service.impl;

import com.proyecto.Libreria.entidad.Usuario;
import com.proyecto.Libreria.service.UsuarioService;
import java.util.List;
import java.util.Optional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.proyecto.Libreria.repositorio.UsuarioRepositorio;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepositorio usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepositorio usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Usuario registrar(Usuario usuario) {
        if (usuario.getCorreo() != null) {
            usuario.setCorreo(usuario.getCorreo().toLowerCase().trim());
        }

        if (usuario.getContrasena() != null && !usuario.getContrasena().startsWith("$2a$")) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            usuario.setContrasena(encoder.encode(usuario.getContrasena().trim()));
        }

        return usuarioRepository.save(usuario);
    }

    @Override
    public Optional<Usuario> iniciarSesion(String correo, String contrasena) {
        Optional<Usuario> usuario = usuarioRepository.findByCorreo(correo.toLowerCase().trim());
        if (usuario.isPresent()) {
            String hashed = usuario.get().getContrasena();
            if (hashed != null) {
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                
                // Verificar si la contraseña esta encriptada
                if (hashed.startsWith("$2a$") || hashed.startsWith("$2b$") || hashed.startsWith("$2y$")) {
                    if (encoder.matches(contrasena.trim(), hashed.trim())) {
                        return usuario;
                    }
                } else {
                    if (contrasena.trim().equals(hashed.trim())) {
                        // Codifica la contraseña y guardar
                        usuario.get().setContrasena(encoder.encode(contrasena.trim()));
                        usuarioRepository.save(usuario.get());
                        return usuario;
                    }
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }
}
