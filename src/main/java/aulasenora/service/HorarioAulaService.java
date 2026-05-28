package aulasenora.service;

import aulasenora.client.CalendarServiceClient;
import aulasenora.client.JwtTokenProvider;
import aulasenora.model.*;
import aulasenora.repository.AulaRepository;
import aulasenora.repository.HorarioAulaRepository;
import aulasenora.repository.MiembroAulaRepository;
import aulasenora.repository.SolicitudHorarioAulaRepository;
import aulasenora.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Service
public class HorarioAulaService {

    private final HorarioAulaRepository horarioAulaRepository;
    private final SolicitudHorarioAulaRepository solicitudHorarioAulaRepository;
    private final AulaRepository aulaRepository;
    private final UsuarioRepository usuarioRepository;
    private final MiembroAulaRepository miembroAulaRepository;
    private final CalendarServiceClient calendarServiceClient;
    private final JwtTokenProvider jwtTokenProvider;

    public HorarioAulaService(HorarioAulaRepository horarioAulaRepository,
                              SolicitudHorarioAulaRepository solicitudHorarioAulaRepository,
                              AulaRepository aulaRepository,
                              UsuarioRepository usuarioRepository,
                              MiembroAulaRepository miembroAulaRepository,
                              CalendarServiceClient calendarServiceClient,
                              JwtTokenProvider jwtTokenProvider) {
        this.horarioAulaRepository = horarioAulaRepository;
        this.solicitudHorarioAulaRepository = solicitudHorarioAulaRepository;
        this.aulaRepository = aulaRepository;
        this.usuarioRepository = usuarioRepository;
        this.miembroAulaRepository = miembroAulaRepository;
        this.calendarServiceClient = calendarServiceClient;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public List<HorarioAula> getHorariosByAula(Long aulaId) {
        return horarioAulaRepository.findByAula_IdOrderByFechaAscHoraInicioAsc(aulaId);
    }

    public List<SolicitudHorarioAula> getSolicitudesByAula(Long aulaId) {
        return solicitudHorarioAulaRepository.findByHorarioAula_Aula_Id(aulaId);
    }

    @Transactional
    public HorarioAula crearHorario(Long aulaId, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, String materia, String username, Boolean esGrupal) {
        Aula aula = aulaRepository.findById(Objects.requireNonNull(aulaId))
                .orElseThrow(() -> new IllegalArgumentException("Aula no encontrada"));

        if (!aula.getVoluntario().getUsuario().getUsername().equals(username)) {
            throw new SecurityException("No tienes permiso para agregar horarios a esta aula");
        }

        HorarioAula horario = new HorarioAula(aula, fecha, horaInicio, horaFin, materia, esGrupal);

        String jitsiLink = "https://meet.jit.si/AulaSenora-" + java.util.UUID.randomUUID().toString().substring(0, 8);
        horario.setMeetLink(jitsiLink);

        String volunteerEmail = aula.getVoluntario().getUsuario().getEmail();
        String title = "Aula Señora: " + aula.getNombreAula() + " - " + materia;
        String desc = "Sesión virtual con " + aula.getVoluntario().getUsuario().getFirstName() +
                      "\n\nÚnete a la clase aquí: " + jitsiLink;

        String token = jwtTokenProvider.generateToken(username);
        calendarServiceClient.createEvent(title, desc, fecha, horaInicio, horaFin, volunteerEmail, token)
                .ifPresent(horario::setGoogleEventId);

        return horarioAulaRepository.save(horario);
    }

    @Transactional
    public void eliminarHorario(Long horarioId, String username) {
        HorarioAula horario = horarioAulaRepository.findById(Objects.requireNonNull(horarioId))
                .orElseThrow(() -> new IllegalArgumentException("Horario no encontrado"));

        if (!horario.getAula().getVoluntario().getUsuario().getUsername().equals(username)) {
            throw new SecurityException("No tienes permiso para eliminar este horario");
        }

        List<SolicitudHorarioAula> solicitudes = solicitudHorarioAulaRepository.findByHorarioAula_Id(horarioId);
        solicitudHorarioAulaRepository.deleteAll(Objects.requireNonNull(solicitudes));

        horarioAulaRepository.delete(horario);
    }

    @Transactional
    public SolicitudHorarioAula solicitarHorario(Long horarioId, String username) {
        HorarioAula horario = horarioAulaRepository.findById(Objects.requireNonNull(horarioId))
                .orElseThrow(() -> new IllegalArgumentException("Horario no encontrado"));

        if ("OCUPADO".equals(horario.getEstado())) {
            throw new IllegalStateException("Este horario ya está ocupado");
        }

        Usuario estudiante = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Estudiante no encontrado"));

        if (solicitudHorarioAulaRepository.findByHorarioAula_IdAndEstudiante_Username(horarioId, username).isPresent()) {
            throw new IllegalStateException("Ya has solicitado este horario");
        }

        SolicitudHorarioAula solicitud = new SolicitudHorarioAula(estudiante, horario);

        if (Boolean.FALSE.equals(horario.getEsGrupal())) {
            horario.setEstado("PENDIENTE");
            horarioAulaRepository.save(horario);
        }

        return solicitudHorarioAulaRepository.save(solicitud);
    }

    @Transactional
    public void aprobarSolicitud(Long solicitudId, String username) {
        SolicitudHorarioAula solicitud = solicitudHorarioAulaRepository.findById(Objects.requireNonNull(solicitudId))
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

        if (Boolean.FALSE.equals(horario.getEsGrupal())) {
            horario.setEstado("OCUPADO");
            horarioAulaRepository.save(horario);

            List<SolicitudHorarioAula> otrasSolicitudes = solicitudHorarioAulaRepository.findByHorarioAula_Id(horario.getId());
            for (SolicitudHorarioAula otra : otrasSolicitudes) {
                if (!otra.getId().equals(solicitud.getId()) && "PENDIENTE".equals(otra.getEstado())) {
                    otra.setEstado("RECHAZADA");
                    solicitudHorarioAulaRepository.save(otra);
                }
            }
        }
    }

    @Transactional
    public void rechazarSolicitud(Long solicitudId, String username) {
        SolicitudHorarioAula solicitud = solicitudHorarioAulaRepository.findById(Objects.requireNonNull(solicitudId))
                .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada"));

        HorarioAula horario = solicitud.getHorarioAula();

        if (!horario.getAula().getVoluntario().getUsuario().getUsername().equals(username)) {
            throw new SecurityException("No tienes permiso para rechazar esta solicitud");
        }

        solicitud.setEstado("RECHAZADA");
        solicitudHorarioAulaRepository.save(solicitud);

        if (Boolean.FALSE.equals(horario.getEsGrupal())) {
            List<SolicitudHorarioAula> pendingRequests = solicitudHorarioAulaRepository.findByHorarioAula_Id(horario.getId()).stream()
                    .filter(s -> "PENDIENTE".equals(s.getEstado()))
                    .toList();

            if (pendingRequests.isEmpty() && !"OCUPADO".equals(horario.getEstado())) {
                horario.setEstado("DISPONIBLE");
                horarioAulaRepository.save(horario);
            }
        }
    }

    public java.util.Map<Long, String> getEstadoSolicitudesPorEstudiante(Long aulaId, String username) {
        java.util.Map<Long, String> resultado = new java.util.HashMap<>();
        List<HorarioAula> horarios = horarioAulaRepository.findByAula_IdOrderByFechaAscHoraInicioAsc(aulaId);
        for (HorarioAula horario : horarios) {
            solicitudHorarioAulaRepository.findByHorarioAula_IdAndEstudiante_Username(horario.getId(), username)
                    .ifPresent(solicitud -> resultado.put(horario.getId(), solicitud.getEstado()));
        }
        return resultado;
    }
}
