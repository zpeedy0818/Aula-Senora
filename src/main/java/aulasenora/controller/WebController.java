package aulasenora.controller;

import aulasenora.model.MiembroAula;
import aulasenora.model.SolicitudAula;
import aulasenora.service.AulaService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class WebController {
    
    private final aulasenora.repository.UsuarioRepository usuarioRepository;
    private final AulaService aulaService;

    public WebController(aulasenora.repository.UsuarioRepository usuarioRepository,
                         AulaService aulaService) {
        this.usuarioRepository = usuarioRepository;
        this.aulaService = aulaService;
    }

    // Removing "/", "/login", "/register" because they are handled by
    // LoginViewController / RegistroController

    @GetMapping("/student/profile")
    public String studentProfile(java.security.Principal principal, org.springframework.ui.Model model) {
        if (principal != null) {
            usuarioRepository.findByUsername(principal.getName()).ifPresent(u -> {
                model.addAttribute("user", u);
                
                // Calculate profile completion percentage
                int totalFields = 8;
                int filledFields = 0;
                if (u.getFirstName() != null && !u.getFirstName().isBlank()) filledFields++;
                if (u.getLastName() != null && !u.getLastName().isBlank()) filledFields++;
                if (u.getEmail() != null && !u.getEmail().isBlank()) filledFields++;
                if (u.getBio() != null && !u.getBio().isBlank()) filledFields++;
                if (u.getPhoneNumber() != null && !u.getPhoneNumber().isBlank()) filledFields++;
                if (u.getBirthDate() != null) filledFields++;
                if (u.getGradoAcademico() != null && !u.getGradoAcademico().isBlank()) filledFields++;
                if (u.getInstitucion() != null && !u.getInstitucion().isBlank()) filledFields++;
                int completionPercent = (int) Math.round((filledFields * 100.0) / totalFields);
                model.addAttribute("profileCompletion", completionPercent);

                // Real stats: number of aulas the student is enrolled in
                List<MiembroAula> misAulas = aulaService.getAulasByEstudiante(principal.getName());
                long aulasCount = misAulas.stream()
                        .filter(m -> "ESTUDIANTE".equals(m.getRol()))
                        .count();
                model.addAttribute("aulasInscritas", aulasCount);

                // Real stats: solicitudes
                List<SolicitudAula> solicitudes = aulaService.getSolicitudesCompletasPorEstudiante(principal.getName());
                long solicitudesPendientes = solicitudes.stream()
                        .filter(s -> "PENDIENTE".equals(s.getEstado()))
                        .count();
                long solicitudesAprobadas = solicitudes.stream()
                        .filter(s -> "APROBADA".equals(s.getEstado()) || "ACEPTADA".equals(s.getEstado()))
                        .count();
                model.addAttribute("solicitudesPendientes", solicitudesPendientes);
                model.addAttribute("solicitudesAprobadas", solicitudesAprobadas);

                // Recent activity from solicitudes (last 5)
                List<SolicitudAula> recentActivity = solicitudes.stream()
                        .sorted((a, b) -> {
                            if (a.getFechaSolicitud() == null || b.getFechaSolicitud() == null) return 0;
                            return b.getFechaSolicitud().compareTo(a.getFechaSolicitud());
                        })
                        .limit(5)
                        .toList();
                model.addAttribute("recentActivity", recentActivity);
            });
        }
        return "student/profile";
    }

    @GetMapping("/student/history")
    public String studentHistory() {
        return "student/history";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(org.springframework.ui.Model model) {
        model.addAttribute("totalEstudiantes", 1248);
        model.addAttribute("totalVoluntarios", 312);
        model.addAttribute("totalTutorias", 8504);
        model.addAttribute("reportesPendientes", 3);
        return "admin/dashboard";
    }

    @PostMapping("/student/profile/update")
    public String updateProfile(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam(required = false) String bio,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) String birthDate,
            @RequestParam(required = false) String gradoAcademico,
            @RequestParam(required = false) String institucion,
            @RequestParam(required = false) String ciudad,
            java.security.Principal principal) {
        if (principal != null) {
            usuarioRepository.findByUsername(principal.getName())
                    .ifPresent(existingUser -> {
                        existingUser.setFirstName(firstName.trim().toUpperCase());
                        existingUser.setLastName(lastName.trim().toUpperCase());

                        if (bio != null && !bio.isBlank()) {
                            existingUser.setBio(bio.trim());
                        } else {
                            existingUser.setBio(null);
                        }
                        if (phoneNumber != null && !phoneNumber.isBlank()) {
                            existingUser.setPhoneNumber(phoneNumber.trim());
                        } else {
                            existingUser.setPhoneNumber(null);
                        }
                        if (birthDate != null && !birthDate.isBlank()) {
                            try {
                                existingUser.setBirthDate(LocalDate.parse(birthDate, DateTimeFormatter.ISO_LOCAL_DATE));
                            } catch (Exception ignored) { }
                        } else {
                            existingUser.setBirthDate(null);
                        }
                        if (gradoAcademico != null && !gradoAcademico.isBlank()) {
                            existingUser.setGradoAcademico(gradoAcademico.trim());
                        } else {
                            existingUser.setGradoAcademico(null);
                        }
                        if (institucion != null && !institucion.isBlank()) {
                            existingUser.setInstitucion(institucion.trim());
                        } else {
                            existingUser.setInstitucion(null);
                        }
                        if (ciudad != null && !ciudad.isBlank()) {
                            existingUser.setCiudad(ciudad.trim());
                        } else {
                            existingUser.setCiudad(null);
                        }

                        usuarioRepository.save(existingUser);
                    });
        }
        return "redirect:/student/profile?success=true";
    }
}
