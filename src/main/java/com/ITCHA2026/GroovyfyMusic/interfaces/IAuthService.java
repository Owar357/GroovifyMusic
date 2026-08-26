package com.ITCHA2026.GroovyfyMusic.interfaces;

import com.ITCHA2026.GroovyfyMusic.dto.auth.LoginRequestDTO;
import com.ITCHA2026.GroovyfyMusic.dto.auth.LoginResponseDTO;

public interface IAuthService {
    LoginResponseDTO login(LoginRequestDTO dto);
}
