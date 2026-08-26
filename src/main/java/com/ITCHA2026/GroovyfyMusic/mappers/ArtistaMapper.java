package com.ITCHA2026.GroovyfyMusic.mappers;


import com.ITCHA2026.GroovyfyMusic.dto.ArtistaRegisterDTO;
import com.ITCHA2026.GroovyfyMusic.dto.ArtistaResponseDTO;
import com.ITCHA2026.GroovyfyMusic.entities.Artista;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ArtistaMapper {

    @Mapping(target = "id", ignore = true)
    Artista toEntity(ArtistaRegisterDTO dto);

    ArtistaResponseDTO toResponseDTO(Artista Entity);

    List<ArtistaResponseDTO>toResponseDTOList(List<Artista> entities);

}
