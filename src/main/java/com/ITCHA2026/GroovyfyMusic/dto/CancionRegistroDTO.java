package com.ITCHA2026.GroovyfyMusic.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
public class CancionRegistroDTO {
    private String nombre;
    private LocalDate fechaLanzamiento;
    private Integer duracionSegundos;
    private String portada;
    private String archivoAudio;
}
