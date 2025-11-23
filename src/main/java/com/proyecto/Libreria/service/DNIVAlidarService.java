package com.proyecto.Libreria.service;

// 1. IMPORTS NECESARIOS
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyecto.Libreria.entidad.DniResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Service // <-- ANOTACIÓN CLAVE: Indica a Spring que es un Bean de Servicio
public class DNIVAlidarService {

    private static final Logger logger = LoggerFactory.getLogger(DNIVAlidarService.class);

    // 2. INYECCIÓN DE PROPIEDADES (usando las claves de application.properties)
    @Value("${api.peru.token}")
    private String apiToken;

    @Value("${api.peru.url}")
    private String apiUrlBase;

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    // 3. CONSTRUCTOR
    // Inicializa HttpClient y ObjectMapper para hacer peticiones HTTP y parsear
    // JSON
    public DNIVAlidarService() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.objectMapper = new ObjectMapper();
    }

    // 4. MÉTODO DE CONSULTA
    public DniResponse consultarPorDni(String dni) {

        // Construye la URL final con el endpoint correcto de API Peru
        String urlConsulta = apiUrlBase + "/dni/" + dni;

        logger.info("Iniciando consulta de DNI: {}", dni);
        logger.debug("URL de consulta: {}", urlConsulta);
        logger.debug("Token (primeros 10 caracteres): {}...",
                apiToken != null && apiToken.length() > 10 ? apiToken.substring(0, 10) : "TOKEN_VACIO");

        try {
            // Construye la petición HTTP con Bearer token
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlConsulta))
                    .header("Accept", "application/json")
                    .header("Authorization", "Bearer " + apiToken)
                    .timeout(Duration.ofSeconds(10))
                    .GET()
                    .build();

            logger.debug("Enviando petición HTTP a la API...");

            // Realiza la solicitud y obtiene la respuesta
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            logger.info("API respondió con código de estado: {}", response.statusCode());
            logger.debug("Cuerpo de respuesta: {}", response.body());

            if (response.statusCode() == 200) {
                // Parsea el JSON a DniResponse
                DniResponse dniResponse = objectMapper.readValue(response.body(), DniResponse.class);
                logger.info("DNI consultado exitosamente: {} - {}", dni, dniResponse.getNombreCompleto());
                return dniResponse;

            } else if (response.statusCode() == 401) {
                logger.error("Error de autenticación (401): Token inválido o expirado");
                logger.error("Respuesta completa: {}", response.body());
                return null;

            } else if (response.statusCode() == 404) {
                logger.warn("DNI no encontrado en RENIEC (404): {}", dni);
                return null;

            } else {
                logger.error("API DNI respondió con código de estado inesperado: {}", response.statusCode());
                logger.error("Respuesta: {}", response.body());
                return null;
            }

        } catch (java.net.http.HttpTimeoutException e) {
            logger.error("Timeout al conectar con la API de DNI: {}", e.getMessage());
            return null;

        } catch (java.io.IOException e) {
            logger.error("Error de conexión con la API de DNI: {}", e.getMessage());
            e.printStackTrace();
            return null;

        } catch (Exception e) {
            logger.error("Error inesperado al consultar DNI: {}", e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}