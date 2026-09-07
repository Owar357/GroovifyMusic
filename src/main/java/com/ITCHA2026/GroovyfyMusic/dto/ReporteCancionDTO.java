package com.ITCHA2026.GroovyfyMusic.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReporteCancionDTO {
    private String nombreCancion;
    private String nombreAlbum;
    private Integer totalReproducciones;

}
