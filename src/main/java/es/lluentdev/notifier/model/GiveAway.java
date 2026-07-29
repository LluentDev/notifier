package es.lluentdev.notifier.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Record para representar un juego en la API de GiveAway.
 * 
 * @param id              El ID del juego.
 * @param title           El título del juego.
 * @param worth           El valor del juego.
 * @param description     La descripción del juego.
 * @param platform        La plataforma del juego.
 * @param image           La URL de la imagen del juego.
 * @param type            El tipo de juego.
 * @param status          El estado del juego.
 * @param openGiveawayUrl La URL para abrir el juego en GiveAway.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record GiveAway(
        long id,
        String title,
        String worth,
        String description,
        String platform,
        String image,
        String type,
        String status,
        @JsonProperty("open_giveaway_url") String openGiveawayUrl) {

}
