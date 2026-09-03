package com.ITCHA2026.GroovyfyMusic.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.expiration-ms}")
    private long expirationMs;

    private SecretKey obtenerLlave() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * Genera un token JWT a partir del UsuarioPrincipal.
     * Incluye: id, alias, correo y rol como claims.
     */
    public String generarToken(UsuarioPrincipal principal) {
        Date ahora = new Date();
        Date expiracion = new Date(ahora.getTime() + expirationMs);

        return Jwts.builder()
                .subject(principal.getCorreo())
                .claim("id", principal.getId())
                .claim("alias", principal.getAlias())
                .claim("correo", principal.getCorreo())
                .claim("rol", principal.getRol())
                .claim("imagen", principal.getImagen())
                .issuedAt(ahora)
                .expiration(expiracion)
                .signWith(obtenerLlave())
                .compact();
    }

    /**
     * Valida el token y devuelve los Claims si es válido.
     * Lanza JwtException si es inválido o expiró.
     */
    public Claims validarYExtraerClaims(String token) {
        return Jwts.parser()
                .verifyWith(obtenerLlave())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}