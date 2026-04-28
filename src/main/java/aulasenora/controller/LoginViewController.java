package aulasenora.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginViewController {

    @GetMapping("/login")
    public String mostrarLogin(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            @RequestParam(value = "registerSuccess", required = false) String registerSuccess,
            Model model) {

        if (error != null) {
            model.addAttribute("error", "Usuario o contraseña inválidos");
        }
        if (logout != null) {
            model.addAttribute("mensaje", "Has cerrado sesión correctamente");
        }
        if (registerSuccess != null) {
            model.addAttribute("mensaje", "Registro exitoso, por favor inicia sesión");
        }
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard(org.springframework.security.core.Authentication auth) {
        String rol = auth.getAuthorities().stream()
                .map(gr -> gr.getAuthority())
                .filter(r -> r.startsWith("ROLE_"))
                .map(r -> r.substring(5))
                .findFirst()
                .orElse("ESTUDIANTE");

        return switch (rol) {
            case "ADMIN" -> "redirect:/admin/dashboard";
            case "VOLUNTARIO" -> "redirect:/volunteer/dashboard";
            default -> "redirect:/student/dashboard";
        };
    }

    @GetMapping("/")
    public String rootRedirect(org.springframework.security.core.Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated() && 
            !(authentication instanceof org.springframework.security.authentication.AnonymousAuthenticationToken)) {
            return "redirect:/dashboard";
        }
        return "index";
    }
}
