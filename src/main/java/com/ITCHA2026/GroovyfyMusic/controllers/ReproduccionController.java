package com.ITCHA2026.GroovyfyMusic.controllers;

import com.ITCHA2026.GroovyfyMusic.dto.ReproduccionRegistroDTO;
import com.ITCHA2026.GroovyfyMusic.dto.ReproduccionResponseDTO;
import com.ITCHA2026.GroovyfyMusic.security.AuthenticatedUser;
import com.ITCHA2026.GroovyfyMusic.services.ReproduccionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reproducciones")
@RequiredArgsConstructor
public class ReproduccionController {

    private final ReproduccionService reproduccionService;


    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ReproduccionResponseDTO> registrarReproduccion(@RequestBody ReproduccionRegistroDTO dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthenticatedUser user = (AuthenticatedUser) authentication.getPrincipal();
        Integer usuarioId = user.id();

        ReproduccionResponseDTO response = reproduccionService.registrarReproduccion(dto, usuarioId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping("/usuario")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ReproduccionResponseDTO>> obtenerHistorialUsuario() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthenticatedUser user = (AuthenticatedUser) authentication.getPrincipal();
        Integer usuarioId = user.id();

        List<ReproduccionResponseDTO> response = reproduccionService.obtenerHistorialPorUsuario(usuarioId);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/cancion/{cancionId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ReproduccionResponseDTO>> obtenerReproduccionesPorCancion(@PathVariable Integer cancionId) {
        List<ReproduccionResponseDTO> response = reproduccionService.obtenerReproduccionesPorCancion(cancionId);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/cancion/{cancionId}/contar")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Long> contarReproducciones(@PathVariable Integer cancionId) {
        long count = reproduccionService.contarReproduccionesPorCancion(cancionId);
        return ResponseEntity.ok(count);
    }
}