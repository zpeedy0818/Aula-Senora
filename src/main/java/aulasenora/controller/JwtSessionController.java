package aulasenora.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aulasenora.service.UsuarioDetailsService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class JwtSessionController {

    private final UsuarioDetailsService userDetailsService;

    @Value("${JWT_SECRET:mi-secreto-super-seguro-de-32-caracteres}")
    private String jwtSecret;

    public JwtSessionController(UsuarioDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/jwt-session")
    public ResponseEntity<?> createSessionFromJwt(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Token requerido.");
        }
        String token = authHeader.substring(7);

        DecodedJWT decoded;
        try {
            decoded = JWT.require(Algorithm.HMAC256(jwtSecret)).build().verify(token);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Token inválido.");
        }

        String username = decoded.getSubject();
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        // Persistir el SecurityContext en la sesión HTTP para que las siguientes
        // peticiones (navegación al dashboard) lo recuperen via cookie JSESSIONID.
        request.getSession(true).setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);

        String role = userDetails.getAuthorities().stream()
                .map(a -> a.getAuthority().replace("ROLE_", ""))
                .findFirst()
                .orElse("ESTUDIANTE");

        Map<String, Object> response = new HashMap<>();
        response.put("username", username);
        response.put("role", role);
        return ResponseEntity.ok(response);
    }
}
