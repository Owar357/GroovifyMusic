package com.ITCHA2026.GroovyfyMusic.mappers;

import com.ITCHA2026.GroovyfyMusic.dto.RoleDTO;
import com.ITCHA2026.GroovyfyMusic.entities.Role;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleMapper {

    RoleDTO toDTO(Role entity);

    Role toEntity(RoleDTO dto);

    List<RoleDTO> toDtoList(List<Role> entities);
}
