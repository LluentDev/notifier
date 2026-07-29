package es.lluentdev.notifier.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    // Set para almacenar los identificadores de los juegos ya notificados y evitar
    // duplicados.
    private final Set<String> notifiedDealKeys = new HashSet<>();

    public NotifierService(GamerPowerClient gamerPowerClient,
            CheapSharkClient cheapSharkClient,
            TelegramClient telegramClient, FreeToGameClient freeToGameClient) {
        this.gamerPowerClient = gamerPowerClient;
        this.cheapSharkClient = cheapSharkClient;
        this.telegramClient = telegramClient;
        this.freeToGameClient = freeToGameClient;
    }

    /**
     * Obtiene la lista de juegos activos de GamerPower filtrando por tipo y estado.
     * 
     * @return Lista de juegos activos de GamerPower y estado "Active".
     */
    public List<GiveAway> getActiveGames() {
        return gamerPowerClient.fetchPcGiveaways().stream()
                .filter(g -> "Game".equalsIgnoreCase(g.type()))
                .filter(g -> "Active".equalsIgnoreCase(g.status()))
                .toList();
    }

    /**
     * Verifica y notifica nuevos juegos de las APIs integradas (GamerPower,
     * CheapShark y FreeToGame).
     * Evita notificar juegos que ya han sido enviados previamente.
     */
    public void checkAndNotifyNewGames() {
        // 1. GamerPower
        List<GiveAway> gamerPowerGames = getActiveGames();
        for (GiveAway game : gamerPowerGames) {
            String key = "GP_" + game.id();
            if (!notifiedDealKeys.contains(key)) {
                telegramClient.sendGiveawayNotification(game);
                notifiedDealKeys.add(key);
                System.out.println("✅ Notificado (GamerPower): " + game.title());
            }
        }

        // 2. CheapShark
        try {
            List<CheapShark> cheapSharkDeals = cheapSharkClient.fetchFreeDeals();
            for (CheapShark deal : cheapSharkDeals) {
                String key = "CS_" + deal.dealID();
                if (!notifiedDealKeys.contains(key)) {
                    telegramClient.sendCheapSharkNotification(deal);
                    notifiedDealKeys.add(key);
                    System.out.println("✅ Notificado (CheapShark): " + deal.title());
                }
            }
        } catch (Exception e) {
            System.err.println("⚠️ Error en el procesamiento de CheapShark:" + e.getMessage());
        }

        // 3. FreeToGame
        try {
            List<FreeToGame> ftgGames = freeToGameClient.fetchLatestPcGames().stream()
                    .limit(10) //
                    .toList();

            for (FreeToGame game : ftgGames) {
                String key = "FTG_" + game.id();
                if (!notifiedDealKeys.contains(key)) {
                    telegramClient.sendFreeToGameNotification(game);
                    notifiedDealKeys.add(key);
                    System.out.println("✅ Notificado (FreeToGame): " + game.title());

                    Thread.sleep(1000); // paramos para no saturar la API de Telegram
                }
            }
        } catch (InterruptedException e) {
            System.err.println("⚠️ Error consultando FreeToGame: " + e.getMessage());
        }
    }
}