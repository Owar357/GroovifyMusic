package com.ITCHA2026.GroovyfyMusic.services;

import com.ITCHA2026.GroovyfyMusic.dto.ArtistaRegisterDTO;
import com.ITCHA2026.GroovyfyMusic.dto.ArtistaResponseDTO;
import com.ITCHA2026.GroovyfyMusic.entities.Artista;
import com.ITCHA2026.GroovyfyMusic.exceptions.BadRequestException;
import com.ITCHA2026.GroovyfyMusic.exceptions.ConflictException;
import com.ITCHA2026.GroovyfyMusic.exceptions.ResourceNotFoundException;
import com.ITCHA2026.GroovyfyMusic.interfaces.IArtistaService;
import com.ITCHA2026.GroovyfyMusic.mappers.ArtistaMapper;
import com.ITCHA2026.GroovyfyMusic.repository.AlbumRepository;
import com.ITCHA2026.GroovyfyMusic.repository.ArtistaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArtistaService implements IArtistaService {

    private final ArtistaRepository repository;
    private final AlbumRepository albumRepository;
    private final ArtistaMapper mapper;
    private final CloudinaryService cloudinaryService;

    @Override
    @Transactional(readOnly = true)
    public List<ArtistaResponseDTO> findAll() {
        return mapper.toResponseDTOList(repository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public ArtistaResponseDTO findById(Integer id) {
        Artista artista = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el artista con el ID: " + id));
        return mapper.toResponseDTO(artista);
    }

    @Override
    @Transactional
    public ArtistaResponseDTO save(ArtistaRegisterDTO dto, MultipartFile imagen) {
        // 1. Validar nombre duplicado
        if (repository.existsByNombre(dto.getNombre())) {
            throw new ConflictException("Ya existe un artista registrado con el nombre: " + dto.getNombre());
        }

        Artista artista = mapper.toEntity(dto);

        // 2. Subir imagen a Cloudinary si se adjuntó un archivo
        if (imagen != null && !imagen.isEmpty()) {
            String urlImagen = cloudinaryService.uploadImage(imagen, "groovyfymusic/artistas");
            artista.setImagen(urlImagen); // Se usa setImagen según tu entidad
        }

        return mapper.toResponseDTO(repository.save(artista));
    }

    @Override
    @Transactional
    public ArtistaResponseDTO update(Integer id, ArtistaRegisterDTO dto, MultipartFile imagen) {
        // 1. Validar existencia del artista
        Artista artista = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el artista con el ID: " + id));

        // 2. Validar que el nuevo nombre no le pertenezca a otro artista
        if (repository.existsByNombreAndIdNot(dto.getNombre(), id)) {
            throw new ConflictException("Ya existe otro artista con el nombre: " + dto.getNombre());
        }

        // 3. Actualizar los campos exactos de tu entidad
        artista.setNombre(dto.getNombre());
        artista.setBiografia(dto.getBiografia());

        // 4. Si envían una nueva foto, actualizar el campo 'imagen'
        if (imagen != null && !imagen.isEmpty()) {
            String urlImagen = cloudinaryService.uploadImage(imagen, "groovyfymusic/artistas");
            artista.setImagen(urlImagen);
        }

        return mapper.toResponseDTO(repository.save(artista));
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        Artista artista = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el artista con el ID: " + id));

        if (albumRepository.existsByArtistaId(id)) {
            throw new BadRequestException("No se puede eliminar el artista porque tiene álbumes asociados.");
        }

        repository.delete(artista);
    }
}