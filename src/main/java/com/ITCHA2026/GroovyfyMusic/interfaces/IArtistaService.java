package com.ITCHA2026.GroovyfyMusic.interfaces;

import com.ITCHA2026.GroovyfyMusic.dto.ArtistaRegisterDTO;
import com.ITCHA2026.GroovyfyMusic.dto.ArtistaResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IArtistaService {
    List<ArtistaResponseDTO> findAll();
    ArtistaResponseDTO findById(Integer id);
    ArtistaResponseDTO save(ArtistaRegisterDTO dto, MultipartFile imagen);
    ArtistaResponseDTO update(Integer id, ArtistaRegisterDTO dto, MultipartFile imagen);
    void delete(Integer id);
}