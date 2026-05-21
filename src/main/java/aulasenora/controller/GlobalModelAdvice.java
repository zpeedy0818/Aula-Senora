package aulasenora.controller;

import aulasenora.model.MensajeGlobal;
import aulasenora.repository.MensajeGlobalRepository;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
public class GlobalModelAdvice {

    private final MensajeGlobalRepository mensajeGlobalRepository;
    private final aulasenora.repository.UsuarioRepository usuarioRepository;

    public GlobalModelAdvice(MensajeGlobalRepository mensajeGlobalRepository, aulasenora.repository.UsuarioRepository usuarioRepository) {
        this.mensajeGlobalRepository = mensajeGlobalRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @ModelAttribute("mensajesGlobalesActivos")
    public List<MensajeGlobal> mensajesGlobalesActivos() {
        return mensajeGlobalRepository.findByActivoTrueOrderByFechaCreacionDesc();
    }

    @ModelAttribute("currentUser")
    public aulasenora.model.Usuario getCurrentUser(org.springframework.security.core.Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {
            return usuarioRepository.findByUsername(authentication.getName()).orElse(null);
        }
        return null;
    }
}
