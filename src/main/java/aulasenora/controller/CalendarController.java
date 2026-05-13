package aulasenora.controller;

import aulasenora.model.*;
import aulasenora.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/calendar")
public class CalendarController {

    private final HorarioDisponibleRepository horarioDisponibleRepository;
    private final SolicitudCupoRepository solicitudCupoRepository;
    private final VoluntarioRepository voluntarioRepository;
    private final UsuarioRepository usuarioRepository;
    private final HorarioAulaRepository horarioAulaRepository;

    public CalendarController(HorarioDisponibleRepository horarioDisponibleRepository,
                              SolicitudCupoRepository solicitudCupoRepository,
                              VoluntarioRepository voluntarioRepository,
                              UsuarioRepository usuarioRepository,
                              HorarioAulaRepository horarioAulaRepository) {
        this.horarioDisponibleRepository = horarioDisponibleRepository;
        this.solicitudCupoRepository = solicitudCupoRepository;
        this.voluntarioRepository = voluntarioRepository;
        this.usuarioRepository = usuarioRepository;
        this.horarioAulaRepository = horarioAulaRepository;
    }

    // Retorna la disponibilidad de un voluntario específico
    @GetMapping("/volunteer/{id}/availability")
    public ResponseEntity<List<Map<String, Object>>> getVolunteerAvailability(@PathVariable Long id) {
        Optional<Voluntario> voluntarioOpt = voluntarioRepository.findById(Objects.requireNonNull(id));
        if (voluntarioOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Añadir solicitudes (Pendientes y Aceptadas) primero para identificar horarios ocupados
        List<SolicitudCupo> solicitudes = solicitudCupoRepository.findByHorario_Voluntario(voluntarioOpt.get());
        java.util.Set<Long> horariosConSolicitudIds = new java.util.HashSet<>();
        
        List<Map<String, Object>> events = new ArrayList<>();

        for (SolicitudCupo s : solicitudes) {
            if ("RECHAZADA".equals(s.getEstado())) continue;
            
            horariosConSolicitudIds.add(s.getHorario().getId());

            Map<String, Object> event = new HashMap<>();
            event.put("id", "sol-" + s.getId());
            event.put("title", "Tutoría: " + s.getEstudiante().getUsername());
            event.put("start", s.getHorario().getFecha() + "T" + s.getHorario().getHoraInicio());
            event.put("end", s.getHorario().getFecha() + "T" + s.getHorario().getHoraFin());
            
            if ("ACEPTADA".equals(s.getEstado())) {
                event.put("color", "#6366f1"); // Indigo
                event.put("extendedProps", Map.of("materia", s.getHorario().getMateria(), "tipo", "ocupado", "estado", "ACEPTADA"));
            } else {
                event.put("color", "#f59e0b"); // Amber (Pendiente)
                event.put("extendedProps", Map.of("materia", s.getHorario().getMateria(), "tipo", "ocupado", "estado", "PENDIENTE"));
            }
            event.put("textColor", "white");
            events.add(event);
        }

        // Añadir horarios disponibles SOLO si no tienen una solicitud activa
        List<HorarioDisponible> horarios = horarioDisponibleRepository.findByVoluntario(voluntarioOpt.get());
        for (HorarioDisponible h : horarios) {
            if (horariosConSolicitudIds.contains(h.getId())) continue;

            Map<String, Object> event = new HashMap<>();
            event.put("id", h.getId());
            event.put("title", "Disponible: " + h.getMateria());
            event.put("start", h.getFecha() + "T" + h.getHoraInicio());
            event.put("end", h.getFecha() + "T" + h.getHoraFin());
            event.put("color", "#10b981"); // Verde esmeralda Tailwind
            event.put("textColor", "white");
            event.put("extendedProps", Map.of("materia", h.getMateria(), "tipo", "disponible"));
            events.add(event);
        }

        // Añadir horarios de aula del voluntario
        List<HorarioAula> horariosAula = horarioAulaRepository.findByAula_Voluntario(voluntarioOpt.get());
        for (HorarioAula ha : horariosAula) {
            Map<String, Object> event = new HashMap<>();
            event.put("id", "aula-" + ha.getId());
            event.put("title", "Aula: " + ha.getAula().getNombreAula());
            event.put("start", ha.getFecha() + "T" + ha.getHoraInicio());
            event.put("end", ha.getFecha() + "T" + ha.getHoraFin());
            event.put("color", "#8b5cf6"); // Violeta Tailwind
            event.put("textColor", "white");
            event.put("extendedProps", Map.of("aula", ha.getAula().getNombreAula(), "tipo", "aula"));
            events.add(event);
        }

        return ResponseEntity.ok(events);
    }

    // Retorna la disponibilidad del voluntario autenticado
    @GetMapping("/volunteer/my-availability")
    public ResponseEntity<List<Map<String, Object>>> getMyAvailability(Principal principal) {
        if (principal == null) return ResponseEntity.status(401).build();

        Optional<Voluntario> voluntarioOpt = voluntarioRepository.findByUsuario_Username(principal.getName());
        if (voluntarioOpt.isEmpty()) return ResponseEntity.notFound().build();

        return getVolunteerAvailability(voluntarioOpt.get().getId());
    }

    // Retorna las clases de un estudiante
    @GetMapping("/student/events")
    public ResponseEntity<List<Map<String, Object>>> getStudentEvents(Principal principal) {
        if (principal == null) return ResponseEntity.status(401).build();

        Optional<Usuario> estudianteOpt = usuarioRepository.findByUsername(principal.getName());
        if (estudianteOpt.isEmpty()) return ResponseEntity.notFound().build();

        List<SolicitudCupo> solicitudes = solicitudCupoRepository.findByEstudiante(estudianteOpt.get());
        List<Map<String, Object>> events = new ArrayList<>();

        for (SolicitudCupo s : solicitudes) {
            Map<String, Object> event = new HashMap<>();
            event.put("id", "sol-" + s.getId());
            event.put("title", "Tutoría: " + s.getHorario().getMateria());
            event.put("start", s.getHorario().getFecha() + "T" + s.getHorario().getHoraInicio());
            event.put("end", s.getHorario().getFecha() + "T" + s.getHorario().getHoraFin());
            
            if ("ACEPTADA".equals(s.getEstado())) {
                event.put("color", "#6366f1"); // Indigo
            } else if ("PENDIENTE".equals(s.getEstado())) {
                event.put("color", "#f59e0b"); // Amber
            } else {
                event.put("color", "#ef4444"); // Red
            }
            
            event.put("textColor", "white");
            event.put("extendedProps", Map.of("estado", s.getEstado(), "voluntario", s.getHorario().getVoluntario().getUsuario().getUsername()));
            events.add(event);
        }

        return ResponseEntity.ok(events);
    }
}
