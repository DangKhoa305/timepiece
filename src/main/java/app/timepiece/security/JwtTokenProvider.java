package app.timepiece.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {



    public String generateToken(Long id) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JwtSecret.JWT_EXPIRATION);

        return Jwts.builder()
                .setSubject(String.valueOf(id))
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(JwtSecret.key, SignatureAlgorithm.HS512)
                .compact();
    }
}
