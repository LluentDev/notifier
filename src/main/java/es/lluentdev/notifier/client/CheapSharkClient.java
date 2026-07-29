package es.lluentdev.notifier.client;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import es.lluentdev.notifier.model.CheapShark;

/**
 * Cliente REST para interactuar con la API de CheapShark.
 * 
 * @author LluentDev
 * @version 1.0
 */

@Component
public class CheapSharkClient {

    private final RestClient restClient;

    public CheapSharkClient() {
        // Configuramos la URL base del cliente REST
        this.restClient = RestClient.builder()
                .baseUrl("https://www.cheapshark.com/api/1.0")
                .defaultHeader("User-Agent", "NotifierBot/1.0 (es.lluentdev.notifier)")
                .defaultHeader("Accept", "application/json")
                .build();
    }

    /**
     * Obtiene la lista de ofertas gratuitas de CheapShark.
     * 
     * @return Lista de ofertas gratuitas de CheapShark.
     *         ParameterizedTypeReference se utiliza para manejar la deserialización
     *         de la respuesta JSON a una lista de objetos CheapShark.
     */
    public List<CheapShark> fetchFreeDeals() {
        return restClient.get()
                .uri("/deals?upperPrice=0")
                .retrieve()
                .body(new ParameterizedTypeReference<List<CheapShark>>() {
                });
    }
}
