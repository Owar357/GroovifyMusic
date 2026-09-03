package com.ITCHA2026.GroovyfyMusic.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);

    @Value("${app.debug-errors:false}")
    private boolean debugErrors;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {


        Object jwtError = request.getAttribute("jwt_exception");
        logger.warn("Auth error en {} {}: {} (causa: {})",
                request.getMethod(),
                request.getRequestURI(),
                authException.getMessage(),
                jwtError);

        String mensaje = "Debe iniciar sesión para acceder a este recurso";

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=UTF-8");

        StringBuilder body = new StringBuilder();
        body.append("{\"message\":\"").append(mensaje).append("\"");
        body.append(",\"path\":\"").append(request.getRequestURI()).append("\"");

        if (debugErrors) {
            body.append(",\"error\":\"").append(authException.getClass().getSimpleName()).append("\"");
            body.append(",\"detail\":\"").append(escapeJson(authException.getMessage())).append("\"");
            if (jwtError != null) {
                body.append(",\"cause\":\"").append(escapeJson(jwtError.toString())).append("\"");
            }
        }
        body.append("}");

        response.getWriter().write(body.toString());
    }

    private String escapeJson(String s) {
        return s == null ? "" : s.replace("\"", "'").replace("\n", " ");
    }
}