package com.ITCHA2026.GroovyfyMusic.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReproduccionResponseDTO {
    private Integer id;
    private Integer usuarioId;
    private String usuarioAlias;
    private Integer cancionId;
    private String cancionNombre;
    private LocalDateTime reproducidoEn;
}
