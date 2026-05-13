package aulasenora.controller;

import aulasenora.model.HorarioDisponible;
import aulasenora.repository.HorarioDisponibleRepository;
import aulasenora.model.SolicitudCupo;
import aulasenora.repository.SolicitudCupoRepository;
import aulasenora.repository.VoluntarioRepository;
import aulasenora.service.AulaService;
import aulasenora.service.UsuarioService;
import aulasenora.model.Aula;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import aulasenora.dto.PerfilVoluntarioDTO;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/volunteer")
public class VolunteerController {

    private final VoluntarioRepository voluntarioRepository;
    private final HorarioDisponibleRepository horarioDisponibleRepository;
    private final SolicitudCupoRepository solicitudCupoRepository;
    private final AulaService aulaService;
    private final UsuarioService usuarioService;

    public VolunteerController(VoluntarioRepository voluntarioRepository, HorarioDisponibleRepository horarioDisponibleRepository, SolicitudCupoRepository solicitudCupoRepository, AulaService aulaService, UsuarioService usuarioService) {
        this.voluntarioRepository = voluntarioRepository;
        this.horarioDisponibleRepository = horarioDisponibleRepository;
        this.solicitudCupoRepository = solicitudCupoRepository;
        this.aulaService = aulaService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/dashboard")
    public String dashboard(@RequestParam(required = false, defaultValue = "inicio") String tab, Principal principal, Model model) {
        if (principal == null) return "redirect:/login";

        String username = principal.getName();
        var voluntarioOpt = voluntarioRepository.findByUsuario_Username(username);
        
        if (voluntarioOpt.isEmpty()) {
            return "redirect:/";
        }
        
        var voluntario = voluntarioOpt.get();
        model.addAttribute("voluntario", voluntario);
        
        // Solo mostrar solicitudes pendientes y las últimas 5 procesadas para evitar que la página sea "pesada"
        List<SolicitudCupo> todasLasSolicitudes = solicitudCupoRepository.findByHorario_Voluntario(voluntario);
        List<SolicitudCupo> solicitudesPendientes = todasLasSolicitudes.stream()
                .filter(s -> "PENDIENTE".equals(s.getEstado()))
                .sorted((a, b) -> {
                    LocalDateTime fa = a.getFechaSolicitud() != null ? a.getFechaSolicitud() : LocalDateTime.MIN;
                    LocalDateTime fb = b.getFechaSolicitud() != null ? b.getFechaSolicitud() : LocalDateTime.MIN;
                    return fb.compareTo(fa);
                })
                .toList();
        
        List<SolicitudCupo> solicitudesRecientes = todasLasSolicitudes.stream()
                .filter(s -> !"PENDIENTE".equals(s.getEstado()))
                .sorted((a, b) -> {
                    LocalDateTime fa = a.getFechaSolicitud() != null ? a.getFechaSolicitud() : LocalDateTime.MIN;
                    LocalDateTime fb = b.getFechaSolicitud() != null ? b.getFechaSolicitud() : LocalDateTime.MIN;
                    return fb.compareTo(fa);
                })
                .limit(5)
                .toList();

        model.addAttribute("solicitudes", solicitudesPendientes);
        model.addAttribute("solicitudesHistorial", solicitudesRecientes);
        
        // Aulas creadas por el voluntario
        List<Aula> misAulas = aulaService.getAulasByVoluntario(username);
        model.addAttribute("misAulas", misAulas);
        
        model.addAttribute("activeTab", tab);

        return "volunteer/dashboard";
    }

    @GetMapping("/profile")
    public String profile(Principal principal, Model model) {
        if (principal == null) return "redirect:/login";

        voluntarioRepository.findByUsuario_Username(principal.getName()).ifPresent(voluntario -> {
            PerfilVoluntarioDTO dto = new PerfilVoluntarioDTO();
            dto.setFirstName(voluntario.getUsuario().getFirstName());
            dto.setLastName(voluntario.getUsuario().getLastName());
            dto.setEmail(voluntario.getUsuario().getEmail());
            dto.setInstitution(voluntario.getInstitution());
            dto.setSkills(voluntario.getSkills());
            dto.setMateriaEspecializada(voluntario.getMateriaEspecializada());
            
            model.addAttribute("perfilDTO", dto);
            model.addAttribute("username", voluntario.getUsuario().getUsername());
        });

        return "volunteer/profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@Valid @ModelAttribute("perfilDTO") PerfilVoluntarioDTO perfilDTO,
                                org.springframework.validation.BindingResult bindingResult,
                                Principal principal,
                                Model model) {
        if (principal == null) return "redirect:/login";

        if (bindingResult.hasErrors()) {
            model.addAttribute("username", principal.getName());
            return "volunteer/profile";
        }

        try {
            usuarioService.actualizarPerfilVoluntario(principal.getName(), perfilDTO);
            return "redirect:/volunteer/profile?success=true";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("username", principal.getName());
            return "volunteer/profile";
        }
    }

    @GetMapping("/schedule")
    public String volunteerSchedule(Principal principal, Model model) {
        if (principal == null) return "redirect:/login";

        String username = principal.getName();
        voluntarioRepository.findByUsuario_Username(username).ifPresent(voluntario -> {
            model.addAttribute("voluntario", voluntario);
        });

        return "volunteer/calendar";
    }

    @PostMapping("/schedule/add")
    public String addSchedule(
            @RequestParam String fecha,
            @RequestParam String horaInicio,
            @RequestParam String horaFin,
            @RequestParam String materia,
            Principal principal) {
        
        if (principal == null) return "redirect:/login";

        LocalTime start = LocalTime.parse(horaInicio);
        LocalTime end = LocalTime.parse(horaFin);

        if (end.isBefore(start) || end.equals(start)) {
            return "redirect:/volunteer/schedule?error=time";
        }

        voluntarioRepository.findByUsuario_Username(principal.getName()).ifPresent(voluntario -> {
            HorarioDisponible horario = new HorarioDisponible();
            horario.setVoluntario(voluntario);
            horario.setFecha(java.time.LocalDate.parse(fecha));
            horario.setHoraInicio(start);
            horario.setHoraFin(end);
            horario.setMateria(materia);
            horarioDisponibleRepository.save(horario);
        });

        return "redirect:/volunteer/schedule?scheduleAdded=true";
    }

    @PostMapping("/schedule/delete/{id}")
    public String deleteSchedule(@PathVariable long id, Principal principal) {
        if (principal == null) return "redirect:/login";

        horarioDisponibleRepository.findById(id).ifPresent(horario -> {
            // Verificar que el horario pertenezca al voluntario actual
            if (horario.getVoluntario().getUsuario().getUsername().equals(principal.getName())) {
                // Eliminar solicitudes asociadas primero para evitar error de integridad referencial
                List<SolicitudCupo> solicitudes = solicitudCupoRepository.findByHorario(horario);
                if (!solicitudes.isEmpty()) {
                    solicitudCupoRepository.deleteAll(solicitudes);
                }
                horarioDisponibleRepository.delete(horario);
            }
        });

        return "redirect:/volunteer/schedule?scheduleDeleted=true";
    }

    @PostMapping("/request/{id}/status")
    public String updateRequestStatus(@PathVariable long id, @RequestParam String status, @RequestParam(required = false) Boolean fromCalendar, Principal principal) {
        if (principal == null) return "redirect:/login";

        solicitudCupoRepository.findById(id).ifPresent(solicitud -> {
            // Check if current user is the volunteer for this request
            if (solicitud.getHorario().getVoluntario().getUsuario().getUsername().equals(principal.getName())) {
                if ("ACEPTADA".equals(status) || "RECHAZADA".equals(status)) {
                    solicitud.setEstado(status);
                    solicitudCupoRepository.save(solicitud);
                }
            }
        });

        if (fromCalendar != null && fromCalendar) {
            return "redirect:/volunteer/schedule?statusUpdated=true";
        }
        return "redirect:/volunteer/dashboard?statusUpdated=true";
    }
}
