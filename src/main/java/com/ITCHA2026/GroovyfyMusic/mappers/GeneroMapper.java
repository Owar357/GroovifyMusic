package com.ITCHA2026.GroovyfyMusic.mappers;

import com.ITCHA2026.GroovyfyMusic.dto.GeneroDTO;
import com.ITCHA2026.GroovyfyMusic.entities.Genero;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GeneroMapper {

    Genero toEntity(GeneroDTO dto);

    Genero toDTO(Genero Entity);

}
