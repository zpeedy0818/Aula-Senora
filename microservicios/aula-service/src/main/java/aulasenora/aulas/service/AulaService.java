package aulasenora.aulas.service;

import aulasenora.aulas.model.*;
import aulasenora.aulas.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class AulaService {

    private final AulaRepository aulaRepository;
    private final SolicitudAulaRepository solicitudAulaRepository;
    private final MiembroAulaRepository miembroAulaRepository;
    private final UsuarioRepository usuarioRepository;

    public AulaService(AulaRepository aulaRepository,
                       SolicitudAulaRepository solicitudAulaRepository,
                       MiembroAulaRepository miembroAulaRepository,
                       UsuarioRepository usuarioRepository) {
        this.aulaRepository = aulaRepository;
        this.solicitudAulaRepository = solicitudAulaRepository;
        this.miembroAulaRepository = miembroAulaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public Aula crearAula(Aula aula, Long voluntarioId) {
        Usuario usuario = usuarioRepository.findById(voluntarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Voluntario voluntario = new Voluntario();
        voluntario.setId(voluntarioId);
        voluntario.setUsuario(usuario);

        aula.setVoluntario(voluntario);
        Aula saved = aulaRepository.save(aula);

        MiembroAula miembro = new MiembroAula();
        miembro.setAula(saved);
        miembro.setUsuario(usuario);
        miembro.setRol("VOLUNTARIO");
        miembroAulaRepository.save(miembro);

        return saved;
    }

    public List<Aula> listarPorVoluntario(Long voluntarioId) {
        return aulaRepository.findByVoluntario_Id(voluntarioId);
    }

    public List<Aula> listarTodas() {
        return aulaRepository.findAll();
    }

    public List<Aula> buscarPorNombre(String nombre) {
        return aulaRepository.findByNombreAulaContainingIgnoreCase(nombre);
    }

    public Aula obtenerPorId(Long id) {
        return aulaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aula no encontrada"));
    }

    public List<MiembroAula> listarMiembros(Long aulaId) {
        return miembroAulaRepository.findByAulaId(aulaId);
    }

    public List<MiembroAula> listarAulasPorEstudiante(Long usuarioId) {
        return miembroAulaRepository.findByUsuarioId(usuarioId);
    }

    @Transactional
    public void solicitarAcceso(Long aulaId, Long usuarioId) {
        Aula aula = obtenerPorId(aulaId);
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        var existente = solicitudAulaRepository
                .findByAulaIdAndEstudianteId(aula.getId(), usuario.getId());
        if (existente.isPresent()) {
            throw new RuntimeException("Ya has enviado una solicitud a esta aula");
        }

        var miembro = miembroAulaRepository
                .findByAulaIdAndUsuarioId(aula.getId(), usuario.getId());
        if (miembro.isPresent()) {
            throw new RuntimeException("Ya eres miembro de esta aula");
        }

        SolicitudAula solicitud = new SolicitudAula();
        solicitud.setAula(aula);
        solicitud.setEstudiante(usuario);
        solicitud.setEstado("PENDIENTE");
        solicitudAulaRepository.save(solicitud);
    }

    public List<SolicitudAula> solicitudesPendientes(Long aulaId) {
        return solicitudAulaRepository.findByAulaIdAndEstado(aulaId, "PENDIENTE");
    }

    public List<SolicitudAula> solicitudesPorEstudiante(Long estudianteId) {
        return solicitudAulaRepository.findByEstudianteId(estudianteId);
    }

    @Transactional
    public void aprobarSolicitud(Long solicitudId) {
        SolicitudAula solicitud = solicitudAulaRepository.findById(solicitudId)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));
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
        SolicitudAula solicitud = solicitudAulaRepository.findById(solicitudId)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));
        solicitud.setEstado("RECHAZADA");
        solicitudAulaRepository.save(solicitud);
    }

    @Transactional
    public void eliminarAula(Long aulaId, Long voluntarioId) {
        Aula aula = obtenerPorId(aulaId);
        if (!aula.getVoluntario().getId().equals(voluntarioId)) {
            throw new RuntimeException("No tienes permisos para eliminar esta aula");
        }
        solicitudAulaRepository.deleteAll(solicitudAulaRepository.findByAulaId(aulaId));
        miembroAulaRepository.deleteAll(miembroAulaRepository.findByAulaId(aulaId));
        aulaRepository.delete(aula);
    }

    @Transactional
    public void eliminarEstudiante(Long aulaId, Long estudianteId, Long voluntarioId) {
        Aula aula = obtenerPorId(aulaId);
        if (!aula.getVoluntario().getId().equals(voluntarioId)) {
            throw new RuntimeException("No tienes permisos para eliminar estudiantes");
        }
        if (voluntarioId.equals(estudianteId)) {
            throw new RuntimeException("No puedes eliminarte a ti mismo del aula");
        }
        miembroAulaRepository.findByAulaIdAndUsuarioId(aulaId, estudianteId)
                .ifPresent(miembroAulaRepository::delete);
    }

    public Map<Long, String> estadoSolicitudesPorEstudiante(Long estudianteId) {
        Map<Long, String> resultado = new java.util.HashMap<>();
        List<SolicitudAula> solicitudes = solicitudAulaRepository.findByEstudianteId(estudianteId);
        for (SolicitudAula s : solicitudes) {
            resultado.put(s.getAula().getId(), s.getEstado());
        }
        return resultado;
    }

    public long contarMiembros(Long aulaId) {
        return miembroAulaRepository.countByAulaId(aulaId);
    }

    public boolean esMiembro(Long aulaId, Long usuarioId) {
        return miembroAulaRepository.existsByAulaIdAndUsuarioId(aulaId, usuarioId);
    }
}
