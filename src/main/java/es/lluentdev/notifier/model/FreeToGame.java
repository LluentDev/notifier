package es.lluentdev.notifier.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Record para representar un juego gratuito en la API de FreeToGame.
 * 
 * @param id                El ID del juego.
 * @param title             El título del juego.
 * @param thumbnail         La URL de la imagen del juego.
 * @param short_description Una breve descripción del juego.
 * @param game_url          La URL del juego en FreeToGame.
 * @param genre             El género del juego.
 * @param platform          La plataforma del juego.
 * @param profileUrl        La URL del perfil del juego en FreeToGame.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record FreeToGame(
                long id,
                String title,
                String thumbnail,
                String short_description,
                String game_url,
                String genre,
                String platform,
                @JsonProperty("free_to_game_profile_url") String profileUrl) {
}