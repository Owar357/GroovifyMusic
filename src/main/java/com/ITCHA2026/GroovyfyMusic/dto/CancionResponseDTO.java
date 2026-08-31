package com.ITCHA2026.GroovyfyMusic.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class CancionResponseDTO {
    private  Integer id;
    private  String nombre;
    private LocalDate fechaLanzamiento;
    private  Integer duracionSegundos;
    private String portada;
    private String archivoAudio;
    private UsuarioResponseDTO artista;
    private AlbumResponseDTO album;
    private List<GeneroResponseDTO> generos;
}
