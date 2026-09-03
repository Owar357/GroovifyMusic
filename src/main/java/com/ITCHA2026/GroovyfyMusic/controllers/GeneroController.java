package com.ITCHA2026.GroovyfyMusic.controllers;

import com.ITCHA2026.GroovyfyMusic.dto.GeneroDTO;
import com.ITCHA2026.GroovyfyMusic.interfaces.IGeneroService;
import com.ITCHA2026.GroovyfyMusic.services.GeneroService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/genero")
public class GeneroController  {
    private final IGeneroService iservice;

    @GetMapping
    public ResponseEntity<List<GeneroDTO>> findAll() {
        return ResponseEntity.ok(iservice.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneroDTO> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(iservice.findById(id));
    }
    @GetMapping("/buscar")
    public ResponseEntity<List<GeneroDTO>> buscarPorNombre(@RequestParam String nombre){
        return ResponseEntity.ok(iservice.buscarPorNombre(nombre));
    }



}
