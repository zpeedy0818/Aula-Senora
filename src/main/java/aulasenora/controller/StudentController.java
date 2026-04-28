package aulasenora.controller;

import aulasenora.model.HorarioDisponible;
import aulasenora.model.SolicitudCupo;
import aulasenora.model.Usuario;
import aulasenora.repository.HorarioDisponibleRepository;
import aulasenora.repository.SolicitudCupoRepository;
import aulasenora.repository.UsuarioRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentController {

    private final UsuarioRepository usuarioRepository;
    private final HorarioDisponibleRepository horarioDisponibleRepository;
    private final SolicitudCupoRepository solicitudCupoRepository;

    public StudentController(UsuarioRepository usuarioRepository, HorarioDisponibleRepository horarioDisponibleRepository, SolicitudCupoRepository solicitudCupoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.horarioDisponibleRepository = horarioDisponibleRepository;
        this.solicitudCupoRepository = solicitudCupoRepository;
    }

    @GetMapping("/dashboard")
    public String studentDashboard(Principal principal, Model model) {
        if (principal == null) return "redirect:/login";

        // Obtener todos los horarios disponibles
        List<HorarioDisponible> horarios = horarioDisponibleRepository.findAll();
        model.addAttribute("horarios", horarios);

        return "student/dashboard";
    }

    @PostMapping("/request-slot")
    public String requestSlot(@RequestParam Long horarioId, @RequestParam String mensaje, Principal principal) {
        if (principal == null) return "redirect:/login";

        usuarioRepository.findByUsernameOrEmail(principal.getName(), principal.getName()).ifPresent(estudiante -> {
            horarioDisponibleRepository.findById(horarioId).ifPresent(horario -> {
                SolicitudCupo solicitud = new SolicitudCupo();
                solicitud.setEstudiante(estudiante);
                solicitud.setHorarioDisponible(horario);
                solicitud.setMensaje(mensaje);
                solicitud.setEstado("PENDIENTE");
                solicitudCupoRepository.save(solicitud);
            });
        });

        return "redirect:/student/dashboard?requestSent=true";
    }
}
