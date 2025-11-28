package com.proyecto.Libreria.service.impl;

import com.proyecto.Libreria.entidad.Usuario;
import com.proyecto.Libreria.service.LogAuditoriaService;
import com.proyecto.Libreria.service.UsuarioService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.proyecto.Libreria.repositorio.UsuarioRepositorio;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepositorio usuarioRepository;
    private final LogAuditoriaService logAuditoriaService;

    public UsuarioServiceImpl(UsuarioRepositorio usuarioRepository, LogAuditoriaService logAuditoriaService) {
        this.usuarioRepository = usuarioRepository;
        this.logAuditoriaService = logAuditoriaService;
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

        if (usuario.getActivo() == null) {
            usuario.setActivo(true);
        }

        if (usuario.getFechaCreacion() == null) {
            usuario.setFechaCreacion(LocalDateTime.now());
        }

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        // Registrar log de creación
        logAuditoriaService.registrarLog(
                usuario.getId(),
                usuario.getNombreCompleto(),
                "CREAR",
                "USUARIO",
                usuarioGuardado.getId(),
                "Usuario creado: " + usuarioGuardado.getCorreo() + " con rol " + usuarioGuardado.getRol());

        return usuarioGuardado;
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
                        // Solo registrar log si el usuario está activo
                        if (usuario.get().getActivo() != null && usuario.get().getActivo()) {
                            logAuditoriaService.registrarLog(
                                    usuario.get().getId(),
                                    usuario.get().getNombreCompleto(),
                                    "LOGIN",
                                    "USUARIO",
                                    usuario.get().getId(),
                                    "Inicio de sesion exitoso");
                        }
                        return usuario;
                    }
                } else {
                    if (contrasena.trim().equals(hashed.trim())) {
                        // Codifica la contraseña y guardar
                        usuario.get().setContrasena(encoder.encode(contrasena.trim()));
                        usuarioRepository.save(usuario.get());

                        // Solo registrar log si el usuario está activo
                        if (usuario.get().getActivo() != null && usuario.get().getActivo()) {
                            logAuditoriaService.registrarLog(
                                    usuario.get().getId(),
                                    usuario.get().getNombreCompleto(),
                                    "LOGIN",
                                    "USUARIO",
                                    usuario.get().getId(),
                                    "Inicio de sesion exitoso");
                        }
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

    @Override
    public Optional<Usuario> obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public Usuario actualizarUsuario(Long id, Usuario usuario, String modificadoPor) {
        Optional<Usuario> usuarioExistente = usuarioRepository.findById(id);
        if (usuarioExistente.isPresent()) {
            Usuario usuarioActualizar = usuarioExistente.get();

            // Guardar valores anteriores para el log
            String valoresAnteriores = String.format("Rol: %s, Activo: %s",
                    usuarioActualizar.getRol(), usuarioActualizar.getActivo());

            // Actualizar campos
            if (usuario.getNombreCompleto() != null) {
                usuarioActualizar.setNombreCompleto(usuario.getNombreCompleto());
            }
            if (usuario.getPrimerApellido() != null) {
                usuarioActualizar.setPrimerApellido(usuario.getPrimerApellido());
            }
            if (usuario.getCorreo() != null) {
                usuarioActualizar.setCorreo(usuario.getCorreo().toLowerCase().trim());
            }
            if (usuario.getRol() != null) {
                usuarioActualizar.setRol(usuario.getRol());
            }
            if (usuario.getTelefono() != null) {
                usuarioActualizar.setTelefono(usuario.getTelefono());
            }
            if (usuario.getContrasena() != null && !usuario.getContrasena().isEmpty()) {
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                usuarioActualizar.setContrasena(encoder.encode(usuario.getContrasena().trim()));
            }

            usuarioActualizar.setFechaModificacion(LocalDateTime.now());
            usuarioActualizar.setModificadoPor(modificadoPor);

            Usuario usuarioGuardado = usuarioRepository.save(usuarioActualizar);

            // Registrar log de modificación
            String valoresNuevos = String.format("Rol: %s, Activo: %s",
                    usuarioGuardado.getRol(), usuarioGuardado.getActivo());
            logAuditoriaService.registrarLog(
                    null,
                    modificadoPor,
                    "MODIFICAR",
                    "USUARIO",
                    usuarioGuardado.getId(),
                    "Usuario modificado: " + usuarioGuardado.getCorreo() + ". Antes: " + valoresAnteriores
                            + " | Despues: " + valoresNuevos);

            return usuarioGuardado;
        }
        throw new RuntimeException("Usuario no encontrado");
    }

    @Override
    public void desactivarUsuario(Long id, String modificadoPor) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (usuario.isPresent()) {
            Usuario usuarioDesactivar = usuario.get();
            usuarioDesactivar.setActivo(false);
            usuarioDesactivar.setFechaModificacion(LocalDateTime.now());
            usuarioDesactivar.setModificadoPor(modificadoPor);
            usuarioRepository.save(usuarioDesactivar);

            // Registrar log de desactivación
            logAuditoriaService.registrarLog(
                    null,
                    modificadoPor,
                    "DESACTIVAR",
                    "USUARIO",
                    usuarioDesactivar.getId(),
                    "Usuario desactivado: " + usuarioDesactivar.getCorreo() + " (" + usuarioDesactivar.getRol() + ")");
        }
    }

    @Override
    public void activarUsuario(Long id, String modificadoPor) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (usuario.isPresent()) {
            Usuario usuarioActivar = usuario.get();
            usuarioActivar.setActivo(true);
            usuarioActivar.setFechaModificacion(LocalDateTime.now());
            usuarioActivar.setModificadoPor(modificadoPor);
            usuarioRepository.save(usuarioActivar);

            // Registrar log de activación
            logAuditoriaService.registrarLog(
                    null,
                    modificadoPor,
                    "ACTIVAR",
                    "USUARIO",
                    usuarioActivar.getId(),
                    "Usuario activado: " + usuarioActivar.getCorreo() + " (" + usuarioActivar.getRol() + ")");
        }
    }

    @Override
    public void eliminarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    public List<Usuario> listarUsuariosActivos() {
        return usuarioRepository.findAll().stream()
                .filter(u -> u.getActivo() != null && u.getActivo())
                .collect(Collectors.toList());
    }

    @Override
    public List<Usuario> listarUsuariosPorRol(String rol) {
        return usuarioRepository.findAll().stream()
                .filter(u -> rol.equalsIgnoreCase(u.getRol()))
                .collect(Collectors.toList());
    }
}
