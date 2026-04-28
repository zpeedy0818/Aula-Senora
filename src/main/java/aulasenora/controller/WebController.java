package aulasenora.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class WebController {
    
    private final aulasenora.repository.UsuarioRepository usuarioRepository;

    public WebController(aulasenora.repository.UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // Removing "/", "/login", "/register" because they are handled by
    // LoginViewController / RegistroController

    @GetMapping("/student/dashboard")
    public String studentDashboard() {
        return "student/dashboard";
    }

    @GetMapping("/student/profile")
    public String studentProfile(java.security.Principal principal, org.springframework.ui.Model model) {
        if (principal != null) {
            usuarioRepository.findByUsernameOrEmail(principal.getName(), principal.getName())
                    .ifPresent(u -> model.addAttribute("user", u));
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
    @PostMapping("/student/profile/update")
    public String updateProfile(@ModelAttribute aulasenora.model.Usuario updatedUser, java.security.Principal principal) {
        if (principal != null) {
            usuarioRepository.findByUsernameOrEmail(principal.getName(), principal.getName())
                    .ifPresent(existingUser -> {
                        existingUser.setFirstName(updatedUser.getFirstName().trim().toUpperCase());
                        existingUser.setLastName(updatedUser.getLastName().trim().toUpperCase());
                        usuarioRepository.save(existingUser);
                    });
        }
        return "redirect:/student/profile?success=true";
    }
}
