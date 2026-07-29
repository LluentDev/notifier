package es.lluentdev.notifier.client;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import es.lluentdev.notifier.model.GiveAway;

/**
 * Cliente REST para interactuar con la API de GamerPower.
 * 
 * @author LluentDev
 * @version 1.0
 */

@Component
public class GamerPowerClient {

    private final RestClient restClient;

    public GamerPowerClient() {
        // Configuramos la URL base del cliente REST
        this.restClient = RestClient.builder()
                .baseUrl("https://www.gamerpower.com/api")
                .build();
    }

    /**
     * Obtiene la lista de giveaways disponibles para PC desde la API de GamerPower.
     * 
     * @return Lista de objetos GiveAway representando los giveaways.
     *         ParametrizedTypeReference se utiliza para manejar la deserialización
     *         de la respuesta JSON a una lista de objetos GiveAway.
     */
    public List<GiveAway> fetchPcGiveaways() {
        return restClient.get()
                .uri("/giveaways?platform=pc")
                .retrieve()
                .body(new ParameterizedTypeReference<List<GiveAway>>() {
                });
    }

}
