package app.timepiece.security;

import io.jsonwebtoken.security.Keys;

import java.security.Key;

public class JwtSecret {

    public final String JWT_SECRET = "fab84bbe165440e45739c2fc46fe0c7a057545c6d7c376b689ef760b346ab30d"; // replace with your actual secret key
    public final long JWT_EXPIRATION = 604800000L; // 7 days
    public final Key key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes());
}
