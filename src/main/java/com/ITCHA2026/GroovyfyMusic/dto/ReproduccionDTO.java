package com.ITCHA2026.GroovyfyMusic.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReproduccionDTO {
    private  Integer id;
    private UsuarioResponseDTO usuario;
    private CancionResponseDTO cancion;
    private LocalDateTime reproducidoEn;
}
