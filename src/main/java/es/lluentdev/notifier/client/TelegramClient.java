package es.lluentdev.notifier.client;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import es.lluentdev.notifier.model.CheapShark;
import es.lluentdev.notifier.model.FreeToGame;
import es.lluentdev.notifier.model.GiveAway;

/**
 * Cliente REST para interactuar con la API de Telegram.
 * 
 * @author LluentDev
 * @version 1.0
 */
@Component
public class TelegramClient {

        private final RestClient restClient;
        private final String botToken;
        private final String chatId;

        // Inyectamos las propiedades definidas en application.properties
        public TelegramClient(
                        @Value("${telegram.bot.token}") String botToken,
                        @Value("${telegram.chat.id}") String chatId) {
                this.restClient = RestClient.create();
                this.botToken = botToken;
                this.chatId = chatId;
        }

        /**
         * Envía una notificación a Telegram con los detalles del juego.
         * 
         * @param game El objeto GiveAway que contiene la información del juego.
         */
        public void sendGiveawayNotification(GiveAway game) {

                String caption = """
                                🎮 <b>¡NUEVA OFERTA DISPONIBLE!</b>

                                <b>%s</b>

                                📝 %s

                                💰 <b>Valor:</b> %s
                                💻 <b>Plataforma:</b> %s

                                🔗 <a href="%s">Reclamar juego aquí</a>
                                """.formatted(
                                game.title(),
                                game.description(),
                                game.worth(),
                                game.platform(),
                                game.openGiveawayUrl());

                Map<String, Object> payload = Map.of(
                                "chat_id", chatId,
                                "photo", game.image(),
                                "caption", caption,
                                "parse_mode", "HTML");

                String telegramUrl = "https://api.telegram.org/bot" + botToken + "/sendPhoto";

                restClient.post()
                                .uri(telegramUrl)
                                .header("Content-Type", "application/json")
                                .body(payload)
                                .retrieve()
                                .toBodilessEntity();
        }

        /**
         * Envía una notificación a Telegram con los detalles del juego detectado en
         * CheapShark.
         * 
         * @param deal El objeto CheapShark que contiene los detalles del juego en
         *             oferta.
         */
        public void sendCheapSharkNotification(CheapShark deal) {
                String dealUrl = "https://www.cheapshark.com/redirect?dealID=" + deal.dealID();

                String caption = """
                                🎮 <b>¡CHOLLO GRATIS DETECTADO (CheapShark)!</b>

                                <b>%s</b>

                                💰 <b>Precio Original:</b> $%s (Ahora GRATIS)

                                🔗 <a href="%s">Reclamar oferta aquí</a>
                                """.formatted(
                                deal.title(),
                                deal.normalPrice(),
                                dealUrl);

                Map<String, Object> payload = Map.of(
                                "chat_id", chatId,
                                "photo", deal.thumb(),
                                "caption", caption,
                                "parse_mode", "HTML");

                String telegramUrl = "https://api.telegram.org/bot" + botToken + "/sendPhoto";

                restClient.post()
                                .uri(telegramUrl)
                                .header("Content-Type", "application/json")
                                .body(payload)
                                .retrieve()
                                .toBodilessEntity();
        }

        /**
         * Envía una notificación a Telegram con los detalles del juego detectado en
         * FreeToGame.
         * 
         * @param deal El objeto FreeToGame que contiene los detalles del juego en
         *             oferta.
         */
        public void sendFreeToGameNotification(FreeToGame deal) {
                String dealUrl = "https://www.freetogame.com/game/" + deal.id();

                String caption = """
                                🎮 <b>¡CHOLLO GRATIS DETECTADO (FreeToGame)!</b>

                                <b>%s</b>

                                💰 <b>Precio Original:</b> $%s (Ahora GRATIS)

                                🔗 <a href="%s">Reclamar oferta aquí</a>
                                """.formatted(
                                deal.title(),
                                deal.platform(),
                                deal.short_description(),
                                dealUrl);

                Map<String, Object> payload = Map.of(
                                "chat_id", chatId,
                                "photo", deal.thumbnail(),
                                "caption", caption,
                                "parse_mode", "HTML");

                String telegramUrl = "https://api.telegram.org/bot" + botToken + "/sendPhoto";

                restClient.post()
                                .uri(telegramUrl)
                                .header("Content-Type", "application/json")
                                .body(payload)
                                .retrieve()
                                .toBodilessEntity();
        }

        /**
         * Envía un mensaje de texto plano al canal o chat configurado.
         * 
         * @param message Texto a enviar.
         */
        public void sendMessage(String message) {
                String telegramUrl = "https://api.telegram.org/bot" + botToken + "/sendMessage";

                Map<String, Object> payload = Map.of(
                                "chat_id", chatId,
                                "text", message,
                                "parse_mode", "HTML");

                try {
                        restClient.post()
                                        .uri(telegramUrl)
                                        .header("Content-Type", "application/json")
                                        .body(payload)
                                        .retrieve()
                                        .toBodilessEntity();
                } catch (Exception e) {
                        System.err.println("⚠️ Error enviando mensaje a Telegram: " + e.getMessage());
                }
        }
}