package com.ITCHA2026.GroovyfyMusic.interfaces;

import com.ITCHA2026.GroovyfyMusic.dto.RoleDTO;

import java.util.List;

public interface IRoleService {
    List<RoleDTO> findAll();

    RoleDTO save(RoleDTO dto);

    RoleDTO update(Integer id, RoleDTO dto );

    RoleDTO findById(Integer id);

}
