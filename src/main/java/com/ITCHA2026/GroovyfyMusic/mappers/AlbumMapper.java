package com.ITCHA2026.GroovyfyMusic.mappers;

import com.ITCHA2026.GroovyfyMusic.dto.AlbumRegisterDTO;
import com.ITCHA2026.GroovyfyMusic.dto.AlbumResponseDTO;
import com.ITCHA2026.GroovyfyMusic.entities.Album;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AlbumMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "artista", ignore = true)
    Album toEntity(AlbumRegisterDTO dto);

    @Mapping(target = "artistaId", source = "artista.id")
    @Mapping(target = "nombreArtista", source = "artista.nombre")
    AlbumResponseDTO toResponseDTO(Album entity);

    List<AlbumResponseDTO> toResponseDTOList(List<Album> entities);
}