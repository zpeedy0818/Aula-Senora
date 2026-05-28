package aulasenora.client;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    @Value("${JWT_SECRET:mi-secreto-super-seguro-de-32-caracteres}")
    private String jwtSecret;

    @Value("${JWT_EXPIRATION_MS:86400000}")
    private long expirationMs;

    private Algorithm algorithm;

    @PostConstruct
    public void init() {
        this.algorithm = Algorithm.HMAC256(jwtSecret);
    }

    public String generateToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withIssuer("aulasenora-main")
                .withExpiresAt(new java.util.Date(System.currentTimeMillis() + expirationMs))
                .sign(algorithm);
    }
}
