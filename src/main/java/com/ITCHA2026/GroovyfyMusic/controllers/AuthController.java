package com.ITCHA2026.GroovyfyMusic.controllers;

import com.ITCHA2026.GroovyfyMusic.dto.UsuarioRegistroDTO;
import com.ITCHA2026.GroovyfyMusic.dto.UsuarioResponseDTO;
import com.ITCHA2026.GroovyfyMusic.dto.auth.LoginRequestDTO;
import com.ITCHA2026.GroovyfyMusic.dto.auth.LoginResponseDTO;
import com.ITCHA2026.GroovyfyMusic.entities.Usuario;
import com.ITCHA2026.GroovyfyMusic.repository.UsuarioRepository;
import com.ITCHA2026.GroovyfyMusic.security.JwtService;
import com.ITCHA2026.GroovyfyMusic.security.UsuarioPrincipal;
import com.ITCHA2026.GroovyfyMusic.services.AuthService;
import com.ITCHA2026.GroovyfyMusic.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UsuarioService usuarioService;
    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> register(
            @RequestPart("usuario") UsuarioRegistroDTO dto,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) {

        UsuarioResponseDTO usuarioCreado = usuarioService.register(dto, imagen);

        Usuario usuario = usuarioRepository.findByCorreo(dto.getCorreo())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado después del registro"));

        UsuarioPrincipal principal = new UsuarioPrincipal(usuario);
        String token = jwtService.generarToken(principal);
        
        Map<String, Object> response = new HashMap<>();
        response.put("usuario", usuarioCreado);
        response.put("token", token);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}