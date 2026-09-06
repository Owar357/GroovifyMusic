package com.ITCHA2026.GroovyfyMusic.controllers;

import com.ITCHA2026.GroovyfyMusic.dto.AlbumRegisterDTO;
import com.ITCHA2026.GroovyfyMusic.dto.AlbumResponseDTO;
import com.ITCHA2026.GroovyfyMusic.interfaces.IAlbumService;
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
@RequestMapping("/api")
@RequiredArgsConstructor
public class AlbumController {

    private final IAlbumService iservice;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/album")
    public ResponseEntity<List<AlbumResponseDTO>> findAll() {

        return ResponseEntity.ok(iservice.findAll());
    }

        @PreAuthorize("isAuthenticated()")
        @GetMapping("/album/artista/{artistaId}")
        public ResponseEntity<List<AlbumResponseDTO>> getByArtistaId(@PathVariable Integer artistaId) {
            return ResponseEntity.ok(iservice.findByArtistaId(artistaId));
        }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/album/{id}")
    public ResponseEntity<AlbumResponseDTO> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(iservice.findById(id));
    }
    @PreAuthorize("hasRole('ARTISTA')")
    @PostMapping(value = "/album", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> save(
            @RequestPart("album") AlbumRegisterDTO dto,
            @RequestPart(value = "file", required = false) MultipartFile file){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthenticatedUser user = (AuthenticatedUser) authentication.getPrincipal();

        dto.setArtistaId(user.id());

        Map<String, Object> response = new HashMap<>();
        AlbumResponseDTO albumsave = iservice.save(dto, file);

        response.put("message", "album creado exitosamente");
        response.put("Album", albumsave);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ARTISTA')")
    @PutMapping(value = "/album/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> Update(
            @PathVariable Integer id,
            @RequestPart("album") AlbumRegisterDTO dto,
            @RequestPart(value = "file", required = false) MultipartFile file){

        Map<String, Object> response = new HashMap<>();
        AlbumResponseDTO albumupdate = iservice.update(id, dto, file);

        response.put("message", "Album actualizado correctamente");
        response.put("album", albumupdate);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ARTISTA')")
    @DeleteMapping("/album/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id){
        Map<String, Object> response = new HashMap<>();

        iservice.delete(id);
        response.put("message", "Album borrado exitosamente");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}