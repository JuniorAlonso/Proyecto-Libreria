package com.proyecto.Libreria.controlador;

import com.proyecto.Libreria.entidad.Usuario;
import com.proyecto.Libreria.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioRestControlador {

    private final UsuarioService usuarioService;

    public UsuarioRestControlador(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> obtenerTodosLosUsuarios() {
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }

    @GetMapping("/activos")
    public ResponseEntity<List<Usuario>> obtenerUsuariosActivos() {
        return ResponseEntity.ok(usuarioService.listarUsuariosActivos());
    }

    @GetMapping("/rol/{rol}")
    public ResponseEntity<List<Usuario>> obtenerUsuariosPorRol(@PathVariable String rol) {
        return ResponseEntity.ok(usuarioService.listarUsuariosPorRol(rol));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerUsuarioPorId(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.obtenerUsuarioPorId(id);
        return usuario.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario, HttpSession session) {
        try {
            Usuario usuarioSesion = (Usuario) session.getAttribute("usuario");
            if (usuarioSesion != null) {
                usuario.setModificadoPor(usuarioSesion.getNombreCompleto());
            }
            Usuario nuevoUsuario = usuarioService.registrar(usuario);
            return ResponseEntity.ok(nuevoUsuario);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuario,
            HttpSession session) {
        try {
            Usuario usuarioSesion = (Usuario) session.getAttribute("usuario");
            String modificadoPor = usuarioSesion != null ? usuarioSesion.getNombreCompleto() : "Sistema";

            Usuario usuarioActualizado = usuarioService.actualizarUsuario(id, usuario, modificadoPor);
            return ResponseEntity.ok(usuarioActualizado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<?> desactivarUsuario(@PathVariable Long id, HttpSession session) {
        try {
            Usuario usuarioSesion = (Usuario) session.getAttribute("usuario");
            String modificadoPor = usuarioSesion != null ? usuarioSesion.getNombreCompleto() : "Sistema";

            usuarioService.desactivarUsuario(id, modificadoPor);
            return ResponseEntity.ok(Map.of("mensaje", "Usuario desactivado exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PatchMapping("/{id}/activar")
    public ResponseEntity<?> activarUsuario(@PathVariable Long id, HttpSession session) {
        try {
            Usuario usuarioSesion = (Usuario) session.getAttribute("usuario");
            String modificadoPor = usuarioSesion != null ? usuarioSesion.getNombreCompleto() : "Sistema";

            usuarioService.activarUsuario(id, modificadoPor);
            return ResponseEntity.ok(Map.of("mensaje", "Usuario activado exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id) {
        try {
            usuarioService.eliminarUsuario(id);
            return ResponseEntity.ok(Map.of("mensaje", "Usuario eliminado exitosamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
