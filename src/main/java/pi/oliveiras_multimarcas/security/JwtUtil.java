package pi.oliveiras_multimarcas.security;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtUtil {
    @Value("${secret_acess}")
    private String SECRET_ACCESS;

    @Value("${expiration_acess}")
    private long EXPIRATION_ACESS;

    @Value("${secret_refresh}")
    private String SECRET_REFRESH;

    @Value("${expiration_refresh}")
    private long EXPIRATION_REFRESH;

    private Algorithm getAlgorithm(String secretKey) {
        return Algorithm.HMAC256(secretKey);
    }

    public String generateTokenAcess(UUID id){
        Map<String, Object> claim = Map.of(
                "id", id.toString()
        );
        return JWT.create()
                .withPayload(claim)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_ACESS))
                .sign(getAlgorithm(SECRET_ACCESS));
    }

    public String generateTokenRefresh(UUID id){
        Map<String, Object> claim = Map.of(
                "id", id.toString()
        );
        return JWT.create()
                .withPayload(claim)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_REFRESH))
                .sign(getAlgorithm(SECRET_REFRESH));
    }

    public Map<String, Object> extractClaims(String token, String typeToken){
        String secret = (Objects.equals(typeToken, "acess")) ? SECRET_ACCESS : SECRET_REFRESH;
        return JWT.require(getAlgorithm(secret)).build().verify(token).getClaims()
                .entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().as(Object.class)));
    }

    public boolean isTokenValid(String token, String typeToken){
        String secret = (Objects.equals(typeToken, "acess")) ? SECRET_ACCESS : SECRET_REFRESH;
        try {
            JWT.require(getAlgorithm(secret)).build().verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

}