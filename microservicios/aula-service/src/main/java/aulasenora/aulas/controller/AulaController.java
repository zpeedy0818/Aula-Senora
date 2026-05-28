package aulasenora.aulas.controller;

import aulasenora.aulas.model.*;
import aulasenora.aulas.service.AulaService;
import aulasenora.aulas.service.HorarioService;
import aulasenora.aulas.service.RecursoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/aulas")
public class AulaController {

    private final AulaService aulaService;
    private final HorarioService horarioService;
    private final RecursoService recursoService;

    public AulaController(AulaService aulaService, HorarioService horarioService,
                          RecursoService recursoService) {
        this.aulaService = aulaService;
        this.horarioService = horarioService;
        this.recursoService = recursoService;
    }

    private Long obtenerUsuarioId(Authentication auth) {
        return Long.parseLong(auth.getName());
    }

    @GetMapping
    public ResponseEntity<List<Aula>> listarTodas() {
        return ResponseEntity.ok(aulaService.listarTodas());
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Aula>> buscar(@RequestParam String q) {
        return ResponseEntity.ok(aulaService.buscarPorNombre(q));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Aula> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(aulaService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<Aula> crear(@RequestBody Aula aula, Authentication auth) {
        Long userId = obtenerUsuarioId(auth);
        return ResponseEntity.ok(aulaService.crearAula(aula, userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id, Authentication auth) {
        Long userId = obtenerUsuarioId(auth);
        aulaService.eliminarAula(id, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/voluntario")
    public ResponseEntity<List<Aula>> misAulas(Authentication auth) {
        Long userId = obtenerUsuarioId(auth);
        return ResponseEntity.ok(aulaService.listarPorVoluntario(userId));
    }

    @GetMapping("/estudiante")
    public ResponseEntity<List<MiembroAula>> aulasComoEstudiante(Authentication auth) {
        Long userId = obtenerUsuarioId(auth);
        return ResponseEntity.ok(aulaService.listarAulasPorEstudiante(userId));
    }

    @GetMapping("/{id}/miembros")
    public ResponseEntity<List<MiembroAula>> miembros(@PathVariable Long id) {
        return ResponseEntity.ok(aulaService.listarMiembros(id));
    }

    @GetMapping("/{id}/miembros/contar")
    public ResponseEntity<Long> contarMiembros(@PathVariable Long id) {
        return ResponseEntity.ok(aulaService.contarMiembros(id));
    }

    @GetMapping("/{id}/es-miembro")
    public ResponseEntity<Boolean> esMiembro(@PathVariable Long id, Authentication auth) {
        Long userId = obtenerUsuarioId(auth);
        return ResponseEntity.ok(aulaService.esMiembro(id, userId));
    }

    @PostMapping("/{id}/solicitar")
    public ResponseEntity<Void> solicitarAcceso(@PathVariable Long id, Authentication auth) {
        Long userId = obtenerUsuarioId(auth);
        aulaService.solicitarAcceso(id, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/solicitudes")
    public ResponseEntity<List<SolicitudAula>> solicitudesPendientes(@PathVariable Long id) {
        return ResponseEntity.ok(aulaService.solicitudesPendientes(id));
    }

    @GetMapping("/solicitudes")
    public ResponseEntity<List<SolicitudAula>> misSolicitudes(Authentication auth) {
        Long userId = obtenerUsuarioId(auth);
        return ResponseEntity.ok(aulaService.solicitudesPorEstudiante(userId));
    }

    @GetMapping("/solicitudes/estados")
    public ResponseEntity<Map<Long, String>> estadoSolicitudes(Authentication auth) {
        Long userId = obtenerUsuarioId(auth);
        return ResponseEntity.ok(aulaService.estadoSolicitudesPorEstudiante(userId));
    }

    @PostMapping("/solicitudes/{id}/aprobar")
    public ResponseEntity<Void> aprobarSolicitud(@PathVariable Long id) {
        aulaService.aprobarSolicitud(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/solicitudes/{id}/rechazar")
    public ResponseEntity<Void> rechazarSolicitud(@PathVariable Long id) {
        aulaService.rechazarSolicitud(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/estudiantes/{estudianteId}")
    public ResponseEntity<Void> eliminarEstudiante(@PathVariable Long id,
                                                    @PathVariable Long estudianteId,
                                                    Authentication auth) {
        Long userId = obtenerUsuarioId(auth);
        aulaService.eliminarEstudiante(id, estudianteId, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/horarios")
    public ResponseEntity<List<HorarioAula>> horarios(@PathVariable Long id) {
        return ResponseEntity.ok(horarioService.horariosPorAula(id));
    }

    @GetMapping("/{id}/horarios/rango")
    public ResponseEntity<List<HorarioAula>> horariosEnRango(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return ResponseEntity.ok(horarioService.horariosPorAulaYFecha(id, start, end));
    }

    @PostMapping("/{id}/horarios")
    public ResponseEntity<HorarioAula> crearHorario(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime horaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime horaFin,
            @RequestParam String materia,
            @RequestParam(required = false) Boolean esGrupal,
            Authentication auth) {
        Long userId = obtenerUsuarioId(auth);
        return ResponseEntity.ok(horarioService.crearHorario(id, fecha, horaInicio, horaFin, materia, userId, esGrupal));
    }

    @DeleteMapping("/horarios/{horarioId}")
    public ResponseEntity<Void> eliminarHorario(@PathVariable Long horarioId, Authentication auth) {
        Long userId = obtenerUsuarioId(auth);
        horarioService.eliminarHorario(horarioId, userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/horarios/{horarioId}/solicitar")
    public ResponseEntity<SolicitudHorarioAula> solicitarHorario(@PathVariable Long horarioId, Authentication auth) {
        Long userId = obtenerUsuarioId(auth);
        return ResponseEntity.ok(horarioService.solicitarHorario(horarioId, userId));
    }

    @GetMapping("/{id}/solicitudes-horario")
    public ResponseEntity<List<SolicitudHorarioAula>> solicitudesHorario(@PathVariable Long id) {
        return ResponseEntity.ok(horarioService.solicitudesPorAula(id));
    }

    @GetMapping("/horarios/{horarioId}/solicitudes")
    public ResponseEntity<List<SolicitudHorarioAula>> solicitudesPorHorario(@PathVariable Long horarioId) {
        return ResponseEntity.ok(horarioService.solicitudesPorHorario(horarioId));
    }

    @PostMapping("/solicitudes-horario/{solicitudId}/aprobar")
    public ResponseEntity<Void> aprobarSolicitudHorario(@PathVariable Long solicitudId, Authentication auth) {
        Long userId = obtenerUsuarioId(auth);
        horarioService.aprobarSolicitudHorario(solicitudId, userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/solicitudes-horario/{solicitudId}/rechazar")
    public ResponseEntity<Void> rechazarSolicitudHorario(@PathVariable Long solicitudId, Authentication auth) {
        Long userId = obtenerUsuarioId(auth);
        horarioService.rechazarSolicitudHorario(solicitudId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/solicitudes-horario/estados")
    public ResponseEntity<Map<Long, String>> estadoSolicitudesHorario(@PathVariable Long id, Authentication auth) {
        Long userId = obtenerUsuarioId(auth);
        return ResponseEntity.ok(horarioService.estadoSolicitudesHorario(id, userId));
    }

    @GetMapping("/{id}/recursos")
    public ResponseEntity<List<RecursoAula>> recursos(@PathVariable Long id) {
        return ResponseEntity.ok(recursoService.listarPorAula(id));
    }

    @GetMapping("/{id}/recursos/{recursoId}")
    public ResponseEntity<Void> eliminarRecurso(@PathVariable Long id, @PathVariable Long recursoId) {
        recursoService.eliminar(recursoId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/horarios-disponibles/voluntario")
    public ResponseEntity<List<HorarioDisponible>> horariosDisponibles(Authentication auth) {
        Long userId = obtenerUsuarioId(auth);
        return ResponseEntity.ok(horarioService.horariosDisponiblesPorVoluntario(userId));
    }

    @PostMapping("/horarios-disponibles")
    public ResponseEntity<HorarioDisponible> crearHorarioDisponible(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime horaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime horaFin,
            @RequestParam String materia,
            Authentication auth) {
        Long userId = obtenerUsuarioId(auth);
        return ResponseEntity.ok(horarioService.crearHorarioDisponible(userId, fecha, horaInicio, horaFin, materia));
    }

    @DeleteMapping("/horarios-disponibles/{id}")
    public ResponseEntity<Void> eliminarHorarioDisponible(@PathVariable Long id, Authentication auth) {
        Long userId = obtenerUsuarioId(auth);
        horarioService.eliminarHorarioDisponible(id, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/solicitudes-cupo/voluntario")
    public ResponseEntity<List<SolicitudCupo>> solicitudesCupoVoluntario(Authentication auth) {
        Long userId = obtenerUsuarioId(auth);
        return ResponseEntity.ok(horarioService.solicitudesCupoPorVoluntario(userId));
    }

    @GetMapping("/solicitudes-cupo/estudiante")
    public ResponseEntity<List<SolicitudCupo>> solicitudesCupoEstudiante(Authentication auth) {
        Long userId = obtenerUsuarioId(auth);
        return ResponseEntity.ok(horarioService.solicitudesCupoPorEstudiante(userId));
    }
}
