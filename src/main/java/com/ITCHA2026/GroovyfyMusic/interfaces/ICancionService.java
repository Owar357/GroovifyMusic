package com.ITCHA2026.GroovyfyMusic.interfaces;

import com.ITCHA2026.GroovyfyMusic.dto.CancionFiltroDTO;
import com.ITCHA2026.GroovyfyMusic.dto.CancionPopularDTO;
import com.ITCHA2026.GroovyfyMusic.dto.CancionRegistroDTO;
import com.ITCHA2026.GroovyfyMusic.dto.CancionResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ICancionService {
    CancionResponseDTO register(CancionRegistroDTO dto, MultipartFile portada, MultipartFile archivoAudio, Integer usuarioId);

    CancionResponseDTO update(Integer id, CancionRegistroDTO dto, MultipartFile portada, MultipartFile archivoAudio);

    CancionResponseDTO findById(Integer id);

    List<CancionResponseDTO> findAll();

    List<CancionResponseDTO> search(CancionFiltroDTO filtro);

    List<CancionResponseDTO> findByAlbumId(Integer albumId);

    List<CancionPopularDTO> findPopularesByArtista(Integer artistaId, int limit);

    void delete(Integer id);
}
