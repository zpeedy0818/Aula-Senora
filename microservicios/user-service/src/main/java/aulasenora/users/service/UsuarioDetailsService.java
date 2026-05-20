package aulasenora.users.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import aulasenora.users.model.Usuario;
import aulasenora.users.repository.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final LoginAttemptService loginAttemptService;

    public UsuarioDetailsService(UsuarioRepository usuarioRepository, LoginAttemptService loginAttemptService) {
        this.usuarioRepository = usuarioRepository;
        this.loginAttemptService = loginAttemptService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        if (loginAttemptService.isBlocked(request.getRemoteAddr())) {
            throw new RuntimeException("Cuenta bloqueada temporalmente. Por favor, intente en 15 minutos.");
        }

        Usuario usuario = usuarioRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase(username, username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con correo o username: " + username));

        // En caso de que se haya autenticado por Google y no tenga contraseña en la BD local,
        // no le permitiremos loguearse con contraseña a menos que asigne una.
        String password = usuario.getPassword() != null ? usuario.getPassword() : "";

        return User.withUsername(usuario.getUsername())
                .password(password)
                .roles(usuario.getRol())
                .disabled(!usuario.isActivo())
                .build();
    }
}
