package com.ITCHA2026.GroovyfyMusic.services;

import com.ITCHA2026.GroovyfyMusic.dto.GeneroDTO;
import com.ITCHA2026.GroovyfyMusic.entities.Genero;
import com.ITCHA2026.GroovyfyMusic.exceptions.ResourceNotFoundException;
import com.ITCHA2026.GroovyfyMusic.interfaces.IGeneroService;
import com.ITCHA2026.GroovyfyMusic.mappers.GeneroMapper;
import com.ITCHA2026.GroovyfyMusic.repository.GeneroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GeneroService implements IGeneroService {

    private final GeneroRepository generoRepository;
    private final GeneroMapper mapper;

    @Override
    public List<GeneroDTO> findAll() {
        return mapper.toDTOList(generoRepository.findAll());
    }

    @Override
    public GeneroDTO findById(Integer id) {
        Genero genero = generoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El género con ID " + id + " no existe"));
        return mapper.toDTO(genero);
    }

    @Override
    public List<GeneroDTO> buscarPorNombre(String nombre) {
        List<Genero> generos = generoRepository.findByNombreContainingIgnoreCase(nombre);
        return mapper.toDTOList(generos);
    }
}