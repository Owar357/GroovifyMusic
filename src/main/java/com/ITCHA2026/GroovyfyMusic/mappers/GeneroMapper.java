package com.ITCHA2026.GroovyfyMusic.mappers;

import com.ITCHA2026.GroovyfyMusic.dto.GeneroDTO;
import com.ITCHA2026.GroovyfyMusic.entities.Genero;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GeneroMapper {

    @Mapping(target = "id", ignore = true)
    Genero toEntity(GeneroDTO dto);

    GeneroDTO toDTO(Genero entity);

    List<GeneroDTO> toDTOList(List<Genero> entities);
}