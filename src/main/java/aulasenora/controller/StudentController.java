package aulasenora.controller;

import aulasenora.model.Aula;
import aulasenora.model.MiembroAula;
import aulasenora.model.SolicitudCupo;
import aulasenora.repository.HorarioDisponibleRepository;
import aulasenora.repository.SolicitudCupoRepository;
import aulasenora.repository.UsuarioRepository;
import aulasenora.service.AulaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/student")
public class StudentController {

    private final UsuarioRepository usuarioRepository;
    private final HorarioDisponibleRepository horarioDisponibleRepository;
    private final SolicitudCupoRepository solicitudCupoRepository;
    private final AulaService aulaService;

    public StudentController(UsuarioRepository usuarioRepository, HorarioDisponibleRepository horarioDisponibleRepository, SolicitudCupoRepository solicitudCupoRepository, AulaService aulaService) {
        this.usuarioRepository = usuarioRepository;
        this.horarioDisponibleRepository = horarioDisponibleRepository;
        this.solicitudCupoRepository = solicitudCupoRepository;
        this.aulaService = aulaService;
    }

    @GetMapping("/dashboard")
    public String studentDashboard(Principal principal, Model model, @RequestParam(required = false, defaultValue = "inicio") String tab) {
        if (principal == null) return "redirect:/login";

        model.addAttribute("activeTab", tab);
        model.addAttribute("isDashboard", true);

        // Aquí pasaremos solo información de resumen para el panel principal en el futuro
        // Por ahora, el dashboard cargará rápido sin la pesada lógica de horarios.
        
        String username = principal.getName();
        usuarioRepository.findByUsername(username).ifPresent(usuario -> model.addAttribute("usuario", usuario));
        
        List<MiembroAula> misAulas = aulaService.getAulasByEstudiante(username);
        model.addAttribute("misAulas", misAulas);

        // Filter out aulas the student is already a member of
        java.util.Set<Long> misAulaIds = misAulas.stream()
                .map(m -> m.getAula().getId())
                .collect(Collectors.toSet());
        List<Aula> aulasDisponibles = aulaService.getAllAulas().stream()
                .filter(a -> !misAulaIds.contains(a.getId()))
                .collect(Collectors.toList());
        model.addAttribute("aulasDisponibles", aulasDisponibles);

        // Map of aulaId -> solicitud estado for this student
        java.util.Map<Long, String> misSolicitudesAula = aulaService.getSolicitudesAulaPorEstudiante(username);
        model.addAttribute("misSolicitudesAula", misSolicitudesAula);

        model.addAttribute("solicitudesCompletas", aulaService.getSolicitudesCompletasPorEstudiante(username));

        return "student/dashboard";
    }

    @GetMapping("/schedule")
    public String studentSchedule(Principal principal, Model model) {
        if (principal == null) return "redirect:/login";

        String username = principal.getName();
        
        // Obtener solo las aulas en las que el estudiante ya está inscrito
        List<MiembroAula> misAulas = aulaService.getAulasByEstudiante(username);
        
        // Extraer los voluntarios de esas aulas (usando un set para evitar duplicados si un voluntario tiene varias aulas)
        List<aulasenora.model.Voluntario> voluntariosInscritos = misAulas.stream()
                .map(miembro -> miembro.getAula().getVoluntario())
                .distinct()
                .collect(Collectors.toList());
                
        model.addAttribute("misAulasInscritas", misAulas);
        model.addAttribute("voluntarios", voluntariosInscritos);

        return "student/calendar";
    }

    @PostMapping("/request-slot")
    public String requestSlot(@RequestParam long horarioId, @RequestParam String mensaje, Principal principal) {
        if (principal == null) return "redirect:/login";

        usuarioRepository.findByUsername(principal.getName()).ifPresent(estudiante -> {
            horarioDisponibleRepository.findById(horarioId).ifPresent(horario -> {
                SolicitudCupo solicitud = new SolicitudCupo();
                solicitud.setEstudiante(estudiante);
                solicitud.setHorario(horario);
                solicitud.setMensaje(mensaje);
                solicitud.setEstado("PENDIENTE");
                solicitudCupoRepository.save(solicitud);
            });
        });

        return "redirect:/student/schedule?requestSent=true";
    }
}
