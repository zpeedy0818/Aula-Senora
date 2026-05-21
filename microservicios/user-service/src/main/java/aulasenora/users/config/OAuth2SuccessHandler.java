package aulasenora.users.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import java.io.IOException;

@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtils jwtUtils;

    @Value("${OAUTH2_REDIRECT_URL:http://localhost:8080/login-success}")
    private String redirectUrl;

    public OAuth2SuccessHandler(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        String username = oAuth2User.getUsername();
        String role = oAuth2User.getAuthorities().stream()
                .map(a -> a.getAuthority().replace("ROLE_", ""))
                .findFirst()
                .orElse("PENDIENTE");
        boolean perfilCompleto = oAuth2User.isPerfilCompleto();

        String token = jwtUtils.generateToken(username, role, perfilCompleto);

        // Recuperar el rol pretendido desde la sesión (guardado por RolCapturingFilter)
        String intendedRol = null;
        HttpSession session = request.getSession(false);
        if (session != null) {
            intendedRol = (String) session.getAttribute(RolCapturingFilter.SESSION_KEY_ROL);
            session.removeAttribute(RolCapturingFilter.SESSION_KEY_ROL);
        }

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(redirectUrl)
                .queryParam("token", token)
                .queryParam("perfilCompleto", perfilCompleto);

        // Si hay un rol pretendido y el perfil no está completo, pasarlo al frontend
        if (intendedRol != null && !perfilCompleto) {
            builder.queryParam("rol", intendedRol);
        }

        String targetUrl = builder.build().toUriString();
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
