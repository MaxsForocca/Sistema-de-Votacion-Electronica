package com.sistema.auth.config;

import java.sql.Date;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {
    private final String SECRET = "clave-secreta"; // reemplaza en prod

    public String generarToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("rol", userDetails.getAuthorities().stream().findFirst().get().getAuthority())
                .setIssuedAt(new Date(0))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 5)) // 5h
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    public String extraerUsername(String token) {
        return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token)
                .getBody().getSubject();
    }

    public boolean validarToken(String token, UserDetails userDetails) {
        return extraerUsername(token).equals(userDetails.getUsername()) &&
               !estaExpirado(token);
    }

    private boolean estaExpirado(String token) {
        return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token)
                .getBody().getExpiration().before(new Date(0));
    }
}
