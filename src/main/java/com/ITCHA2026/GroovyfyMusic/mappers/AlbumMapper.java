package com.ITCHA2026.GroovyfyMusic.mappers;

import  com.ITCHA2026.GroovyfyMusic.dto.AlbumRegisterDTO;
import com.ITCHA2026.GroovyfyMusic.dto.AlbumResponseDTO;
import com.ITCHA2026.GroovyfyMusic.entities.Album;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ArtistaMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AlbumMapper {

    @Mapping(target = "id", ignore = true)
    Album toEntity(AlbumRegisterDTO dto);

    AlbumResponseDTO toResponseDTO(Album entity);

    List<AlbumResponseDTO> toResponseDTOList(List<Album> entities);
}