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
    public String dashboard(Model model) {
        // obtener rol principal sin prefijo ROLE_
        org.springframework.security.core.Authentication auth = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication();
        String rol = auth.getAuthorities().stream()
                .map(gr -> gr.getAuthority())
                .filter(r -> r.startsWith("ROLE_"))
                .map(r -> r.substring(5))
                .findFirst()
                .orElse("ESTUDIANTE");
        model.addAttribute("rol", rol);

        return switch (rol) {
            case "ADMIN" -> "admin/dashboard";
            case "VOLUNTARIO" -> "volunteer/dashboard";
            default -> "student/dashboard";
        };
    }

    @GetMapping("/")
    public String rootRedirect() {
        return "index";
    }
}
