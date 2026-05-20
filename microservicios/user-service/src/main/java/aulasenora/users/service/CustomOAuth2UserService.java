package aulasenora.users.service;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import aulasenora.users.config.CustomOAuth2User;
import aulasenora.users.model.Usuario;
import aulasenora.users.repository.UsuarioRepository;
import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UsuarioRepository usuarioRepository;

    public CustomOAuth2UserService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        try {
            return processOAuth2User(userRequest, oAuth2User);
        } catch (Exception ex) {
            throw new OAuth2AuthenticationException(ex.getMessage());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute("email");
        if (email == null || email.isBlank()) {
            throw new RuntimeException("Email no encontrado en el proveedor OAuth2");
        }

        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(email);
        Usuario usuario;

        if (usuarioOptional.isPresent()) {
            usuario = usuarioOptional.get();
            if (!"GOOGLE".equals(usuario.getProvider())) {
                usuario.setProvider("GOOGLE");
                usuario.setProviderId(oAuth2User.getName());
                usuarioRepository.save(usuario);
            }
        } else {
            usuario = registerNewOAuth2User(userRequest, oAuth2User);
        }

        return new CustomOAuth2User(
                oAuth2User,
                usuario.getRol(),
                usuario.isPerfilCompleto(),
                usuario.getUsername()
        );
    }

    private Usuario registerNewOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        Usuario usuario = new Usuario();
        usuario.setProvider("GOOGLE");
        usuario.setProviderId(oAuth2User.getName());

        String name = oAuth2User.getAttribute("name");
        String firstName = oAuth2User.getAttribute("given_name");
        String lastName = oAuth2User.getAttribute("family_name");

        if (firstName == null) firstName = name != null ? name : "GoogleUser";
        if (lastName == null) lastName = "";

        usuario.setFirstName(firstName.toUpperCase());
        usuario.setLastName(lastName.toUpperCase());
        usuario.setEmail(oAuth2User.getAttribute("email"));

        String email = oAuth2User.getAttribute("email");
        String username = email.split("@")[0];
        if (usuarioRepository.existsByUsername(username)) {
            username = username + "_" + (System.currentTimeMillis() % 1000);
        }
        usuario.setUsername(username);
        usuario.setRol("PENDIENTE");          // Esperando onboarding
        usuario.setPerfilCompleto(false);      // Perfil incompleto hasta el onboarding
        usuario.setActivo(true);

        String imageUrl = oAuth2User.getAttribute("picture");
        if (imageUrl != null) {
            usuario.setProfileImageUrl(imageUrl);
        }

        return usuarioRepository.save(usuario);
    }
}
