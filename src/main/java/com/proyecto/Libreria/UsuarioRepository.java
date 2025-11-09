package main.java.com.proyecto.Libreria;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tuempresa.biblioteca.usuario.model.Usuario;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByCorreo(String correo);
}
