package aulasenora.controller;

import aulasenora.model.Aula;
import aulasenora.model.MiembroAula;
import aulasenora.model.SolicitudAula;
import aulasenora.model.HorarioAula;
import aulasenora.model.SolicitudHorarioAula;
import aulasenora.service.AulaService;
import aulasenora.service.HorarioAulaService;
import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class AulaController {

    private final AulaService aulaService;
    private final HorarioAulaService horarioAulaService;

    public AulaController(AulaService aulaService, HorarioAulaService horarioAulaService) {
        this.aulaService = aulaService;
        this.horarioAulaService = horarioAulaService;
    }

    // --- VOLUNTEER ENDPOINTS ---

    @PostMapping("/volunteer/aulas/create")
    public String createAula(@ModelAttribute Aula aula, Authentication authentication, RedirectAttributes redirectAttributes) {
        try {
            aulaService.crearAula(aula, authentication.getName());
            redirectAttributes.addFlashAttribute("successMessage", "Aula creada exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al crear el aula: " + e.getMessage());
        }
        return "redirect:/volunteer/dashboard";
    }

    @GetMapping("/volunteer/aulas/{id}")
    public String viewAulaDetailVolunteer(@PathVariable Long id, Model model) {
        Aula aula = aulaService.getAulaById(id);
        List<SolicitudAula> solicitudesPendientes = aulaService.getSolicitudesPendientesByAula(id);
        List<MiembroAula> miembros = aulaService.getMiembrosByAula(id);
        List<HorarioAula> horarios = horarioAulaService.getHorariosByAula(id);
        List<SolicitudHorarioAula> solicitudesHorarios = horarioAulaService.getSolicitudesByAula(id);

        model.addAttribute("aula", aula);
        model.addAttribute("solicitudesPendientes", solicitudesPendientes);
        model.addAttribute("miembros", miembros);
        model.addAttribute("horarios", horarios);
        model.addAttribute("solicitudesHorarios", solicitudesHorarios);
        return "volunteer/aula-detail";
    }

    @PostMapping("/volunteer/aulas/requests/{id}/approve")
    public String approveRequest(@PathVariable Long id, @RequestParam Long aulaId, RedirectAttributes redirectAttributes) {
        try {
            aulaService.aprobarSolicitud(id);
            redirectAttributes.addFlashAttribute("successMessage", "Solicitud aprobada.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/volunteer/aulas/" + aulaId;
    }

    @PostMapping("/volunteer/aulas/requests/{id}/reject")
    public String rejectRequest(@PathVariable Long id, @RequestParam Long aulaId, RedirectAttributes redirectAttributes) {
        try {
            aulaService.rechazarSolicitud(id);
            redirectAttributes.addFlashAttribute("successMessage", "Solicitud rechazada.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/volunteer/aulas/" + aulaId;
    }

    @PostMapping("/volunteer/aulas/{id}/delete")
    public String deleteAula(@PathVariable Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
        try {
            aulaService.eliminarAula(id, authentication.getName());
            redirectAttributes.addFlashAttribute("successMessage", "Aula eliminada exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar el aula: " + e.getMessage());
        }
        return "redirect:/volunteer/dashboard";
    }

    @PostMapping("/volunteer/aulas/{id}/estudiantes/{estudianteId}/delete")
    public String removeEstudiante(@PathVariable Long id, @PathVariable Long estudianteId, Authentication authentication, RedirectAttributes redirectAttributes) {
        try {
            aulaService.eliminarEstudianteDeAula(id, estudianteId, authentication.getName());
            redirectAttributes.addFlashAttribute("successMessage", "Estudiante eliminado del aula exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar al estudiante: " + e.getMessage());
        }
        return "redirect:/volunteer/aulas/" + id;
    }

    // --- HORARIOS VOLUNTEER ENDPOINTS ---

    @PostMapping("/volunteer/aulas/{id}/horarios/create")
    public String createHorario(@PathVariable Long id, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha, @RequestParam String horaInicio, @RequestParam String horaFin, @RequestParam String materia, @RequestParam(defaultValue = "false") boolean esGrupal, Authentication authentication, RedirectAttributes redirectAttributes) {
        try {
            LocalTime start = LocalTime.parse(horaInicio);
            LocalTime end = LocalTime.parse(horaFin);
            
            if (end.isBefore(start) || end.equals(start)) {
                redirectAttributes.addFlashAttribute("errorMessage", "Error: La hora de fin debe ser posterior a la hora de inicio.");
                return "redirect:/volunteer/aulas/" + id;
            }

            horarioAulaService.crearHorario(id, fecha, start, end, materia, authentication.getName(), esGrupal);
            redirectAttributes.addFlashAttribute("successMessage", "Horario creado exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al crear horario: " + e.getMessage());
        }
        return "redirect:/volunteer/aulas/" + id;
    }

    @PostMapping("/volunteer/aulas/{id}/horarios/{horarioId}/delete")
    public String deleteHorario(@PathVariable Long id, @PathVariable Long horarioId, Authentication authentication, RedirectAttributes redirectAttributes) {
        try {
            horarioAulaService.eliminarHorario(horarioId, authentication.getName());
            redirectAttributes.addFlashAttribute("successMessage", "Horario eliminado.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar horario: " + e.getMessage());
        }
        return "redirect:/volunteer/aulas/" + id;
    }

    @PostMapping("/volunteer/aulas/horarios/requests/{solicitudId}/approve")
    public String approveHorarioRequest(@PathVariable Long solicitudId, @RequestParam Long aulaId, Authentication authentication, RedirectAttributes redirectAttributes) {
        try {
            horarioAulaService.aprobarSolicitud(solicitudId, authentication.getName());
            redirectAttributes.addFlashAttribute("successMessage", "Solicitud de horario aprobada.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/volunteer/aulas/" + aulaId;
    }

    @PostMapping("/volunteer/aulas/horarios/requests/{solicitudId}/reject")
    public String rejectHorarioRequest(@PathVariable Long solicitudId, @RequestParam Long aulaId, Authentication authentication, RedirectAttributes redirectAttributes) {
        try {
            horarioAulaService.rechazarSolicitud(solicitudId, authentication.getName());
            redirectAttributes.addFlashAttribute("successMessage", "Solicitud de horario rechazada.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/volunteer/aulas/" + aulaId;
    }

    // --- STUDENT ENDPOINTS ---

    @GetMapping("/student/aulas/explore")
    public String exploreAulas(Model model) {
        List<Aula> todasLasAulas = aulaService.getAllAulas();
        model.addAttribute("aulasDisponibles", todasLasAulas);
        return "student/explore-aulas"; // We can create a new view or list them in dashboard. We'll list them in dashboard or a new view.
    }

    @PostMapping("/student/aulas/{id}/request")
    public String requestAccess(@PathVariable Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
        try {
            aulaService.solicitarAcceso(id, authentication.getName());
            redirectAttributes.addFlashAttribute("successMessage", "Solicitud de acceso enviada.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/student/dashboard";
    }

    @GetMapping("/student/aulas/{id}")
    public String viewAulaDetailStudent(@PathVariable Long id, Model model, Authentication authentication) {
        Aula aula = aulaService.getAulaById(id);
        
        // Verify if student is member
        List<MiembroAula> misAulas = aulaService.getAulasByEstudiante(authentication.getName());
        boolean isMember = misAulas.stream().anyMatch(m -> m.getAula().getId().equals(id) && "ESTUDIANTE".equals(m.getRol()));
        
        if (!isMember) {
            return "redirect:/student/dashboard";
        }
        
        List<HorarioAula> horarios = horarioAulaService.getHorariosByAula(id);
        
        // Build a map of horarioId -> student's request status for this student
        java.util.Map<Long, String> misSolicitudes = horarioAulaService.getEstadoSolicitudesPorEstudiante(id, authentication.getName());
        
        model.addAttribute("aula", aula);
        model.addAttribute("horarios", horarios);
        model.addAttribute("misSolicitudes", misSolicitudes);
        return "student/aula-detail";
    }

    // --- HORARIOS STUDENT ENDPOINTS ---

    @PostMapping("/student/aulas/horarios/{horarioId}/request")
    public String requestHorarioAula(@PathVariable Long horarioId, @RequestParam Long aulaId, Authentication authentication, RedirectAttributes redirectAttributes) {
        try {
            horarioAulaService.solicitarHorario(horarioId, authentication.getName());
            redirectAttributes.addFlashAttribute("successMessage", "Horario solicitado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/student/aulas/" + aulaId;
    }
}
