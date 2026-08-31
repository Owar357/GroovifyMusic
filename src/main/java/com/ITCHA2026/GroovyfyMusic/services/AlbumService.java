package com.ITCHA2026.GroovyfyMusic.services;

import com.ITCHA2026.GroovyfyMusic.dto.AlbumRegisterDTO;
import com.ITCHA2026.GroovyfyMusic.dto.AlbumResponseDTO;
import com.ITCHA2026.GroovyfyMusic.entities.Album;
import com.ITCHA2026.GroovyfyMusic.entities.Artista;
import com.ITCHA2026.GroovyfyMusic.exceptions.ResourceNotFoundException;
import com.ITCHA2026.GroovyfyMusic.interfaces.IAlbumService;
import com.ITCHA2026.GroovyfyMusic.mappers.AlbumMapper;
import com.ITCHA2026.GroovyfyMusic.repository.AlbumRepository;
import com.ITCHA2026.GroovyfyMusic.repository.ArtistaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlbumService implements IAlbumService {

    private final AlbumRepository repository;
    private final ArtistaRepository artrepository;
    private final AlbumMapper mapper;
    private final CloudinaryService cloudinaryService;

    // Carpeta donde se organizarán las portadas dentro de Cloudinary
    private static final String CLOUDINARY_FOLDER = "groovyfy/portadas";

    @Override
    @Transactional(readOnly = true)
    public List<AlbumResponseDTO> findAll() {
        return mapper.toResponseDTOList(repository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public AlbumResponseDTO findById(Integer id) {
        Album entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el álbum con ID: " + id));
        return mapper.toResponseDTO(entity);
    }

    @Override
    @Transactional
    public AlbumResponseDTO save(AlbumRegisterDTO albumRegisterDTO, MultipartFile file) {
        // 1. Validar existencia del artista
        Artista artista = artrepository.findById(albumRegisterDTO.getArtistaId())
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el artista con ID: " + albumRegisterDTO.getArtistaId()));

        // 2. Mapear DTO a Entidad y asignar relación
        Album album = mapper.toEntity(albumRegisterDTO);
        album.setArtista(artista);

        // 3. Subir portada mediante uploadImage(file, folder)
        if (file != null && !file.isEmpty()) {
            String urlPortada = cloudinaryService.uploadImage(file, CLOUDINARY_FOLDER);
            album.setPortada(urlPortada);
        }

        return mapper.toResponseDTO(repository.save(album));
    }

    @Override
    @Transactional
    public AlbumResponseDTO update(Integer id, AlbumRegisterDTO albumRegisterDTO, MultipartFile file) {
        // 1. Verificar existencia del álbum
        Album album = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el álbum con el ID: " + id));

        // 2. Verificar existencia del artista
        Artista artista = artrepository.findById(albumRegisterDTO.getArtistaId())
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el artista especificado."));

        // 3. Actualizar campos
        album.setNombre(albumRegisterDTO.getNombre());
        album.setFechaLanzamiento(albumRegisterDTO.getFechaLanzamiento());
        album.setDuracion(albumRegisterDTO.getDuracion());
        album.setArtista(artista);

        // 4. Actualizar portada mediante uploadImage(file, folder) si se envía un archivo nuevo
        if (file != null && !file.isEmpty()) {
            String urlPortada = cloudinaryService.uploadImage(file, CLOUDINARY_FOLDER);
            album.setPortada(urlPortada);
        }

        return mapper.toResponseDTO(repository.save(album));
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        Album album = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el álbum que se quiere eliminar."));

        repository.delete(album);
    }
}