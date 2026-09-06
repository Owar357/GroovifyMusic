package com.ITCHA2026.GroovyfyMusic.controllers;

import com.ITCHA2026.GroovyfyMusic.dto.CancionFiltroDTO;
import com.ITCHA2026.GroovyfyMusic.dto.CancionPopularDTO;
import com.ITCHA2026.GroovyfyMusic.dto.CancionRegistroDTO;
import com.ITCHA2026.GroovyfyMusic.dto.CancionResponseDTO;
import com.ITCHA2026.GroovyfyMusic.security.AuthenticatedUser;
import com.ITCHA2026.GroovyfyMusic.services.CancionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/canciones")
@RequiredArgsConstructor
public class CancionController {

    private final CancionService cancionService;

    @PreAuthorize("hasRole('ARTISTA')")
    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CancionResponseDTO> register(
            @RequestPart("cancion") CancionRegistroDTO dto,
            @RequestPart(value = "portada", required = false) MultipartFile portada,
            @RequestPart(value = "archivoAudio", required = false) MultipartFile archivoAudio) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthenticatedUser user = (AuthenticatedUser) authentication.getPrincipal();
        Integer usuarioId = user.id();

        CancionResponseDTO response = cancionService.register(dto, portada, archivoAudio, usuarioId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<CancionResponseDTO> findById(@PathVariable Integer id) {
        CancionResponseDTO response = cancionService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<List<CancionResponseDTO>> findAll() {
        List<CancionResponseDTO> response = cancionService.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/album/{albumId}")
    public ResponseEntity<List<CancionResponseDTO>> findByAlbumId(@PathVariable Integer albumId) {
        return ResponseEntity.ok(cancionService.findByAlbumId(albumId));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/search")
    public ResponseEntity<List<CancionResponseDTO>> search(@RequestBody CancionFiltroDTO filtro) {
        List<CancionResponseDTO> response = cancionService.search(filtro);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ARTISTA')")
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CancionResponseDTO> update(
            @PathVariable Integer id,
            @RequestPart("cancion") CancionRegistroDTO dto,
            @RequestPart(value = "portada", required = false) MultipartFile portada,
            @RequestPart(value = "archivoAudio", required = false) MultipartFile archivoAudio) {
        CancionResponseDTO response = cancionService.update(id, dto, portada, archivoAudio);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/artista/{artistaId}/populares")
    public ResponseEntity<List<CancionPopularDTO>> obtenerPopulares(
            @PathVariable Integer artistaId,
            @RequestParam(defaultValue = "5") int limit) {

        if (limit <= 0) {
            limit = 10;
        }

        return ResponseEntity.ok(cancionService.findPopularesByArtista(artistaId, limit));
    }


    @PreAuthorize("hasRole('ARTISTA')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        cancionService.delete(id);
        return ResponseEntity.noContent().build();
    }


}