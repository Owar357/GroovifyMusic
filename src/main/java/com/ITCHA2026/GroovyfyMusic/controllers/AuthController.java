package com.ITCHA2026.GroovyfyMusic.controllers;

import com.ITCHA2026.GroovyfyMusic.dto.UsuarioRegistroDTO;
import com.ITCHA2026.GroovyfyMusic.dto.UsuarioResponseDTO;
import com.ITCHA2026.GroovyfyMusic.dto.auth.LoginRequestDTO;
import com.ITCHA2026.GroovyfyMusic.dto.auth.LoginResponseDTO;
import com.ITCHA2026.GroovyfyMusic.services.AuthService;
import com.ITCHA2026.GroovyfyMusic.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UsuarioResponseDTO> register(
            @RequestPart("usuario") UsuarioRegistroDTO dto,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) {
        UsuarioResponseDTO response = usuarioService.register(dto, imagen);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}