package aulasenora.config;

import java.io.IOException;

import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.lang.NonNull;

import aulasenora.service.RecaptchaService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class RecaptchaFilter extends OncePerRequestFilter {

    private final RecaptchaService recaptchaService;

    public RecaptchaFilter(RecaptchaService recaptchaService) {
        this.recaptchaService = recaptchaService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        if ("POST".equalsIgnoreCase(request.getMethod()) && "/login".equals(request.getRequestURI())) {
            String recaptchaResponse = request.getParameter("g-recaptcha-response");
            
            if (recaptchaResponse == null || recaptchaResponse.isEmpty()) {
                response.sendRedirect("/login?error=true");
                return;
            }

            boolean isValid = recaptchaService.verifyRecaptcha(request.getRemoteAddr(), recaptchaResponse);
            
            if (!isValid) {
                response.sendRedirect("/login?error=true");
                return;
            }
        }
        
        filterChain.doFilter(request, response);
    }
}
