package com.ITCHA2026.GroovyfyMusic.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class ReproduccionFiltroDTO {
    private LocalDate desde;
    private LocalDate hasta;
}
