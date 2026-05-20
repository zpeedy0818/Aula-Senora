package aulasenora.users.controller;

import aulasenora.users.config.JwtUtils;
import aulasenora.users.dto.OnboardingDTO;
import aulasenora.users.dto.RegistroDTO;
import aulasenora.users.model.Usuario;
import aulasenora.users.model.Voluntario;
import aulasenora.users.service.UsuarioService;
import aulasenora.users.service.RecaptchaService;
import aulasenora.users.repository.UsuarioRepository;
import aulasenora.users.repository.VoluntarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;
    private final VoluntarioRepository voluntarioRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final RecaptchaService recaptchaService;

    public UserController(UsuarioService usuarioService, UsuarioRepository usuarioRepository,
                          VoluntarioRepository voluntarioRepository,
                          AuthenticationManager authenticationManager, JwtUtils jwtUtils,
                          RecaptchaService recaptchaService) {
        this.usuarioService = usuarioService;
        this.usuarioRepository = usuarioRepository;
        this.voluntarioRepository = voluntarioRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.recaptchaService = recaptchaService;
    }

    // ─── Registro ────────────────────────────────────────────────────────────────

    @PostMapping("/register")
    public ResponseEntity<?> registrar(@Valid @RequestBody RegistroDTO registroDTO,
                                       @RequestParam(name = "recaptchaToken", required = false) String recaptchaToken,
                                       HttpServletRequest request) {
        if (recaptchaToken != null && !recaptchaToken.isBlank()) {
            boolean isCaptchaValid = recaptchaService.verifyRecaptcha(request.getRemoteAddr(), recaptchaToken);
            if (!isCaptchaValid) {
                return ResponseEntity.badRequest().body("reCAPTCHA inválido.");
            }
        }
        try {
            Usuario nuevo = usuarioService.registrar(registroDTO);
            nuevo.setPassword(null);
            return ResponseEntity.ok(nuevo);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ─── Login ────────────────────────────────────────────────────────────────────

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String role = authentication.getAuthorities().stream()
                    .map(a -> a.getAuthority().replace("ROLE_", ""))
                    .findFirst()
                    .orElse("USER");

            // Para login local los usuarios ya tienen perfil completo
            String token = jwtUtils.generateToken(username, role, true);

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("username", username);
            response.put("role", role);
            response.put("perfilCompleto", true);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Credenciales incorrectas: " + e.getMessage());
        }
    }

    // ─── Onboarding (solo para usuarios nuevos de Google) ────────────────────────

    @PostMapping("/onboarding")
    public ResponseEntity<?> onboarding(@RequestBody OnboardingDTO dto, HttpServletRequest request) {
        // Extraer token del header Authorization
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Token requerido.");
        }
        String token = authHeader.substring(7);

        // Validar que el perfil NO esté completo (prevenir onboarding doble)
        if (jwtUtils.getPerfilCompletoFromToken(token)) {
            return ResponseEntity.badRequest().body("El perfil ya está completo.");
        }

        String username = jwtUtils.getUsernameFromToken(token);
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElse(null);
        if (usuario == null) {
            return ResponseEntity.status(404).body("Usuario no encontrado.");
        }

        // Validar rol
        String rol = dto.getRol();
        if (!"ESTUDIANTE".equalsIgnoreCase(rol) && !"VOLUNTARIO".equalsIgnoreCase(rol)) {
            return ResponseEntity.badRequest().body("Rol inválido. Debe ser ESTUDIANTE o VOLUNTARIO.");
        }

        // Si es VOLUNTARIO, validar campos obligatorios
        if ("VOLUNTARIO".equalsIgnoreCase(rol)) {
            if (dto.getInstitution() == null || dto.getInstitution().isBlank()) {
                return ResponseEntity.badRequest().body("La institución es obligatoria para voluntarios.");
            }
            if (dto.getSkills() == null || dto.getSkills().isBlank()) {
                return ResponseEntity.badRequest().body("Las habilidades son obligatorias para voluntarios.");
            }
        }

        // Actualizar campos del usuario
        usuario.setRol(rol.toUpperCase());
        usuario.setPerfilCompleto(true);
        if (dto.getCiudad() != null) usuario.setCiudad(dto.getCiudad());
        if (dto.getBio() != null) usuario.setBio(dto.getBio());
        if (dto.getPhoneNumber() != null) usuario.setPhoneNumber(dto.getPhoneNumber());
        if (dto.getBirthDate() != null) usuario.setBirthDate(dto.getBirthDate());
        if (dto.getGradoAcademico() != null) usuario.setGradoAcademico(dto.getGradoAcademico());
        usuarioRepository.save(usuario);

        // Si es Voluntario, crear/actualizar registro en tabla voluntarios
        if ("VOLUNTARIO".equalsIgnoreCase(rol)) {
            Voluntario voluntario = voluntarioRepository.findByUsuario_Username(username)
                    .orElse(new Voluntario());
            voluntario.setUsuario(usuario);
            voluntario.setInstitution(dto.getInstitution());
            voluntario.setSkills(dto.getSkills());
            if (dto.getMateriaEspecializada() != null) {
                voluntario.setMateriaEspecializada(dto.getMateriaEspecializada());
            }
            voluntarioRepository.save(voluntario);
        }

        // Generar nuevo JWT con perfilCompleto=true
        String nuevoToken = jwtUtils.generateToken(username, rol.toUpperCase(), true);

        Map<String, Object> response = new HashMap<>();
        response.put("token", nuevoToken);
        response.put("username", username);
        response.put("role", rol.toUpperCase());
        response.put("perfilCompleto", true);
        response.put("mensaje", "¡Bienvenido/a! Tu perfil ha sido completado exitosamente.");

        return ResponseEntity.ok(response);
    }

    // ─── Perfil ───────────────────────────────────────────────────────────────────

    @GetMapping("/me")
    public ResponseEntity<?> getMe(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Token requerido.");
        }
        String token = authHeader.substring(7);
        String username = jwtUtils.getUsernameFromToken(token);
        return usuarioRepository.findByUsername(username)
                .map(u -> { u.setPassword(null); return ResponseEntity.ok(u); })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        return usuarioRepository.findById(id)
                .map(u -> {
                    u.setPassword(null);
                    return ResponseEntity.ok(u);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody Usuario updatedData) {
        return usuarioRepository.findById(id)
                .map(usuario -> {
                    if (updatedData.getFirstName() != null) usuario.setFirstName(updatedData.getFirstName().trim().toUpperCase());
                    if (updatedData.getLastName() != null) usuario.setLastName(updatedData.getLastName().trim().toUpperCase());
                    if (updatedData.getBio() != null) usuario.setBio(updatedData.getBio());
                    if (updatedData.getPhoneNumber() != null) usuario.setPhoneNumber(updatedData.getPhoneNumber());
                    if (updatedData.getBirthDate() != null) usuario.setBirthDate(updatedData.getBirthDate());
                    if (updatedData.getGradoAcademico() != null) usuario.setGradoAcademico(updatedData.getGradoAcademico());
                    if (updatedData.getInstitucion() != null) usuario.setInstitucion(updatedData.getInstitucion());
                    if (updatedData.getCiudad() != null) usuario.setCiudad(updatedData.getCiudad());
                    if (updatedData.getProfileImageUrl() != null) usuario.setProfileImageUrl(updatedData.getProfileImageUrl());
                    if (updatedData.getBannerImageUrl() != null) usuario.setBannerImageUrl(updatedData.getBannerImageUrl());

                    Usuario saved = usuarioRepository.save(usuario);
                    saved.setPassword(null);
                    return ResponseEntity.ok(saved);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
