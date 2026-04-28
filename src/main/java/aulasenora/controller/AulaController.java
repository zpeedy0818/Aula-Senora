package aulasenora.controller;

import aulasenora.model.Aula;
import aulasenora.model.MiembroAula;
import aulasenora.model.SolicitudAula;
import aulasenora.service.AulaService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class AulaController {

    private final AulaService aulaService;

    public AulaController(AulaService aulaService) {
        this.aulaService = aulaService;
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

        model.addAttribute("aula", aula);
        model.addAttribute("solicitudesPendientes", solicitudesPendientes);
        model.addAttribute("miembros", miembros);
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
        
        model.addAttribute("aula", aula);
        return "student/aula-detail";
    }
}
