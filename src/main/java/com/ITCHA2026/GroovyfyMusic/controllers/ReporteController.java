package com.ITCHA2026.GroovyfyMusic.controllers;

import com.ITCHA2026.GroovyfyMusic.security.AuthenticatedUser;
import com.ITCHA2026.GroovyfyMusic.services.ReporteArtistaService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/reportes")
@CrossOrigin(origins = "*")
public class ReporteController {

    private final ReporteArtistaService service;

    // --- ¡ESTO ERA LO QUE FALTABA! ---
    @PreAuthorize("hasRole('ARTISTA')")
    @GetMapping("/mis-reproducciones")
    // ---------------------------------
    public ResponseEntity<ByteArrayResource> descargarReporteMisReproducciones(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate FechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate FechaFin,
            Authentication authentication) {

        AuthenticatedUser user = (AuthenticatedUser) authentication.getPrincipal();
        Integer artistaId = user.id();

        byte[] pdfBytes = service.generarReporte(artistaId, FechaInicio, FechaFin);

        ByteArrayResource resource = new ByteArrayResource(pdfBytes);
        String nombreArchivo = "Reporte_reproducciones_" + FechaInicio.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + ".pdf";

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nombreArchivo + "\"")
                .body(resource);
    }
}