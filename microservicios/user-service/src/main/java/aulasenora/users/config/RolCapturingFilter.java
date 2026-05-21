package aulasenora.users.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RolCapturingFilter extends OncePerRequestFilter {

    public static final String SESSION_KEY_ROL = "OAUTH2_INTENDED_ROL";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // Capturar el parámetro 'rol' antes de que Spring Security redirija a Google
        if (request.getRequestURI().contains("/oauth2/authorization/")) {
            String rol = request.getParameter("rol");
            if (rol != null && !rol.isBlank()) {
                request.getSession().setAttribute(SESSION_KEY_ROL, rol.toUpperCase());
            }
        }
        filterChain.doFilter(request, response);
    }
}
