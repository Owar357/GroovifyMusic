package com.ITCHA2026.GroovyfyMusic.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AlbumResponseDTO {
    private Integer id;
    private String nombre;
    private LocalDate fechaLanzamiento;
    private String portada;
    private UsuarioResponseDTO artista;
}