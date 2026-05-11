package aulasenora.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aulasenora.service.UsuarioService;

import java.util.Map;

@RestController
@RequestMapping("/api/time")
public class TimeTrackingController {

    private final UsuarioService usuarioService;

    public TimeTrackingController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/current")
    public ResponseEntity<?> getCurrentTime(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        
        String username = authentication.getName();
        Long accumulatedTime = usuarioService.getTiempoAcumulado(username);
        
        return ResponseEntity.ok(Map.of("tiempoAcumulado", accumulatedTime));
    }

    @PostMapping("/ping")
    public ResponseEntity<?> ping(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        
        // Pings occur every 10 seconds, so we add 10 seconds to the accumulated time
        String username = authentication.getName();
        usuarioService.addTiempoAcumulado(username, 10L);
        
        return ResponseEntity.ok().build();
    }
}
