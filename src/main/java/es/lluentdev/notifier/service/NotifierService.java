package es.lluentdev.notifier.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import es.lluentdev.notifier.client.CheapSharkClient;
import es.lluentdev.notifier.client.FreeToGameClient;
import es.lluentdev.notifier.client.GamerPowerClient;
import es.lluentdev.notifier.client.TelegramClient;
import es.lluentdev.notifier.model.CheapShark;
import es.lluentdev.notifier.model.FreeToGame;
import es.lluentdev.notifier.model.GiveAway;

/**
 * Servicio principal de notificaciones que integra los clientes de las APIs y
 * envía mensajes a Telegram.
 * 
 * @author LluentDev
 * @version 1.0
 */

@Service
public class NotifierService {

    private final GamerPowerClient gamerPowerClient;
    private final CheapSharkClient cheapSharkClient;
    private final TelegramClient telegramClient;
    private final FreeToGameClient freeToGameClient;

    // Set para almacenar los identificadores de los juegos ya notificados
    private final Set<String> notifiedDealKeys = new HashSet<>();

    public NotifierService(GamerPowerClient gamerPowerClient,
            CheapSharkClient cheapSharkClient,
            TelegramClient telegramClient,
            FreeToGameClient freeToGameClient) {
        this.gamerPowerClient = gamerPowerClient;
        this.cheapSharkClient = cheapSharkClient;
        this.telegramClient = telegramClient;
        this.freeToGameClient = freeToGameClient;
    }

    /**
     * Se ejecuta automáticamente SOLO UNA VEZ al arrancar la aplicación.
     * Carga todos los juegos actuales en la memoria SIN enviar mensajes a Telegram
     * para evitar SPAM al reiniciar la app en Render.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void initCacheOnStartup() {
        System.out.println("🚀 Carga inicial silenciosa: memorizando ofertas existentes...");
        checkAndNotifyNewGames(true); // true = modo silencioso
        System.out.println(
                "✅ Memorización completada. Se ignorarán los " + notifiedDealKeys.size() + " juegos actuales.");
        String welcomeMessage = """
                🤖 <b>¡Game Notifier activo y listo!</b>

                Servidor iniciado correctamente en la nube.
                Actualmente monitorizando <b>%d</b> ofertas activas.

                <i>Las nuevas promociones y juegos gratis se publicarán automáticamente aquí.</i>
                """.formatted(notifiedDealKeys.size());

        telegramClient.sendMessage(welcomeMessage);
    }

    public List<GiveAway> getActiveGames() {
        return gamerPowerClient.fetchPcGiveaways().stream()
                .filter(g -> "Game".equalsIgnoreCase(g.type()))
                .filter(g -> "Active".equalsIgnoreCase(g.status()))
                .toList();
    }

    /**
     * Sobrecarga sin parámetros para mantener compatibilidad con el Scheduler.
     */
    public void checkAndNotifyNewGames() {
        checkAndNotifyNewGames(false);
    }

    /**
     * Verifica los juegos activos de las APIs integradas.
     * 
     * @param silent Si es true, añade las claves al Set pero NO envía
     *               notificaciones a Telegram.
     */
    public void checkAndNotifyNewGames(boolean silent) {
        // 1. GamerPower
        try {
            List<GiveAway> gamerPowerGames = getActiveGames();
            if (gamerPowerGames != null) {
                for (GiveAway game : gamerPowerGames) {
                    String key = "GP_" + game.id();
                    if (!notifiedDealKeys.contains(key)) {
                        if (!silent) {
                            telegramClient.sendGiveawayNotification(game);
                            System.out.println("✅ Notificado (GamerPower): " + game.title());
                        }
                        notifiedDealKeys.add(key);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("⚠️ Error consultando GamerPower: " + e.getMessage());
        }

        // 2. CheapShark
        try {
            List<CheapShark> cheapSharkDeals = cheapSharkClient.fetchFreeDeals();
            for (CheapShark deal : cheapSharkDeals) {
                String key = "CS_" + deal.dealID();
                if (!notifiedDealKeys.contains(key)) {
                    if (!silent) {
                        telegramClient.sendCheapSharkNotification(deal);
                        System.out.println("✅ Notificado (CheapShark): " + deal.title());
                    }
                    notifiedDealKeys.add(key);
                }
            }
        } catch (Exception e) {
            System.err.println("⚠️ Error en el procesamiento de CheapShark: " + e.getMessage());
        }

        // 3. FreeToGame
        try {
            List<FreeToGame> ftgGames = freeToGameClient.fetchLatestPcGames().stream()
                    .limit(10)
                    .toList();

            for (FreeToGame game : ftgGames) {
                String key = "FTG_" + game.id();
                if (!notifiedDealKeys.contains(key)) {
                    if (!silent) {
                        telegramClient.sendFreeToGameNotification(game);
                        System.out.println("✅ Notificado (FreeToGame): " + game.title());
                        Thread.sleep(1000); // Pausa para no saturar la API de Telegram
                    }
                    notifiedDealKeys.add(key);
                }
            }
        } catch (InterruptedException e) {
            System.err.println("⚠️ Error consultando FreeToGame: " + e.getMessage());
        }
    }
}