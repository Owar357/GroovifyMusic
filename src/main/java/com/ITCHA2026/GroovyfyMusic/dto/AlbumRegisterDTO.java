package com.ITCHA2026.GroovyfyMusic.dto;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class AlbumRegisterDTO {
    private String Nombre;
    private LocalDateTime FechaLansamiento;
    private String Portada;
    private String Duracion;

}
