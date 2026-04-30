package aulasenora.service;

import aulasenora.model.Aula;
import aulasenora.model.HorarioAula;
import aulasenora.model.SolicitudHorarioAula;
import aulasenora.model.Usuario;
import aulasenora.repository.AulaRepository;
import aulasenora.repository.HorarioAulaRepository;
import aulasenora.repository.SolicitudHorarioAulaRepository;
import aulasenora.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;

@Service
public class HorarioAulaService {

    private final HorarioAulaRepository horarioAulaRepository;
    private final SolicitudHorarioAulaRepository solicitudHorarioAulaRepository;
    private final AulaRepository aulaRepository;
    private final UsuarioRepository usuarioRepository;

    public HorarioAulaService(HorarioAulaRepository horarioAulaRepository,
                              SolicitudHorarioAulaRepository solicitudHorarioAulaRepository,
                              AulaRepository aulaRepository,
                              UsuarioRepository usuarioRepository) {
        this.horarioAulaRepository = horarioAulaRepository;
        this.solicitudHorarioAulaRepository = solicitudHorarioAulaRepository;
        this.aulaRepository = aulaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<HorarioAula> getHorariosByAula(Long aulaId) {
        return horarioAulaRepository.findByAula_Id(aulaId);
    }

    public List<SolicitudHorarioAula> getSolicitudesByAula(Long aulaId) {
        return solicitudHorarioAulaRepository.findByHorarioAula_Aula_Id(aulaId);
    }

    @Transactional
    public HorarioAula crearHorario(Long aulaId, String diaSemana, LocalTime horaInicio, LocalTime horaFin, String materia, String username) {
        Aula aula = aulaRepository.findById(aulaId)
                .orElseThrow(() -> new IllegalArgumentException("Aula no encontrada"));

        // Validate volunteer owns the aula
        if (!aula.getVoluntario().getUsuario().getUsername().equals(username)) {
            throw new SecurityException("No tienes permiso para agregar horarios a esta aula");
        }

        HorarioAula horario = new HorarioAula(aula, diaSemana, horaInicio, horaFin, materia);
        return horarioAulaRepository.save(horario);
    }

    @Transactional
    public void eliminarHorario(Long horarioId, String username) {
        HorarioAula horario = horarioAulaRepository.findById(horarioId)
                .orElseThrow(() -> new IllegalArgumentException("Horario no encontrado"));

        if (!horario.getAula().getVoluntario().getUsuario().getUsername().equals(username)) {
            throw new SecurityException("No tienes permiso para eliminar este horario");
        }

        // Delete associated requests first
        List<SolicitudHorarioAula> solicitudes = solicitudHorarioAulaRepository.findByHorarioAula_Id(horarioId);
        solicitudHorarioAulaRepository.deleteAll(solicitudes);

        horarioAulaRepository.delete(horario);
    }

    @Transactional
    public SolicitudHorarioAula solicitarHorario(Long horarioId, String username) {
        HorarioAula horario = horarioAulaRepository.findById(horarioId)
                .orElseThrow(() -> new IllegalArgumentException("Horario no encontrado"));

        if ("OCUPADO".equals(horario.getEstado())) {
            throw new IllegalStateException("Este horario ya está ocupado");
        }

        Usuario estudiante = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Estudiante no encontrado"));

        // Check if student already requested
        if (solicitudHorarioAulaRepository.findByHorarioAula_IdAndEstudiante_Username(horarioId, username).isPresent()) {
            throw new IllegalStateException("Ya has solicitado este horario");
        }

        SolicitudHorarioAula solicitud = new SolicitudHorarioAula(estudiante, horario);
        
        horario.setEstado("PENDIENTE");
        horarioAulaRepository.save(horario);

        return solicitudHorarioAulaRepository.save(solicitud);
    }

    @Transactional
    public void aprobarSolicitud(Long solicitudId, String username) {
        SolicitudHorarioAula solicitud = solicitudHorarioAulaRepository.findById(solicitudId)
                .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada"));

        HorarioAula horario = solicitud.getHorarioAula();

        if (!horario.getAula().getVoluntario().getUsuario().getUsername().equals(username)) {
            throw new SecurityException("No tienes permiso para aprobar esta solicitud");
        }

        if (!"PENDIENTE".equals(solicitud.getEstado())) {
            throw new IllegalStateException("La solicitud no está pendiente");
        }

        solicitud.setEstado("ACEPTADA");
        solicitudHorarioAulaRepository.save(solicitud);

        horario.setEstado("OCUPADO");
        horarioAulaRepository.save(horario);

        // Reject all other pending requests for this schedule
        List<SolicitudHorarioAula> otrasSolicitudes = solicitudHorarioAulaRepository.findByHorarioAula_Id(horario.getId());
        for (SolicitudHorarioAula otra : otrasSolicitudes) {
            if (!otra.getId().equals(solicitud.getId()) && "PENDIENTE".equals(otra.getEstado())) {
                otra.setEstado("RECHAZADA");
                solicitudHorarioAulaRepository.save(otra);
            }
        }
    }

    @Transactional
    public void rechazarSolicitud(Long solicitudId, String username) {
        SolicitudHorarioAula solicitud = solicitudHorarioAulaRepository.findById(solicitudId)
                .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada"));

        HorarioAula horario = solicitud.getHorarioAula();

        if (!horario.getAula().getVoluntario().getUsuario().getUsername().equals(username)) {
            throw new SecurityException("No tienes permiso para rechazar esta solicitud");
        }

        solicitud.setEstado("RECHAZADA");
        solicitudHorarioAulaRepository.save(solicitud);

        // If no more pending requests, change schedule back to DISPONIBLE
        List<SolicitudHorarioAula> pendingRequests = solicitudHorarioAulaRepository.findByHorarioAula_Id(horario.getId()).stream()
                .filter(s -> "PENDIENTE".equals(s.getEstado()))
                .toList();

        if (pendingRequests.isEmpty() && !"OCUPADO".equals(horario.getEstado())) {
            horario.setEstado("DISPONIBLE");
            horarioAulaRepository.save(horario);
        }
    }
}
