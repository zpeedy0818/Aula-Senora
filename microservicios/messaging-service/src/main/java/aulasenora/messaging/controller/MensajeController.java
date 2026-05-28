package aulasenora.messaging.controller;

import aulasenora.messaging.dto.MensajeAulaDTO;
import aulasenora.messaging.model.MensajeGlobal;
import aulasenora.messaging.service.MensajeAulaService;
import aulasenora.messaging.service.MensajeGlobalService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class MensajeController {

    private final MensajeAulaService mensajeAulaService;
    private final MensajeGlobalService mensajeGlobalService;

    public MensajeController(MensajeAulaService mensajeAulaService,
                             MensajeGlobalService mensajeGlobalService) {
        this.mensajeAulaService = mensajeAulaService;
        this.mensajeGlobalService = mensajeGlobalService;
    }

    @GetMapping("/aulas/{aulaId}/chat")
    public ResponseEntity<List<MensajeAulaDTO>> getMensajes(@PathVariable Long aulaId, Authentication auth) {
        if (auth == null) return ResponseEntity.status(401).build();
        try {
            return ResponseEntity.ok(mensajeAulaService.getMensajes(aulaId, auth.getName()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(403).build();
        }
    }

    @PostMapping("/aulas/{aulaId}/chat")
    public ResponseEntity<MensajeAulaDTO> enviarMensaje(@PathVariable Long aulaId,
                                                         @RequestBody Map<String, String> payload,
                                                         Authentication auth) {
        if (auth == null) return ResponseEntity.status(401).build();
        String contenido = payload.get("contenido");
        if (contenido == null || contenido.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        try {
            return ResponseEntity.ok(mensajeAulaService.enviar(aulaId, auth.getName(), contenido.trim()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(403).build();
        }
    }

    @GetMapping("/mensajes-globales")
    public ResponseEntity<List<MensajeGlobal>> getMensajesGlobales() {
        return ResponseEntity.ok(mensajeGlobalService.listarActivos());
    }

    @GetMapping("/admin/mensajes-globales")
    public ResponseEntity<List<MensajeGlobal>> getTodosMensajesGlobales() {
        return ResponseEntity.ok(mensajeGlobalService.listarTodos());
    }

    @PostMapping("/admin/mensajes-globales")
    public ResponseEntity<MensajeGlobal> crearMensajeGlobal(@RequestBody Map<String, String> payload) {
        String titulo = payload.get("titulo");
        String contenido = payload.get("contenido");
        if (titulo == null || titulo.isBlank() || contenido == null || contenido.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(mensajeGlobalService.crear(titulo, contenido));
    }

    @PutMapping("/admin/mensajes-globales/{id}")
    public ResponseEntity<MensajeGlobal> actualizarMensajeGlobal(@PathVariable Long id,
                                                                  @RequestBody Map<String, Object> payload) {
        String titulo = (String) payload.get("titulo");
        String contenido = (String) payload.get("contenido");
        Boolean activo = payload.containsKey("activo") ? (Boolean) payload.get("activo") : null;
        try {
            return ResponseEntity.ok(mensajeGlobalService.actualizar(id, titulo, contenido, activo));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/admin/mensajes-globales/{id}")
    public ResponseEntity<Void> eliminarMensajeGlobal(@PathVariable Long id) {
        mensajeGlobalService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
