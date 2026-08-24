package com.ITCHA2026.GroovyfyMusic.interfaces;

import com.ITCHA2026.GroovyfyMusic.dto.ArtistaRegisterDTO;
import com.ITCHA2026.GroovyfyMusic.dto.ArtistaResponseDTO;

import java.util.List;

public interface IArtistaService {
    List<ArtistaResponseDTO> findAll();

    ArtistaResponseDTO findById(Integer id);

    ArtistaResponseDTO save (ArtistaRegisterDTO artistaRegisterDTO);

    ArtistaResponseDTO Update (Integer id, ArtistaRegisterDTO artistaRegisterDTO);

    void delete (Integer id);
}
