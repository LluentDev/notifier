package es.lluentdev.notifier.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Record para representar un juego en oferta en la API de CheapShark.
 * 
 * @param title       El título del juego.
 * @param dealID      El ID de la oferta.
 * @param normalPrice El precio normal del juego.
 * @param thumb       La URL de la imagen del juego.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record CheapShark(
        String title,
        String dealID,
        String normalPrice,
        String thumb) {
}