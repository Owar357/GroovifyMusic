package com.ITCHA2026.GroovyfyMusic.security;

/**
 * Se construye ÚNICAMENTE a partir de los claims del token
 * (JwtAuthenticationFilter), nunca consultando la base de datos.
 */
public record AuthenticatedUser (
        Integer id,
        String alias,
        String correo,
        String rol
) {}
