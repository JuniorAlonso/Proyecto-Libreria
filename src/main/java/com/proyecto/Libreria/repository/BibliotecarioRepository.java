package com.proyecto.Libreria.repository;

import com.proyecto.Libreria.model.Bibliotecario;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public class BibliotecarioRepository {

    private final Map<String, Bibliotecario> bibliotecarios = new HashMap<>();

    public BibliotecarioRepository() {

        bibliotecarios.put("admin", new Bibliotecario("admin", "password123"));
        bibliotecarios.put("bibliotecario1", new Bibliotecario("bibliotecario1", "pass456"));
    }

    public Bibliotecario findByUsuario(String usuario) {
        return bibliotecarios.get(usuario);
    }

    // Método para simular la validación de credenciales
    public boolean validarCredenciales(String usuario, String contrasena) {
        Bibliotecario bibliotecario = findByUsuario(usuario);
        return bibliotecario != null && bibliotecario.getContrasena().equals(contrasena);
    }
}

