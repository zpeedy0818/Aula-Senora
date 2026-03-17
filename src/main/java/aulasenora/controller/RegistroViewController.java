package aulasenora.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import aulasenora.dto.RegistroDTO;
import aulasenora.service.UsuarioService;
import aulasenora.service.RecaptchaService;

@Controller
@RequestMapping("/register")
public class RegistroViewController {

    private final UsuarioService usuarioService;
    private final RecaptchaService recaptchaService;

    public RegistroViewController(UsuarioService usuarioService, RecaptchaService recaptchaService) {
        this.usuarioService = usuarioService;
        this.recaptchaService = recaptchaService;
    }

    @GetMapping
    public String mostrarFormulario(Model model) {
        model.addAttribute("registroDTO", new RegistroDTO());
        return "register";
    }

    @PostMapping
    public String registrar(@Valid @ModelAttribute("registroDTO") RegistroDTO registroDTO,
                            org.springframework.validation.BindingResult bindingResult,
                            @RequestParam(name = "g-recaptcha-response", required = false) String recaptchaResponse,
                            HttpServletRequest request,
                            Model model) {
        
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Por favor completa el formulario correctamente.");
            return "register";
        }

        if (recaptchaResponse == null || recaptchaResponse.isEmpty()) {
            model.addAttribute("error", "Por favor, verifica que no eres un robot.");
            return "register";
        }

        boolean isValidCaptcha = recaptchaService.verifyRecaptcha(request.getRemoteAddr(), recaptchaResponse);
        if (!isValidCaptcha) {
            model.addAttribute("error", "Error en la validación reCAPTCHA. Inténtalo de nuevo.");
            return "register";
        }
        
        try {
            usuarioService.registrar(registroDTO);
            return "redirect:/login?registerSuccess";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }
}