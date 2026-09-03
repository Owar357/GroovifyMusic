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

    @GetMapping("/genero")
    public ResponseEntity<List<GeneroDTO>> findAll() {
        return ResponseEntity.ok(iservice.findAll());
    }

    @GetMapping("/genero/{id}")
    public ResponseEntity<GeneroDTO> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(iservice.findById(id));
    }

    @PostMapping("/genero")
    public ResponseEntity<?> save(@RequestBody GeneroDTO dto){
        Map<String, Object> response = new HashMap<>();
        GeneroDTO generosave = iservice.save(dto);

        response.put("message", "Genero creado correctamente");
        response.put("genero", generosave);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @PutMapping("/genero/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody GeneroDTO dto){
        Map<String, Object> response = new HashMap<>();

        dto.setId(id);
        GeneroDTO generoupdate = iservice.save(dto);

        response.put("message", "El genero se actualizo correctamente");
        response.put("genero", generoupdate);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/genero/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id){
        Map<String, Object> response = new HashMap<>();

        iservice.delete(id);
        response.put("message", "el Genero fue elimindado correctamente");

        return new ResponseEntity<>(response,HttpStatus.OK);
    }

}
