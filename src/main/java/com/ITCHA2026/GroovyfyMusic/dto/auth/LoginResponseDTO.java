package com.ITCHA2026.GroovyfyMusic.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponseDTO {
    private String token;
    private String alias;
    private String correo;
    private String rol;
}