package aulasenora.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aulasenora.dto.RegistroDTO;
import aulasenora.model.Usuario;
import aulasenora.service.UsuarioService;

@RestController
@RequestMapping("/api/registro")
public class RegistroController {

    private final UsuarioService usuarioService;

    public RegistroController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody RegistroDTO registroDTO) {
        try {
            Usuario nuevo = usuarioService.registrar(registroDTO);
            nuevo.setPassword(null); // No devolver la contraseña en la respuesta
            return ResponseEntity.ok(nuevo);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}