package aulasenora.controller;

import aulasenora.dto.MensajeAulaDTO;
import aulasenora.service.MensajeAulaService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/aulas/{aulaId}/chat")
public class ChatController {

    private final MensajeAulaService mensajeAulaService;

    public ChatController(MensajeAulaService mensajeAulaService) {
        this.mensajeAulaService = mensajeAulaService;
    }

    @GetMapping
    public ResponseEntity<List<MensajeAulaDTO>> getMensajes(@PathVariable Long aulaId, Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).build();
        }
        try {
            List<MensajeAulaDTO> mensajes = mensajeAulaService.getMensajesByAula(aulaId, authentication.getName());
            return ResponseEntity.ok(mensajes);
        } catch (RuntimeException e) {
            return ResponseEntity.status(403).build();
        }
    }

    @PostMapping
    public ResponseEntity<MensajeAulaDTO> enviarMensaje(@PathVariable Long aulaId, @RequestBody Map<String, String> payload, Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).build();
        }
        try {
            String contenido = payload.get("contenido");
            if (contenido == null || contenido.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            MensajeAulaDTO mensaje = mensajeAulaService.enviarMensaje(aulaId, authentication.getName(), contenido.trim());
            return ResponseEntity.ok(mensaje);
        } catch (RuntimeException e) {
            return ResponseEntity.status(403).build();
        }
    }
}
