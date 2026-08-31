package com.ITCHA2026.GroovyfyMusic.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class AlbumRegisterDTO {
    private String nombre;
    private LocalDateTime fechaLanzamiento;
    private String portada;
    private String duracion;
    private Integer artistaId;
}