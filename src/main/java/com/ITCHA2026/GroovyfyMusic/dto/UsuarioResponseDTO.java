package com.ITCHA2026.GroovyfyMusic.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class UsuarioResponseDTO {
    private Integer id;
    private String alias;
    private String correo;
    private String imagen;
    private LocalDate fechaNacimiento;
    private RoleDTO rol;
    private LocalDateTime creadoEn;
}
