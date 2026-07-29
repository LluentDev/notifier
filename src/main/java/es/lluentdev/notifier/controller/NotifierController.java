package es.lluentdev.notifier.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.lluentdev.notifier.model.GiveAway;
import es.lluentdev.notifier.service.NotifierService;

/**
 * Controlador para gestionar las operaciones relacionadas con las ofertas de
 * juegos.
 * 
 * @author LluentDev
 * @version 1.0
 */

@RestController
@RequestMapping("/api/v1/giveaways")
@CrossOrigin(origins = "*")
public class NotifierController {

    private final NotifierService giveAwayService;

    public NotifierController(NotifierService giveAwayService) {
        this.giveAwayService = giveAwayService;
    }

    // Obtiene la lista de todos los juegos activos
    @GetMapping
    public ResponseEntity<List<GiveAway>> getAllActiveGames() {
        List<GiveAway> games = giveAwayService.getActiveGames();
        return ResponseEntity.ok(games);
    }

    // Dispara manualmente la comprobación y el envío a Telegram
    @PostMapping("/check-now")
    public ResponseEntity<String> triggerCheckAutomatically() {
        giveAwayService.checkAndNotifyNewGames();
        return ResponseEntity.ok("Comprobación ejecutada. Si hay juegos nuevos, se habrán enviado a Telegram.");
    }
}