package com.ITCHA2026.GroovyfyMusic.controllers;

import com.ITCHA2026.GroovyfyMusic.dto.CancionResponseDTO;
import com.ITCHA2026.GroovyfyMusic.dto.PlaylistRegistroDTO;
import com.ITCHA2026.GroovyfyMusic.dto.PlaylistResponseDTO;
import com.ITCHA2026.GroovyfyMusic.interfaces.IPlaylistService;
import com.ITCHA2026.GroovyfyMusic.security.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/playlists")
@RequiredArgsConstructor
public class PlaylistController {

    private final IPlaylistService playlistService;

    private Integer getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthenticatedUser user = (AuthenticatedUser) authentication.getPrincipal();
        return user.id();
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PlaylistResponseDTO> crearPlaylist(
            @RequestPart("playlist") PlaylistRegistroDTO dto,
            @RequestPart(value = "portada", required = false) MultipartFile portada) {
        Integer usuarioId = getAuthenticatedUserId();
        PlaylistResponseDTO response = playlistService.crearPlaylist(dto, usuarioId, portada);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }



    @PreAuthorize("isAuthenticated()")
    @GetMapping("/mis-playlists")
    public ResponseEntity<List<PlaylistResponseDTO>> obtenerPlaylistsUsuario() {
        Integer usuarioId = getAuthenticatedUserId();
        return ResponseEntity.ok(playlistService.obtenerPlaylistsUsuario(usuarioId));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/destacadas")
    public ResponseEntity<List<PlaylistResponseDTO>> obtenerDestacadas() {
        return ResponseEntity.ok(playlistService.obtenerDestacadas());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<PlaylistResponseDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(playlistService.obtenerPorId(id));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/canciones")
    public ResponseEntity<List<CancionResponseDTO>> obtenerCancionesDePlaylist(@PathVariable Integer id) {
        return ResponseEntity.ok(playlistService.obtenerCancionesDePlaylist(id));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{playlistId}/canciones/{cancionId}")
    public ResponseEntity<Map<String, String>> agregarCancion(
            @PathVariable Integer playlistId,
            @PathVariable Integer cancionId) {
        Integer usuarioId = getAuthenticatedUserId();
        playlistService.agregarCancion(playlistId, cancionId, usuarioId);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Canción agregada a la playlist correctamente");
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{playlistId}/canciones/{cancionId}")
    public ResponseEntity<Void> quitarCancion(
            @PathVariable Integer playlistId,
            @PathVariable Integer cancionId) {
        Integer usuarioId = getAuthenticatedUserId();
        playlistService.quitarCancion(playlistId, cancionId, usuarioId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPlaylist(@PathVariable Integer id) {
        Integer usuarioId = getAuthenticatedUserId();
        playlistService.eliminarPlaylist(id, usuarioId);
        return ResponseEntity.noContent().build();
    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/me-gusta/toggle/{cancionId}")
    public ResponseEntity<Map<String, Object>> toggleMeGusta(@PathVariable Integer cancionId) {
        Integer usuarioId = getAuthenticatedUserId();
        boolean agregado = playlistService.toggleMeGusta(usuarioId, cancionId);

        Map<String, Object> response = new HashMap<>();
        response.put("agregado", agregado);
        response.put("message", agregado ? "Agregado a tus me gusta" : "Eliminado de tus me gusta");

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me-gusta/existe/{cancionId}")
    public ResponseEntity<Boolean> existeMeGusta(@PathVariable Integer cancionId) {
        Integer usuarioId = getAuthenticatedUserId();
        return ResponseEntity.ok(playlistService.existeMeGusta(usuarioId, cancionId));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me-gusta")
    public ResponseEntity<List<CancionResponseDTO>> obtenerCancionesMeGusta() {
        Integer usuarioId = getAuthenticatedUserId();
        return ResponseEntity.ok(playlistService.obtenerCancionesMeGusta(usuarioId));
    }
}