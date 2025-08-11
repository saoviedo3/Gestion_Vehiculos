package com.banquito.gestion_vehiculos.service;

import com.banquito.gestion_vehiculos.dto.LoginRequestDTO;
import com.banquito.gestion_vehiculos.dto.LoginResponseDTO;
import com.banquito.gestion_vehiculos.dto.UsuarioDTO;
import com.banquito.gestion_vehiculos.enums.RolEnum;
import com.banquito.gestion_vehiculos.exception.CreateEntityException;
import com.banquito.gestion_vehiculos.exception.ResourceNotFoundException;
import com.banquito.gestion_vehiculos.mapper.UsuarioMapper;
import com.banquito.gestion_vehiculos.model.Usuario;
import com.banquito.gestion_vehiculos.model.Vendedor;
import com.banquito.gestion_vehiculos.repository.UsuarioRepository;
import com.banquito.gestion_vehiculos.repository.VendedorRepository;
import com.banquito.gestion_vehiculos.repository.ConcesionarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;
    private final VendedorRepository vendedorRepository;

    public AuthService(UsuarioRepository usuarioRepository, 
                      UsuarioMapper usuarioMapper, 
                      PasswordEncoder passwordEncoder,
                      VendedorRepository vendedorRepository,
                      ConcesionarioRepository concesionarioRepository) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        this.passwordEncoder = passwordEncoder;
        this.vendedorRepository = vendedorRepository;

    }

    @Transactional
    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        Usuario usuario = usuarioRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con email: " + loginRequest.getEmail()));

        if (!usuario.isActivo()) {
            throw new CreateEntityException("Usuario", "El usuario está inactivo");
        }

        if (!passwordEncoder.matches(loginRequest.getPassword(), usuario.getPassword())) {
            throw new CreateEntityException("Usuario", "Contraseña incorrecta");
        }

        LoginResponseDTO response = new LoginResponseDTO();
        response.setMensaje("Login exitoso");
        response.setRol(usuario.getRol());
        response.setVendedorId(usuario.getVendedorId());
        response.setConcesionarioId(usuario.getConcesionarioId());
        response.setEmail(usuario.getEmail());

        return response;
    }

    @Transactional
    public UsuarioDTO createUsuario(UsuarioDTO dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new CreateEntityException("Usuario", "Ya existe un usuario con el mismo email: " + dto.getEmail());
        }

        Usuario usuario = usuarioMapper.toModel(dto);
        usuario.setId(UUID.randomUUID().toString());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        usuario.setVersion(0L);

        // Por ahora, no buscar automáticamente el vendedor
        // Solo crear el usuario con los datos proporcionados
        System.out.println("Creando usuario: " + dto.getEmail() + " con rol: " + dto.getRol());

        Usuario guardado = usuarioRepository.save(usuario);
        return usuarioMapper.toDTO(guardado);
    }

    public UsuarioDTO findUsuarioByEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con email: " + email));
        return usuarioMapper.toDTO(usuario);
    }

    public List<UsuarioDTO> findAllUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarios.stream().map(usuarioMapper::toDTO).toList();
    }

    @Transactional
    public UsuarioDTO updateUsuario(String id, UsuarioDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));

        if (!usuario.getEmail().equals(dto.getEmail()) && 
            usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new CreateEntityException("Usuario", "Email ya en uso");
        }

        usuario.setEmail(dto.getEmail());
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        usuario.setRol(dto.getRol());
        usuario.setVendedorId(dto.getVendedorId());
        usuario.setConcesionarioId(dto.getConcesionarioId());
        usuario.setActivo(dto.isActivo());
        usuario.setVersion(usuario.getVersion() == null ? 1L : usuario.getVersion() + 1);

        Usuario actualizado = usuarioRepository.save(usuario);
        return usuarioMapper.toDTO(actualizado);
    }

    @Transactional
    public void deleteUsuario(String id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario no encontrado con id: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    public boolean isAdmin(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElse(null);
        return usuario != null && usuario.getRol() == RolEnum.ADMIN;
    }

    public String getConcesionarioIdForVendedor(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElse(null);
        return usuario != null ? usuario.getConcesionarioId() : null;
    }

    public Vendedor findVendedorByEmail(String email) {
        return vendedorRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Vendedor no encontrado con email: " + email));
    }
} 