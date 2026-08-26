package com.ITCHA2026.GroovyfyMusic.services;

import com.ITCHA2026.GroovyfyMusic.dto.RoleDTO;
import com.ITCHA2026.GroovyfyMusic.entities.Role;
import com.ITCHA2026.GroovyfyMusic.exceptions.ConflictException;
import com.ITCHA2026.GroovyfyMusic.exceptions.ResourceNotFoundException;
import com.ITCHA2026.GroovyfyMusic.interfaces.IRoleService;
import com.ITCHA2026.GroovyfyMusic.mappers.RoleMapper;
import com.ITCHA2026.GroovyfyMusic.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RoleService implements IRoleService {

    private  final RoleRepository roleRepository;
    private  final RoleMapper roleMapper;


    @Override
    @Transactional(readOnly = true)
    public List<RoleDTO> findAll() {
      return  roleMapper.toDtoList(roleRepository.findAll());
    }

    @Override
    @Transactional
    public RoleDTO save(RoleDTO dto) {
        if (roleRepository.existsByNombre(dto.getNombre())) {
            throw new ConflictException("Ya existe un rol con ese nombre");
        }
        Role role = roleMapper.toEntity(dto);
        return roleMapper.toDTO(roleRepository.save(role));
    }


    @Override
    @Transactional
    public RoleDTO update(Integer id, RoleDTO dto) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con id: " + id));

        role.setNombre(dto.getNombre());
        return roleMapper.toDTO(roleRepository.save(role));
    }

    @Override
    @Transactional(readOnly = true)
    public RoleDTO findById(Integer id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con id: " + id));
        return roleMapper.toDTO(role);
    }
}
