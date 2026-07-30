package es.lluentdev.notifier.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Clase que programa las tareas de notificación automática.
 */

@Component
public class NotifierScheduler {

    private final NotifierService giveAwayService;

    public NotifierScheduler(NotifierService giveAwayService) {

        this.giveAwayService = giveAwayService;
    }

    /**
     * Método que se ejecuta automáticamente para comprobar nuevas ofertas.
     */
    @Scheduled(fixedRate = 3600000)
    public void checkForNewDealsAutomatically() {
        System.out.println("🤖 Ejecutando comprobación automática de ofertas...");
        giveAwayService.checkAndNotifyNewGames();
    }
}
