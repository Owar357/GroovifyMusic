package com.ITCHA2026.GroovyfyMusic.services;

import com.ITCHA2026.GroovyfyMusic.dto.GeneroDTO;
import com.ITCHA2026.GroovyfyMusic.entities.Genero;
import com.ITCHA2026.GroovyfyMusic.exceptions.ConflictException;
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
public class GeneroService implements IGeneroService {

    private final GeneroMapper mapper;
    private final GeneroRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<GeneroDTO> findAll() {
        return mapper.toDTOList(repository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public GeneroDTO findById(Integer id) {
        Genero genero = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el género con el ID: " + id));
        return mapper.toDTO(genero);
    }

    @Override
    @Transactional
    public GeneroDTO save(GeneroDTO dto) {
        // Validar si ya existe un género con ese nombre
        if (repository.existsByNombre(dto.getNombre())) {
            throw new ConflictException("Ya existe un género registrado con el nombre: " + dto.getNombre());
        }

        Genero genero = mapper.toEntity(dto);
        return mapper.toDTO(repository.save(genero));
    }

    @Override
    @Transactional
    public GeneroDTO update(Integer id, GeneroDTO dto) {
        // 1. Verificar que el género a actualizar exista
        Genero genero = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el género con el ID: " + id));

        // 2. Validar que el nuevo nombre no le pertenezca a OTRO género (excluyendo el ID actual)
        if (repository.existsByNombreAndIdNot(dto.getNombre(), id)) {
            throw new ConflictException("Ya existe otro género con el nombre: " + dto.getNombre());
        }

        // 3. Actualizar la entidad persistida
        genero.setNombre(dto.getNombre());

        return mapper.toDTO(repository.save(genero));
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        Genero genero = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el género con el ID: " + id));

        repository.delete(genero);
    }
}