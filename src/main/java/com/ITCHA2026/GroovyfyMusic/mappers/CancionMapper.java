package com.ITCHA2026.GroovyfyMusic.mappers;

import com.ITCHA2026.GroovyfyMusic.dto.CancionRegistroDTO;
import com.ITCHA2026.GroovyfyMusic.dto.CancionResponseDTO;
import com.ITCHA2026.GroovyfyMusic.entities.Cancion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UsuarioMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CancionMapper {
        CancionResponseDTO toDTO(Cancion entity);

        @Mapping(target = "id", ignore = true)
        @Mapping(target = "usuario", ignore = true)
        Cancion toEntity(CancionRegistroDTO dto);

        List<CancionResponseDTO> toDtoList(List<Cancion> entities);
}
