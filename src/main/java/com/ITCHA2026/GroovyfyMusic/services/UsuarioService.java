package com.ITCHA2026.GroovyfyMusic.services;

import com.ITCHA2026.GroovyfyMusic.dto.UsuarioRegistroDTO;
import com.ITCHA2026.GroovyfyMusic.dto.UsuarioResponseDTO;
import com.ITCHA2026.GroovyfyMusic.entities.Role;
import com.ITCHA2026.GroovyfyMusic.entities.Usuario;
import com.ITCHA2026.GroovyfyMusic.exceptions.ConflictException;
import com.ITCHA2026.GroovyfyMusic.exceptions.ResourceNotFoundException;
import com.ITCHA2026.GroovyfyMusic.interfaces.IUsuarioService;
import com.ITCHA2026.GroovyfyMusic.mappers.UsuarioMapper;
import com.ITCHA2026.GroovyfyMusic.repository.RoleRepository;
import com.ITCHA2026.GroovyfyMusic.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService implements IUsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final UsuarioMapper usuarioMapper;
    private final CloudinaryService cloudinaryService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UsuarioResponseDTO register(UsuarioRegistroDTO dto, MultipartFile imagen) {

        if (usuarioRepository.existsByCorreo(dto.getCorreo())) {
            throw new ConflictException("El correo " + dto.getCorreo() + " ya está registrado.");
        }

        Usuario usuario = usuarioMapper.toEntity(dto);

        usuario.setBiografia(dto.getBiografia());

        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));

        Role role = roleRepository.findByNombre(dto.getRol())
                .orElseThrow(() -> new ResourceNotFoundException("Rol no válido: " + dto.getRol()));
        usuario.setRol(role);

        if (imagen != null && !imagen.isEmpty()) {
            String imagenUrl = cloudinaryService.uploadImage(imagen, "usuarios");
            usuario.setImagen(imagenUrl);
        }

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        return usuarioMapper.toDTO(usuarioGuardado);
    }

    @Override
    @Transactional
    public UsuarioResponseDTO update(Integer id, UsuarioRegistroDTO dto, MultipartFile imagen) {

        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));

        if (!usuarioExistente.getCorreo().equals(dto.getCorreo())
                && usuarioRepository.existsByCorreo(dto.getCorreo())) {
            throw new ConflictException("El correo " + dto.getCorreo() + " ya está registrado por otro usuario.");
        }

        usuarioExistente.setAlias(dto.getAlias());
        usuarioExistente.setCorreo(dto.getCorreo());
        usuarioExistente.setFechaNacimiento(dto.getFechaNacimiento());
        usuarioExistente.setBiografia(dto.getBiografia());

        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            usuarioExistente.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        Role role = roleRepository.findByNombre(dto.getRol())
                .orElseThrow(() -> new ResourceNotFoundException("Rol no válido: " + dto.getRol()));
        usuarioExistente.setRol(role);

        if (imagen != null && !imagen.isEmpty()) {
            String imagenUrl = cloudinaryService.uploadImage(imagen, "usuarios");
            usuarioExistente.setImagen(imagenUrl);
        }

        Usuario usuarioActualizado = usuarioRepository.save(usuarioExistente);

        return usuarioMapper.toDTO(usuarioActualizado);
    }

    @Override
    public void changePassword(Integer id, String passwordActual, String passwordNueva) {
        // Este método lo implementamos después
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponseDTO findById(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
        return usuarioMapper.toDTO(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> findAllArtistas() {
        return usuarioRepository.findByRolNombre("ARTISTA")
                .stream()
                .map(usuarioMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario no encontrado con id: " + id);
        }
        usuarioRepository.deleteById(id);
    }
}