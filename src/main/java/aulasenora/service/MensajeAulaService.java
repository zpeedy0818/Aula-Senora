package aulasenora.service;

import aulasenora.dto.MensajeAulaDTO;
import aulasenora.model.Aula;
import aulasenora.model.MensajeAula;
import aulasenora.model.MiembroAula;
import aulasenora.model.Usuario;
import aulasenora.repository.AulaRepository;
import aulasenora.repository.MensajeAulaRepository;
import aulasenora.repository.MiembroAulaRepository;
import aulasenora.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MensajeAulaService {

    private final MensajeAulaRepository mensajeAulaRepository;
    private final AulaRepository aulaRepository;
    private final MiembroAulaRepository miembroAulaRepository;
    private final UsuarioRepository usuarioRepository;

    public MensajeAulaService(MensajeAulaRepository mensajeAulaRepository, AulaRepository aulaRepository, MiembroAulaRepository miembroAulaRepository, UsuarioRepository usuarioRepository) {
        this.mensajeAulaRepository = mensajeAulaRepository;
        this.aulaRepository = aulaRepository;
        this.miembroAulaRepository = miembroAulaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    private void verificarAcceso(Long aulaId, Usuario usuario) {
        Aula aula = aulaRepository.findById(aulaId).orElseThrow(() -> new RuntimeException("Aula no encontrada"));
        
        // Es el voluntario dueño del aula?
        if (aula.getVoluntario().getId().equals(usuario.getId())) {
            return;
        }

        // Es un estudiante miembro?
        MiembroAula miembro = miembroAulaRepository.findByAulaIdAndUsuarioId(aulaId, usuario.getId())
                .orElseThrow(() -> new RuntimeException("No tienes acceso a esta aula"));
    }

    public List<MensajeAulaDTO> getMensajesByAula(Long aulaId, String username) {
        Usuario usuario = usuarioRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        verificarAcceso(aulaId, usuario);

        List<MensajeAula> mensajes = mensajeAulaRepository.findByAulaIdOrderByFechaEnvioAsc(aulaId);
        return mensajes.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public MensajeAulaDTO enviarMensaje(Long aulaId, String username, String contenido) {
        Usuario usuario = usuarioRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        verificarAcceso(aulaId, usuario);

        Aula aula = aulaRepository.findById(aulaId).get();

        MensajeAula mensaje = new MensajeAula();
        mensaje.setAula(aula);
        mensaje.setRemitente(usuario);
        mensaje.setContenido(contenido);

        MensajeAula saved = mensajeAulaRepository.save(mensaje);
        return convertToDTO(saved);
    }

    private MensajeAulaDTO convertToDTO(MensajeAula mensaje) {
        Usuario remitente = mensaje.getRemitente();
        String nombre = remitente.getFirstName() + " " + remitente.getLastName();
        
        // Determinar rol para el UI (si es el dueño del aula, lo marcamos como voluntario, si no, estudiante)
        String rol = "ESTUDIANTE";
        if (mensaje.getAula().getVoluntario().getId().equals(remitente.getId())) {
            rol = "VOLUNTARIO";
        }

        return new MensajeAulaDTO(
                mensaje.getId(),
                mensaje.getContenido(),
                nombre,
                remitente.getId(),
                remitente.getUsername(),
                rol,
                mensaje.getFechaEnvio()
        );
    }
}
