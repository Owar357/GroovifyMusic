package com.ITCHA2026.GroovyfyMusic.services;

import com.ITCHA2026.GroovyfyMusic.dto.auth.LoginRequestDTO;
import com.ITCHA2026.GroovyfyMusic.dto.auth.LoginResponseDTO;
import com.ITCHA2026.GroovyfyMusic.interfaces.IAuthService;
import com.ITCHA2026.GroovyfyMusic.security.JwtService;
import com.ITCHA2026.GroovyfyMusic.security.UsuarioPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public LoginResponseDTO login(LoginRequestDTO request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getCorreo(),
                        request.getPassword()
                )
        );

        UsuarioPrincipal principal = (UsuarioPrincipal) authentication.getPrincipal();
        String token = jwtService.generarToken(principal);

        return new LoginResponseDTO(
                token,
                principal.getAlias(),
                principal.getCorreo(),
                principal.getRol()
        );
    }
}