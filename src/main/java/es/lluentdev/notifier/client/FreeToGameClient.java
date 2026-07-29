package es.lluentdev.notifier.client;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import es.lluentdev.notifier.model.FreeToGame;

/**
 * Cliente REST para interactuar con la API de FreeToGame.
 * 
 * @author LluentDev
 * @version 1.0
 */

@Component
public class FreeToGameClient {

    private final RestClient restClient;

    public FreeToGameClient() {
        this.restClient = RestClient.builder()
                .baseUrl("https://www.freetogame.com/api")
                .build();
    }

    /**
     * Obtiene los últimos juegos gratuitos para PC desde la API de FreeToGame.
     * 
     * @return Lista de objetos FreeToGame representando los juegos gratuitos.
     *         ParametrizedTypeReference se utiliza para manejar la deserialización
     *         de la respuesta JSON a una lista de objetos FreeToGame.
     */
    public List<FreeToGame> fetchLatestPcGames() {
        return restClient.get()
                .uri("/games?platform=pc&sort-by=release-date")
                .retrieve()
                .body(new ParameterizedTypeReference<List<FreeToGame>>() {
                });
    }

}
