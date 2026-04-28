package aulasenora.service;

import aulasenora.model.Aula;
import aulasenora.model.MiembroAula;
import aulasenora.model.SolicitudAula;
import aulasenora.model.Usuario;
import aulasenora.model.Voluntario;
import aulasenora.repository.AulaRepository;
import aulasenora.repository.MiembroAulaRepository;
import aulasenora.repository.SolicitudAulaRepository;
import aulasenora.repository.UsuarioRepository;
import aulasenora.repository.VoluntarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AulaService {

    private final AulaRepository aulaRepository;
    private final SolicitudAulaRepository solicitudAulaRepository;
    private final MiembroAulaRepository miembroAulaRepository;
    private final UsuarioRepository usuarioRepository;
    private final VoluntarioRepository voluntarioRepository;

    public AulaService(AulaRepository aulaRepository,
                       SolicitudAulaRepository solicitudAulaRepository,
                       MiembroAulaRepository miembroAulaRepository,
                       UsuarioRepository usuarioRepository,
                       VoluntarioRepository voluntarioRepository) {
        this.aulaRepository = aulaRepository;
        this.solicitudAulaRepository = solicitudAulaRepository;
        this.miembroAulaRepository = miembroAulaRepository;
        this.usuarioRepository = usuarioRepository;
        this.voluntarioRepository = voluntarioRepository;
    }

    @Transactional
    public Aula crearAula(Aula aula, String username) {
        Usuario usuario = usuarioRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Voluntario voluntario = voluntarioRepository.findById(usuario.getId()).orElseThrow(() -> new RuntimeException("Voluntario no encontrado"));

        aula.setVoluntario(voluntario);
        Aula savedAula = aulaRepository.save(aula);

        // Añadir el voluntario como miembro
        MiembroAula miembro = new MiembroAula();
        miembro.setAula(savedAula);
        miembro.setUsuario(usuario);
        miembro.setRol("VOLUNTARIO");
        miembroAulaRepository.save(miembro);

        return savedAula;
    }

    public List<Aula> getAulasByVoluntario(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return aulaRepository.findByVoluntarioId(usuario.getId());
    }

    public List<Aula> getAllAulas() {
        return aulaRepository.findAll();
    }

    public Aula getAulaById(Long id) {
        return aulaRepository.findById(id).orElseThrow(() -> new RuntimeException("Aula no encontrada"));
    }

    @Transactional
    public void solicitarAcceso(Long aulaId, String username) {
        Aula aula = getAulaById(aulaId);
        Usuario usuario = usuarioRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Optional<SolicitudAula> existente = solicitudAulaRepository.findByAulaIdAndEstudianteId(aula.getId(), usuario.getId());
        if (existente.isPresent()) {
            throw new RuntimeException("Ya has enviado una solicitud a esta aula");
        }

        Optional<MiembroAula> miembro = miembroAulaRepository.findByAulaIdAndUsuarioId(aula.getId(), usuario.getId());
        if (miembro.isPresent()) {
            throw new RuntimeException("Ya eres miembro de esta aula");
        }

        SolicitudAula solicitud = new SolicitudAula();
        solicitud.setAula(aula);
        solicitud.setEstudiante(usuario);
        solicitud.setEstado("PENDIENTE");
        solicitudAulaRepository.save(solicitud);
    }

    public List<SolicitudAula> getSolicitudesPendientesByAula(Long aulaId) {
        return solicitudAulaRepository.findByAulaIdAndEstado(aulaId, "PENDIENTE");
    }

    @Transactional
    public void aprobarSolicitud(Long solicitudId) {
        SolicitudAula solicitud = solicitudAulaRepository.findById(solicitudId).orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));
        solicitud.setEstado("APROBADA");
        solicitudAulaRepository.save(solicitud);

        MiembroAula miembro = new MiembroAula();
        miembro.setAula(solicitud.getAula());
        miembro.setUsuario(solicitud.getEstudiante());
        miembro.setRol("ESTUDIANTE");
        miembroAulaRepository.save(miembro);
    }

    @Transactional
    public void rechazarSolicitud(Long solicitudId) {
        SolicitudAula solicitud = solicitudAulaRepository.findById(solicitudId).orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));
        solicitud.setEstado("RECHAZADA");
        solicitudAulaRepository.save(solicitud);
    }

    public List<MiembroAula> getAulasByEstudiante(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return miembroAulaRepository.findByUsuarioId(usuario.getId());
    }

    public List<MiembroAula> getMiembrosByAula(Long aulaId) {
        return miembroAulaRepository.findByAulaId(aulaId);
    }
}
