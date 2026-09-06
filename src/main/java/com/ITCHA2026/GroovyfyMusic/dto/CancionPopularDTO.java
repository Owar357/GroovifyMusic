package com.ITCHA2026.GroovyfyMusic.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CancionPopularDTO {
    private Integer id;
    private String nombre;
    private Integer duracionSegundos;
    private String portada;
    private String archivoAudio;
    private long reproducciones;
}
