package com.ITCHA2026.GroovyfyMusic.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AlbumResponseDTO {
    private Integer id;
    private String Nombre;
    private LocalDateTime FechaLansamiento;
    private String Portada;
    private String Duracion;
    private ArtistaResponseDTO Artista;

}
