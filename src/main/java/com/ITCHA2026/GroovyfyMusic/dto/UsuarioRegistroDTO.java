package com.ITCHA2026.GroovyfyMusic.dto;


import com.ITCHA2026.GroovyfyMusic.enums.Roles;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class UsuarioRegistroDTO {
    private  String alias;
    private  String correo;
    private  String password;
    private Roles rol;
    private LocalDate fechaNacimiento;
}
