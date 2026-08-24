package com.ITCHA2026.GroovyfyMusic.interfaces;

import com.ITCHA2026.GroovyfyMusic.dto.AlbumRegisterDTO;
import com.ITCHA2026.GroovyfyMusic.dto.AlbumResponseDTO;
import com.ITCHA2026.GroovyfyMusic.entities.Album;

import java.util.List;

public interface IAlbumService {

    List<AlbumRegisterDTO> findAll();

    AlbumResponseDTO findById(Integer id);

    AlbumResponseDTO save (AlbumRegisterDTO albumRegisterDTO);

    AlbumResponseDTO Update (Integer id, AlbumRegisterDTO albumRegisterDTO);

    void delete (Integer id );



}
