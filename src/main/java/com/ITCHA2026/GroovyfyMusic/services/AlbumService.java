package com.ITCHA2026.GroovyfyMusic.services;

import com.ITCHA2026.GroovyfyMusic.dto.AlbumRegisterDTO;
import com.ITCHA2026.GroovyfyMusic.dto.AlbumResponseDTO;
import com.ITCHA2026.GroovyfyMusic.interfaces.IAlbumService;
import com.ITCHA2026.GroovyfyMusic.mappers.AlbumMapper;
import com.ITCHA2026.GroovyfyMusic.repository.AlbumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlbumService implements IAlbumService {
    final private AlbumRepository repository;
    final private AlbumMapper mapper;

    @Override
    public List<AlbumRegisterDTO> findAll() {
        return List.of();
    }

    @Override
    public AlbumResponseDTO findById(Integer id) {
        return null;
    }

    @Override
    public AlbumResponseDTO save(AlbumRegisterDTO albumRegisterDTO) {
        return null;
    }

    @Override
    public AlbumResponseDTO Update(Integer id, AlbumRegisterDTO albumRegisterDTO) {
        return null;
    }

    @Override
    public void delete(Integer id) {

    }
}
