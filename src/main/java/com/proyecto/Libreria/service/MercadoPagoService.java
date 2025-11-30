package com.proyecto.Libreria.service;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.resources.preference.Preference;
import com.proyecto.Libreria.entidad.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class MercadoPagoService {

    @Value("${mercadopago.access.token}")
    private String accessToken;

    public Preference crearPreferenciaMembresia(Usuario usuario) {
        MercadoPagoConfig.setAccessToken(accessToken);

        PreferenceClient client = new PreferenceClient();

        List<PreferenceItemRequest> items = new ArrayList<>();
        PreferenceItemRequest item = PreferenceItemRequest.builder()
                .title("Membresía Mensual - Librería")
                .quantity(1)
                .unitPrice(new BigDecimal("10.00")) // Precio fijo
                .currencyId("PEN")
                .build();
        items.add(item);

        PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                .success("http://localhost:8083/suscripcion/exito")
                .failure("http://localhost:8083/suscripcion/fallo")
                .pending("http://localhost:8083/suscripcion/pendiente")
                .build();

        PreferenceRequest request = PreferenceRequest.builder()
                .items(items)
                .backUrls(backUrls)
                .autoReturn("approved")
                .externalReference(usuario.getId().toString()) // ID del usuario para referencia
                .build();

        try {
            return client.create(request);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
