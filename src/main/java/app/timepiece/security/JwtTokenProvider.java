package app.timepiece.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {



    public String generateToken(String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JwtSecret.JWT_EXPIRATION);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(JwtSecret.key, SignatureAlgorithm.HS512)
                .compact();
    }
}
