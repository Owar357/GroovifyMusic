package com.ITCHA2026.GroovyfyMusic.interfaces;

import com.ITCHA2026.GroovyfyMusic.dto.AlbumRegisterDTO;
import com.ITCHA2026.GroovyfyMusic.dto.AlbumResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IAlbumService {

    List<AlbumResponseDTO> findAll();

    AlbumResponseDTO findById(Integer id);

    AlbumResponseDTO save(AlbumRegisterDTO albumRegisterDTO, MultipartFile file);

    AlbumResponseDTO update(Integer id, AlbumRegisterDTO albumRegisterDTO, MultipartFile file);

    void delete(Integer id);
}