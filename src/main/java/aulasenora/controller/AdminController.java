package aulasenora.controller;

import aulasenora.client.AdminServiceClient;
import aulasenora.client.JwtTokenProvider;
import aulasenora.model.MensajeGlobal;
import aulasenora.model.Usuario;
import aulasenora.repository.MensajeGlobalRepository;
import aulasenora.repository.UsuarioRepository;
import aulasenora.repository.VoluntarioRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UsuarioRepository usuarioRepository;
    private final MensajeGlobalRepository mensajeGlobalRepository;
    private final VoluntarioRepository voluntarioRepository;
    private final AdminServiceClient adminServiceClient;
    private final JwtTokenProvider jwtTokenProvider;

    public AdminController(UsuarioRepository usuarioRepository,
                           MensajeGlobalRepository mensajeGlobalRepository,
                           VoluntarioRepository voluntarioRepository,
                           AdminServiceClient adminServiceClient,
                           JwtTokenProvider jwtTokenProvider) {
        this.usuarioRepository = usuarioRepository;
        this.mensajeGlobalRepository = mensajeGlobalRepository;
        this.voluntarioRepository = voluntarioRepository;
        this.adminServiceClient = adminServiceClient;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("/dashboard")
    public String adminDashboard(@RequestParam(required = false, defaultValue = "resumen") String tab, Model model) {
        model.addAttribute("activeTab", tab);
        model.addAttribute("isDashboard", true);

        String token = jwtTokenProvider.generateToken("admin");
        adminServiceClient.getStats(token).ifPresent(stats -> {
            model.addAttribute("totalEstudiantes", stats.getOrDefault("totalEstudiantes", 0));
            model.addAttribute("totalVoluntarios", stats.getOrDefault("totalVoluntarios", 0));
            model.addAttribute("totalTutorias", stats.getOrDefault("totalAulas", 0));
            model.addAttribute("reportesPendientes", 0);
        });
        adminServiceClient.listUsers(token).ifPresent(users -> model.addAttribute("usuarios", users));
        model.addAttribute("voluntarios", List.of());
        model.addAttribute("voluntarioMap", Map.of());

        List<MensajeGlobal> mensajes = mensajeGlobalRepository.findByActivoTrueOrderByFechaCreacionDesc();
        model.addAttribute("mensajesGlobales", mensajes);

        return "admin/dashboard";
    }

    @PostMapping("/users/{id}/toggle-status")
    public String toggleUserStatus(@PathVariable Long id) {
        String token = jwtTokenProvider.generateToken("admin");
        adminServiceClient.toggleUserStatus(id, token);
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
        String token = jwtTokenProvider.generateToken("admin");
        adminServiceClient.verifyVolunteer(id, token);
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
