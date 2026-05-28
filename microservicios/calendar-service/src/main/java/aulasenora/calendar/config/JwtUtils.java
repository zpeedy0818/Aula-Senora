package aulasenora.calendar.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtils {
    @Value("${app.jwt.secret}")
    private String jwtSecret;
    private Algorithm algorithm;
    @PostConstruct
    public void init() { this.algorithm = Algorithm.HMAC256(jwtSecret); }
    public DecodedJWT validateToken(String token) { return JWT.require(algorithm).build().verify(token); }
    public String getUsernameFromToken(String token) { return validateToken(token).getSubject(); }
}
