package com.proyecto.Libreria;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration; 

// Añadir la exclusión
@SpringBootApplication(exclude = {R2dbcAutoConfiguration.class})
public class LibreriaApplication {
    public static void main(String[] args) {
        SpringApplication.run(LibreriaApplication.class, args);
    }
}