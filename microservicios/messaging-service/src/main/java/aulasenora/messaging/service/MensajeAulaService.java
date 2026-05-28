package aulasenora.messaging.service;

import aulasenora.messaging.dto.MensajeAulaDTO;
import aulasenora.messaging.model.Aula;
import aulasenora.messaging.model.MensajeAula;
import aulasenora.messaging.model.Usuario;
import aulasenora.messaging.repository.AulaRepository;
import aulasenora.messaging.repository.MensajeAulaRepository;
import aulasenora.messaging.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MensajeAulaService {

    private final MensajeAulaRepository mensajeAulaRepository;
    private final AulaRepository aulaRepository;
    private final UsuarioRepository usuarioRepository;

    public MensajeAulaService(MensajeAulaRepository mensajeAulaRepository,
                              AulaRepository aulaRepository,
                              UsuarioRepository usuarioRepository) {
        this.mensajeAulaRepository = mensajeAulaRepository;
        this.aulaRepository = aulaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<MensajeAulaDTO> getMensajes(Long aulaId, String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Aula aula = aulaRepository.findById(aulaId)
                .orElseThrow(() -> new RuntimeException("Aula no encontrada"));

        if (!tieneAcceso(aula, usuario)) {
            throw new RuntimeException("No tienes acceso a esta aula");
        }

        return mensajeAulaRepository.findByAulaIdOrderByFechaEnvioAsc(aulaId).stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional
    public MensajeAulaDTO enviar(Long aulaId, String username, String contenido) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Aula aula = aulaRepository.findById(aulaId)
                .orElseThrow(() -> new RuntimeException("Aula no encontrada"));

        if (!tieneAcceso(aula, usuario)) {
            throw new RuntimeException("No tienes acceso a esta aula");
        }

        MensajeAula mensaje = new MensajeAula();
        mensaje.setAula(aula);
        mensaje.setRemitente(usuario);
        mensaje.setContenido(contenido);

        MensajeAula saved = mensajeAulaRepository.save(mensaje);
        return toDTO(saved);
    }

    private boolean tieneAcceso(Aula aula, Usuario usuario) {
        return aula.getVoluntario().getId().equals(usuario.getId());
    }

    private MensajeAulaDTO toDTO(MensajeAula m) {
        Usuario remitente = m.getRemitente();
        String nombre = remitente.getFirstName() + " " + remitente.getLastName();
        String rol = m.getAula().getVoluntario().getId().equals(remitente.getId()) ? "VOLUNTARIO" : "ESTUDIANTE";

        return new MensajeAulaDTO(
                m.getId(), m.getContenido(), nombre,
                remitente.getId(), remitente.getUsername(), rol, m.getFechaEnvio()
        );
    }
}
