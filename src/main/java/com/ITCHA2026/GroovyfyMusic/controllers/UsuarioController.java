package com.ITCHA2026.GroovyfyMusic.controllers;

import com.ITCHA2026.GroovyfyMusic.dto.UsuarioRegistroDTO;
import com.ITCHA2026.GroovyfyMusic.dto.UsuarioResponseDTO;
import com.ITCHA2026.GroovyfyMusic.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> findById(@PathVariable Integer id) {
        UsuarioResponseDTO response = usuarioService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UsuarioResponseDTO> update(
            @PathVariable Integer id,
            @RequestPart("usuario") UsuarioRegistroDTO dto,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) {
        UsuarioResponseDTO response = usuarioService.update(id, dto, imagen);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        usuarioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}