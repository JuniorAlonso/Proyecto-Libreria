package com.proyecto.Libreria.service.impl;

import com.proyecto.Libreria.model.Usuario;
import com.proyecto.Libreria.repository.UsuarioRepository;
import com.proyecto.Libreria.service.UsuarioService;
import java.util.List;
import java.util.Optional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
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
                // Intentamos comparar usando BCrypt. Si el valor almacenado no tiene formato BCrypt
                // (por ejemplo, usuarios antiguos con contraseña en texto plano), matches() lanzará
                // IllegalArgumentException; en ese caso hacemos un fallback a comparación en texto
                // plano y, si coincide, re-hasheamos y guardamos el nuevo hash (migración automática).
                try {
                    if (encoder.matches(contrasena.trim(), hashed.trim())) {
                        return usuario;
                    }
                } catch (IllegalArgumentException ex) {
                    // Valor almacenado no es un hash BCrypt — intentamos comparar en texto plano
                    if (contrasena.trim().equals(hashed.trim())) {
                        // Migrar a BCrypt: codificar la contraseña y guardar
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
