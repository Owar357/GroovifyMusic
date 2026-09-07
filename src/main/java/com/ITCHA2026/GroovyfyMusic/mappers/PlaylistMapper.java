package com.ITCHA2026.GroovyfyMusic.mappers;

import com.ITCHA2026.GroovyfyMusic.dto.PlaylistRegistroDTO;
import com.ITCHA2026.GroovyfyMusic.dto.PlaylistResponseDTO;
import com.ITCHA2026.GroovyfyMusic.entities.Playlist;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UsuarioMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PlaylistMapper {

    PlaylistResponseDTO toDTO(Playlist entity);

    List<PlaylistResponseDTO> toDtoList(List<Playlist> entities);

    @Mapping(target = "portada", ignore = true)
    Playlist toEntity(PlaylistRegistroDTO dto);
}