package com.ITCHA2026.GroovyfyMusic.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class PlaylistResponseDTO {
    private Integer id;
    private String nombre;
    private String descripcion;
    private String portada;
    private String tipo;
    private UsuarioResponseDTO usuario;
    private LocalDateTime creadaEn;
}
