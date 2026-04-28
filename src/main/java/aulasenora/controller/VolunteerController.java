package aulasenora.controller;

import aulasenora.model.HorarioDisponible;
import aulasenora.model.Usuario;
import aulasenora.model.Voluntario;
import aulasenora.repository.HorarioDisponibleRepository;
import aulasenora.repository.UsuarioRepository;
import aulasenora.repository.VoluntarioRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/volunteer")
public class VolunteerController {

    private final UsuarioRepository usuarioRepository;
    private final VoluntarioRepository voluntarioRepository;
    private final HorarioDisponibleRepository horarioDisponibleRepository;

    public VolunteerController(UsuarioRepository usuarioRepository, VoluntarioRepository voluntarioRepository, HorarioDisponibleRepository horarioDisponibleRepository) {
        this.usuarioRepository = usuarioRepository;
        this.voluntarioRepository = voluntarioRepository;
        this.horarioDisponibleRepository = horarioDisponibleRepository;
    }

    @GetMapping("/dashboard")
    public String dashboard(Principal principal, Model model) {
        if (principal == null) return "redirect:/login";

        String username = principal.getName();
        voluntarioRepository.findByUsuario_Username(username).ifPresent(voluntario -> {
            model.addAttribute("voluntario", voluntario);
            List<HorarioDisponible> horarios = horarioDisponibleRepository.findByVoluntario(voluntario);
            model.addAttribute("horarios", horarios);
        });

        return "volunteer/dashboard";
    }

    @PostMapping("/schedule/add")
    public String addSchedule(
            @RequestParam String diaSemana,
            @RequestParam String horaInicio,
            @RequestParam String horaFin,
            @RequestParam String materia,
            Principal principal) {
        
        if (principal == null) return "redirect:/login";

        voluntarioRepository.findByUsuario_Username(principal.getName()).ifPresent(voluntario -> {
            HorarioDisponible horario = new HorarioDisponible();
            horario.setVoluntario(voluntario);
            horario.setDiaSemana(diaSemana);
            horario.setHoraInicio(LocalTime.parse(horaInicio));
            horario.setHoraFin(LocalTime.parse(horaFin));
            horario.setMateria(materia);
            horarioDisponibleRepository.save(horario);
        });

        return "redirect:/volunteer/dashboard?scheduleAdded=true";
    }

    @PostMapping("/schedule/delete/{id}")
    public String deleteSchedule(@PathVariable Long id, Principal principal) {
        if (principal == null) return "redirect:/login";

        horarioDisponibleRepository.findById(id).ifPresent(horario -> {
            // Verificar que el horario pertenezca al voluntario actual
            if (horario.getVoluntario().getUsuario().getUsername().equals(principal.getName())) {
                horarioDisponibleRepository.delete(horario);
            }
        });

        return "redirect:/volunteer/dashboard?scheduleDeleted=true";
    }
}
