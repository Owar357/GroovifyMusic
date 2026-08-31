package com.ITCHA2026.GroovyfyMusic.dto;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ArtistaResponseDTO {
    private Integer id;
    private String nombre;
    private String biografia;
    private String imagen;
    private LocalDateTime creadoEn;
}
