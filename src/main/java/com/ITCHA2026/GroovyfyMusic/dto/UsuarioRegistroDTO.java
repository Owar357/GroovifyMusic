package com.ITCHA2026.GroovyfyMusic.dto;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class UsuarioRegistroDTO {
    private  String alias;
    private  String correo;
    private  String password;
    private  String imagen;
    private LocalDate fechaNacimiento;
}
