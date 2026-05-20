package aulasenora.users.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {

    private final OAuth2User oAuth2User;
    private final String role;
    private final boolean perfilCompleto;
    private final String username;

    public CustomOAuth2User(OAuth2User oAuth2User, String role, boolean perfilCompleto, String username) {
        this.oAuth2User = oAuth2User;
        this.role = role;
        this.perfilCompleto = perfilCompleto;
        this.username = username;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public String getName() {
        return oAuth2User.getName();
    }

    public String getEmail() {
        return oAuth2User.getAttribute("email");
    }

    public boolean isPerfilCompleto() {
        return perfilCompleto;
    }

    public String getUsername() {
        return username;
    }
}
