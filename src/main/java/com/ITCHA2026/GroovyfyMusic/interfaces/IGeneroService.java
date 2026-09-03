package com.ITCHA2026.GroovyfyMusic.interfaces;

import com.ITCHA2026.GroovyfyMusic.dto.GeneroDTO;
import com.ITCHA2026.GroovyfyMusic.entities.Genero;

import java.util.List;

public interface IGeneroService {
    List<GeneroDTO> findAll();

    GeneroDTO findById(Integer id);

    List<GeneroDTO> buscarPorNombre(String nombre);

}
