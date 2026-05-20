package aulasenora.users.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class JwtUtils {

    @Value("${JWT_SECRET:mi-secreto-super-seguro-de-32-caracteres}")
    private String secret;

    @Value("${JWT_EXPIRATION_MS:86400000}") // 1 día por defecto
    private long expirationMs;

    public String generateToken(String username, String role) {
        return generateToken(username, role, true);
    }

    public String generateToken(String username, String role, boolean perfilCompleto) {
        return JWT.create()
                .withSubject(username)
                .withClaim("role", role)
                .withClaim("perfilCompleto", perfilCompleto)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationMs))
                .sign(Algorithm.HMAC256(secret));
    }

    public String getUsernameFromToken(String token) {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(secret))
                .build()
                .verify(token);
        return decodedJWT.getSubject();
    }

    public String getRoleFromToken(String token) {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(secret))
                .build()
                .verify(token);
        return decodedJWT.getClaim("role").asString();
    }

    public boolean getPerfilCompletoFromToken(String token) {
        try {
            DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(secret))
                    .build()
                    .verify(token);
            Boolean val = decodedJWT.getClaim("perfilCompleto").asBoolean();
            return val != null && val;
        } catch (Exception e) {
            return true; // Si no tiene el claim, asumimos perfil completo (usuarios anteriores)
        }
    }

    public boolean validateToken(String token) {
        try {
            JWT.require(Algorithm.HMAC256(secret))
                    .build()
                    .verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
