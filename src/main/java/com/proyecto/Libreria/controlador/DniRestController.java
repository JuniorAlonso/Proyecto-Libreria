package com.proyecto.Libreria.controlador;

import com.proyecto.Libreria.entidad.DniResponse;
import com.proyecto.Libreria.service.DNIVAlidarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController // Indica que esta clase es un controlador REST que devuelve datos JSON
@RequestMapping("/api/dni") // Define el path base: /api/dni/...
public class DniRestController {

    private static final Logger logger = LoggerFactory.getLogger(DniRestController.class);

    @Autowired
    private DNIVAlidarService dniValidadorService;

    // Endpoint para consultar el DNI: GET /api/dni/{dni}
    @GetMapping("/{dni}")
    public ResponseEntity<?> consultarDni(@PathVariable String dni) {

        logger.info("Recibida petición para consultar DNI: {}", dni);

        // Validación básica del DNI
        if (dni == null || dni.trim().isEmpty()) {
            logger.warn("DNI vacío o nulo recibido");
            Map<String, String> error = new HashMap<>();
            error.put("error", "DNI inválido");
            error.put("message", "El DNI no puede estar vacío");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        // Llama al servicio que se conecta con la API externa
        DniResponse datosPersona = dniValidadorService.consultarPorDni(dni);

        // Verifica si se obtuvieron datos válidos
        if (datosPersona != null && datosPersona.getNombres() != null) {
            logger.info("DNI encontrado exitosamente: {}", dni);
            // Retorna los datos con código 200 OK
            return new ResponseEntity<>(datosPersona, HttpStatus.OK);
        } else {
            logger.warn("No se encontraron datos para el DNI: {}", dni);
            // Retorna un error 404 Not Found con mensaje descriptivo
            Map<String, String> error = new HashMap<>();
            error.put("error", "DNI no encontrado");
            error.put("message",
                    "No se encontraron datos para el DNI proporcionado. Verifica que el DNI sea correcto o que el servicio de API esté funcionando.");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }
}