package aulasenora.controller;

import aulasenora.model.MensajeGlobal;
import aulasenora.model.Usuario;
import aulasenora.repository.AulaRepository;
import aulasenora.repository.MensajeGlobalRepository;
import aulasenora.repository.UsuarioRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import aulasenora.model.Voluntario;
import aulasenora.repository.VoluntarioRepository;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UsuarioRepository usuarioRepository;
    private final AulaRepository aulaRepository;
    private final MensajeGlobalRepository mensajeGlobalRepository;
    private final VoluntarioRepository voluntarioRepository;

    public AdminController(UsuarioRepository usuarioRepository, AulaRepository aulaRepository, MensajeGlobalRepository mensajeGlobalRepository, VoluntarioRepository voluntarioRepository) {
        this.usuarioRepository = usuarioRepository;
        this.aulaRepository = aulaRepository;
        this.mensajeGlobalRepository = mensajeGlobalRepository;
        this.voluntarioRepository = voluntarioRepository;
    }

    @GetMapping("/dashboard")
    public String adminDashboard(@RequestParam(required = false, defaultValue = "resumen") String tab, Model model) {
        model.addAttribute("activeTab", tab);
        model.addAttribute("isDashboard", true);

        List<Voluntario> voluntarios = voluntarioRepository.findAll();
        Map<Long, Voluntario> voluntarioMap = new HashMap<>();
        for (Voluntario v : voluntarios) {
            voluntarioMap.put(v.getId(), v);
        }
        model.addAttribute("voluntarios", voluntarios);
        model.addAttribute("voluntarioMap", voluntarioMap);

        model.addAttribute("totalEstudiantes", usuarioRepository.countByRol("ESTUDIANTE"));
        model.addAttribute("totalVoluntarios", (long) voluntarios.size());
        model.addAttribute("totalTutorias", aulaRepository.count());
        model.addAttribute("reportesPendientes", 0);

        List<Usuario> usuarios = usuarioRepository.findByRolNot("ADMIN");
        model.addAttribute("usuarios", usuarios);

        List<MensajeGlobal> mensajes = mensajeGlobalRepository.findByActivoTrueOrderByFechaCreacionDesc();
        model.addAttribute("mensajesGlobales", mensajes);

        return "admin/dashboard";
    }

    @PostMapping("/users/{id}/toggle-status")
    public String toggleUserStatus(@PathVariable Long id) {
        Optional<Usuario> userOpt = usuarioRepository.findById(id);
        if (userOpt.isPresent()) {
            Usuario user = userOpt.get();
            // Evitar que el admin se desactive a sí mismo por accidente
            if (!"ADMIN".equals(user.getRol())) {
                user.setActivo(!user.isActivo());
                usuarioRepository.save(user);
            }
        }
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/messages")
    public String createMessage(@RequestParam String titulo, @RequestParam String contenido) {
        MensajeGlobal msj = new MensajeGlobal();
        msj.setTitulo(titulo.trim());
        msj.setContenido(contenido.trim());
        mensajeGlobalRepository.save(msj);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/messages/{id}/toggle")
    public String toggleMessage(@PathVariable Long id) {
        Optional<MensajeGlobal> msjOpt = mensajeGlobalRepository.findById(id);
        if (msjOpt.isPresent()) {
            MensajeGlobal msj = msjOpt.get();
            msj.setActivo(!msj.getActivo());
            mensajeGlobalRepository.save(msj);
        }
        return "redirect:/admin/dashboard?tab=mensajes";
    }

    @PostMapping("/users/{id}/verify")
    public String verifyVolunteer(@PathVariable Long id) {
        Optional<Voluntario> volOpt = voluntarioRepository.findById(id);
        if (volOpt.isPresent()) {
            Voluntario vol = volOpt.get();
            vol.setVerificado(true);
            voluntarioRepository.save(vol);
        }
        return "redirect:/admin/dashboard?tab=usuarios";
    }

    @GetMapping("/report/users")
    public void downloadUsersReport(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"reporte_usuarios.csv\"");
        
        PrintWriter writer = response.getWriter();
        writer.println("ID,Nombre,Apellido,Email,Rol,Activo,Verificado(Solo Voluntarios)");
        
        List<Usuario> usuarios = usuarioRepository.findAll();
        for (Usuario u : usuarios) {
            String verificado = "N/A";
            if ("VOLUNTARIO".equals(u.getRol())) {
                var volOpt = voluntarioRepository.findById(u.getId());
                verificado = volOpt.map(v -> v.isVerificado() != null && v.isVerificado() ? "SI" : "NO").orElse("NO");
            }
            writer.printf("%d,%s,%s,%s,%s,%s,%s\n", 
                u.getId(), 
                u.getFirstName(), 
                u.getLastName(), 
                u.getEmail(), 
                u.getRol(), 
                u.isActivo() ? "SI" : "NO",
                verificado
            );
        }
    }
}
