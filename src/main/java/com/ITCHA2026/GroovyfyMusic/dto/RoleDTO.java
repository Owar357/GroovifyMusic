package com.ITCHA2026.GroovyfyMusic.dto;

import com.ITCHA2026.GroovyfyMusic.enums.Roles;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RoleDTO {
    private  Integer id;
    private Roles nombre;
    private LocalDateTime creadoEn;
}
