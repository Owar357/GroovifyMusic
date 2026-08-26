package com.ITCHA2026.GroovyfyMusic.interfaces;

import com.ITCHA2026.GroovyfyMusic.dto.GeneroDTO;

import java.util.List;

public interface IGeneroService {
    List<GeneroDTO> findAll();

    GeneroDTO findById(Integer id, GeneroDTO dto);

    GeneroDTO save (GeneroDTO dto);

    GeneroDTO update (Integer id, GeneroDTO dto);

    void delete (Integer id);
}
