package aulasenora.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import aulasenora.model.Usuario;
import aulasenora.service.UsuarioService;

@Controller
@RequestMapping("/register")
public class RegistroViewController {

    private final UsuarioService usuarioService;

    public RegistroViewController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public String mostrarFormulario(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "register";
    }

    @PostMapping
    public String registrar(@ModelAttribute Usuario usuario, Model model) {
        try {
            // Check if role is provided (default to ESTUDIANTE if not)
            if (usuario.getRol() == null || usuario.getRol().isEmpty() || usuario.getRol().equals("USER")) {
                usuario.setRol("ESTUDIANTE");
            }

            usuarioService.registrar(usuario);
            return "redirect:/login?registerSuccess";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }
}