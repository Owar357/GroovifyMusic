package com.ITCHA2026.GroovyfyMusic.mappers;


import com.ITCHA2026.GroovyfyMusic.dto.ReproduccionResponseDTO;
import com.ITCHA2026.GroovyfyMusic.entities.Reproduccion;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UsuarioMapper.class, CancionMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReproduccionMapper {
    ReproduccionResponseDTO toDTO(Reproduccion entity);

    List<ReproduccionResponseDTO> toDtoList(List<Reproduccion> entities);
}
