package com.ITCHA2026.GroovyfyMusic.interfaces;

import com.ITCHA2026.GroovyfyMusic.dto.UsuarioRegistroDTO;
import com.ITCHA2026.GroovyfyMusic.dto.UsuarioResponseDTO;
import com.ITCHA2026.GroovyfyMusic.entities.Usuario;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IUsuarioService {
    UsuarioResponseDTO register(UsuarioRegistroDTO dto, MultipartFile imagen);
    UsuarioResponseDTO update(Integer id, UsuarioRegistroDTO dto, MultipartFile imagen);
    void changePassword(Integer id, String passwordActual, String passwordNueva);
    UsuarioResponseDTO findById(Integer id);
    List<UsuarioResponseDTO> findAllArtistas();
    List<UsuarioResponseDTO> searchArtistas(String alias);
    void delete(Integer id);
}
