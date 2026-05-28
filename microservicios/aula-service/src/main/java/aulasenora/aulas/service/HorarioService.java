package aulasenora.aulas.service;

import aulasenora.aulas.model.*;
import aulasenora.aulas.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Service
public class HorarioService {

    private final HorarioAulaRepository horarioAulaRepository;
    private final HorarioDisponibleRepository horarioDisponibleRepository;
    private final SolicitudHorarioAulaRepository solicitudHorarioAulaRepository;
    private final SolicitudCupoRepository solicitudCupoRepository;
    private final AulaRepository aulaRepository;
    private final UsuarioRepository usuarioRepository;
    private final VoluntarioRepository voluntarioRepository;

    public HorarioService(HorarioAulaRepository horarioAulaRepository,
                          HorarioDisponibleRepository horarioDisponibleRepository,
                          SolicitudHorarioAulaRepository solicitudHorarioAulaRepository,
                          SolicitudCupoRepository solicitudCupoRepository,
                          AulaRepository aulaRepository,
                          UsuarioRepository usuarioRepository,
                          VoluntarioRepository voluntarioRepository) {
        this.horarioAulaRepository = horarioAulaRepository;
        this.horarioDisponibleRepository = horarioDisponibleRepository;
        this.solicitudHorarioAulaRepository = solicitudHorarioAulaRepository;
        this.solicitudCupoRepository = solicitudCupoRepository;
        this.aulaRepository = aulaRepository;
        this.usuarioRepository = usuarioRepository;
        this.voluntarioRepository = voluntarioRepository;
    }

    public List<HorarioAula> horariosPorAula(Long aulaId) {
        return horarioAulaRepository.findByAulaId(aulaId);
    }

    public List<HorarioAula> horariosPorAulaYFecha(Long aulaId, LocalDate start, LocalDate end) {
        return horarioAulaRepository.findByAulaIdAndFechaBetween(aulaId, start, end);
    }

    @Transactional
    public HorarioAula crearHorario(Long aulaId, LocalDate fecha, LocalTime horaInicio,
                                     LocalTime horaFin, String materia, Long voluntarioId, Boolean esGrupal) {
        Aula aula = aulaRepository.findById(aulaId)
                .orElseThrow(() -> new IllegalArgumentException("Aula no encontrada"));

        if (!aula.getVoluntario().getId().equals(voluntarioId)) {
            throw new SecurityException("No tienes permiso para agregar horarios a esta aula");
        }

        String jitsiLink = "https://meet.jit.si/AulaSenora-" +
                java.util.UUID.randomUUID().toString().substring(0, 8);

        HorarioAula horario = new HorarioAula(aula, fecha, horaInicio, horaFin, materia, esGrupal);
        horario.setMeetLink(jitsiLink);

        return horarioAulaRepository.save(horario);
    }

    @Transactional
    public void eliminarHorario(Long horarioId, Long voluntarioId) {
        HorarioAula horario = horarioAulaRepository.findById(horarioId)
                .orElseThrow(() -> new IllegalArgumentException("Horario no encontrado"));

        if (!horario.getAula().getVoluntario().getId().equals(voluntarioId)) {
            throw new SecurityException("No tienes permiso para eliminar este horario");
        }

        List<SolicitudHorarioAula> solicitudes = solicitudHorarioAulaRepository.findByHorarioAulaId(horarioId);
        solicitudHorarioAulaRepository.deleteAll(solicitudes);
        horarioAulaRepository.delete(horario);
    }

    @Transactional
    public SolicitudHorarioAula solicitarHorario(Long horarioId, Long usuarioId) {
        HorarioAula horario = horarioAulaRepository.findById(horarioId)
                .orElseThrow(() -> new IllegalArgumentException("Horario no encontrado"));

        if ("OCUPADO".equals(horario.getEstado())) {
            throw new IllegalStateException("Este horario ya esta ocupado");
        }

        Usuario estudiante = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Estudiante no encontrado"));

        if (solicitudHorarioAulaRepository.findByHorarioAulaId(horarioId).stream()
                .anyMatch(s -> s.getEstudiante().getId().equals(usuarioId))) {
            throw new IllegalStateException("Ya has solicitado este horario");
        }

        SolicitudHorarioAula solicitud = new SolicitudHorarioAula(estudiante, horario);

        if (Boolean.FALSE.equals(horario.getEsGrupal())) {
            horario.setEstado("PENDIENTE");
            horarioAulaRepository.save(horario);
        }

        return solicitudHorarioAulaRepository.save(solicitud);
    }

    public List<SolicitudHorarioAula> solicitudesPorAula(Long aulaId) {
        return solicitudHorarioAulaRepository.findByHorarioAula_Aula_Id(aulaId);
    }

    public List<SolicitudHorarioAula> solicitudesPorHorario(Long horarioId) {
        return solicitudHorarioAulaRepository.findByHorarioAulaId(horarioId);
    }

    @Transactional
    public void aprobarSolicitudHorario(Long solicitudId, Long voluntarioId) {
        SolicitudHorarioAula solicitud = solicitudHorarioAulaRepository.findById(solicitudId)
                .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada"));

        HorarioAula horario = solicitud.getHorarioAula();

        if (!horario.getAula().getVoluntario().getId().equals(voluntarioId)) {
            throw new SecurityException("No tienes permiso para aprobar esta solicitud");
        }

        if (!"PENDIENTE".equals(solicitud.getEstado())) {
            throw new IllegalStateException("La solicitud no esta pendiente");
        }

        solicitud.setEstado("ACEPTADA");
        solicitudHorarioAulaRepository.save(solicitud);

        if (Boolean.FALSE.equals(horario.getEsGrupal())) {
            horario.setEstado("OCUPADO");
            horarioAulaRepository.save(horario);

            solicitudHorarioAulaRepository.findByHorarioAulaId(horario.getId()).stream()
                    .filter(s -> !s.getId().equals(solicitud.getId()) && "PENDIENTE".equals(s.getEstado()))
                    .forEach(s -> {
                        s.setEstado("RECHAZADA");
                        solicitudHorarioAulaRepository.save(s);
                    });
        }
    }

    @Transactional
    public void rechazarSolicitudHorario(Long solicitudId, Long voluntarioId) {
        SolicitudHorarioAula solicitud = solicitudHorarioAulaRepository.findById(solicitudId)
                .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada"));

        HorarioAula horario = solicitud.getHorarioAula();

        if (!horario.getAula().getVoluntario().getId().equals(voluntarioId)) {
            throw new SecurityException("No tienes permiso para rechazar esta solicitud");
        }

        solicitud.setEstado("RECHAZADA");
        solicitudHorarioAulaRepository.save(solicitud);

        if (Boolean.FALSE.equals(horario.getEsGrupal())) {
            boolean hasPending = solicitudHorarioAulaRepository.findByHorarioAulaId(horario.getId()).stream()
                    .anyMatch(s -> "PENDIENTE".equals(s.getEstado()));
            if (!hasPending && !"OCUPADO".equals(horario.getEstado())) {
                horario.setEstado("DISPONIBLE");
                horarioAulaRepository.save(horario);
            }
        }
    }

    public Map<Long, String> estadoSolicitudesHorario(Long aulaId, Long usuarioId) {
        Map<Long, String> resultado = new java.util.HashMap<>();
        List<HorarioAula> horarios = horarioAulaRepository.findByAulaId(aulaId);
        for (HorarioAula horario : horarios) {
            solicitudHorarioAulaRepository.findByHorarioAulaId(horario.getId()).stream()
                    .filter(s -> s.getEstudiante().getId().equals(usuarioId))
                    .findFirst()
                    .ifPresent(s -> resultado.put(horario.getId(), s.getEstado()));
        }
        return resultado;
    }

    public List<HorarioDisponible> horariosDisponiblesPorVoluntario(Long voluntarioId) {
        return horarioDisponibleRepository.findByVoluntarioId(voluntarioId);
    }

    @Transactional
    public HorarioDisponible crearHorarioDisponible(Long voluntarioId, LocalDate fecha,
                                                     LocalTime horaInicio, LocalTime horaFin, String materia) {
        Voluntario voluntario = voluntarioRepository.findById(voluntarioId)
                .orElseThrow(() -> new IllegalArgumentException("Voluntario no encontrado"));

        HorarioDisponible hd = new HorarioDisponible(voluntario, fecha, horaInicio, horaFin, materia);
        return horarioDisponibleRepository.save(hd);
    }

    @Transactional
    public void eliminarHorarioDisponible(Long horarioId, Long voluntarioId) {
        HorarioDisponible hd = horarioDisponibleRepository.findById(horarioId)
                .orElseThrow(() -> new IllegalArgumentException("Horario no encontrado"));
        if (!hd.getVoluntario().getId().equals(voluntarioId)) {
            throw new SecurityException("No tienes permiso para eliminar este horario");
        }
        horarioDisponibleRepository.delete(hd);
    }

    public List<SolicitudCupo> solicitudesCupoPorVoluntario(Long voluntarioId) {
        return solicitudCupoRepository.findByHorario_VoluntarioId(voluntarioId);
    }

    public List<SolicitudCupo> solicitudesCupoPorEstudiante(Long estudianteId) {
        return solicitudCupoRepository.findByEstudianteId(estudianteId);
    }
}
