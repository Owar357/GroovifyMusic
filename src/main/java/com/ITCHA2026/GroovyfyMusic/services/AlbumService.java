package com.ITCHA2026.GroovyfyMusic.services;

import com.ITCHA2026.GroovyfyMusic.dto.AlbumRegisterDTO;
import com.ITCHA2026.GroovyfyMusic.dto.AlbumResponseDTO;
import com.ITCHA2026.GroovyfyMusic.entities.Album;
import com.ITCHA2026.GroovyfyMusic.entities.Usuario;
import com.ITCHA2026.GroovyfyMusic.exceptions.ResourceNotFoundException;
import com.ITCHA2026.GroovyfyMusic.interfaces.IAlbumService;
import com.ITCHA2026.GroovyfyMusic.mappers.AlbumMapper;
import com.ITCHA2026.GroovyfyMusic.repository.AlbumRepository;
import com.ITCHA2026.GroovyfyMusic.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlbumService implements IAlbumService {

    private final AlbumRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final AlbumMapper mapper;
    private final CloudinaryService cloudinaryService;

    private static final String CLOUDINARY_FOLDER = "groovyfy/portadas";

    @Override
    @Transactional(readOnly = true)
    public List<AlbumResponseDTO> findAll() {
        return mapper.toResponseDTOList(repository.findAll());
    }

        @Override
        @Transactional(readOnly = true)
        public AlbumResponseDTO findById(Integer id) {
            Album album = repository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("No se encontró el álbum con ID: " + id));
            return mapper.toResponseDTO(album);
        }

    @Override
    @Transactional(readOnly = true)
    public List<AlbumResponseDTO> findByArtistaId(Integer artistaId) {
        return mapper.toResponseDTOList(repository.findByArtistaId(artistaId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AlbumResponseDTO> search(String nombre) {
        return mapper.toResponseDTOList(repository.findByNombreContainingIgnoreCase(nombre));
    }

    @Override
    @Transactional
    public AlbumResponseDTO save(AlbumRegisterDTO albumRegisterDTO, MultipartFile file) {
        Usuario artista = usuarioRepository.findById(albumRegisterDTO.getArtistaId())
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el usuario con ID: " + albumRegisterDTO.getArtistaId()));

        if (!artista.getRol().getNombre().name().equals("ARTISTA")) {
            throw new IllegalArgumentException("El usuario seleccionado no es un Artista y no puede registrar álbumes.");
        }

        Album album = mapper.toEntity(albumRegisterDTO);
        album.setArtista(artista);

        //con esta condicion se obligara a la funcionalidad a crearlo en cero ai entraranlas cacniones afectando la duracion
        album.setDuracion("00:00");

        if (file != null && !file.isEmpty()) {
            String urlPortada = cloudinaryService.uploadImage(file, CLOUDINARY_FOLDER);
            album.setPortada(urlPortada);
        }

        return mapper.toResponseDTO(repository.save(album));
    }

    @Override
    @Transactional
    public AlbumResponseDTO update(Integer id, AlbumRegisterDTO albumRegisterDTO, MultipartFile file) {
        Album album = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el álbum con el ID: " + id));

        Usuario artista = usuarioRepository.findById(albumRegisterDTO.getArtistaId())
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el artista especificado."));

        album.setNombre(albumRegisterDTO.getNombre());
        album.setFechaLanzamiento(albumRegisterDTO.getFechaLanzamiento());
        album.setArtista(artista);


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