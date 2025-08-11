package com.banquito.gestion_vehiculos.mapper;

import com.banquito.gestion_vehiculos.dto.UsuarioDTO;
import com.banquito.gestion_vehiculos.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public UsuarioDTO toDTO(Usuario usuario) {
        if (usuario == null) return null;
        
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setEmail(usuario.getEmail());
        dto.setRol(usuario.getRol());
        dto.setVendedorId(usuario.getVendedorId());
        dto.setConcesionarioId(usuario.getConcesionarioId());
        dto.setActivo(usuario.isActivo());
        dto.setVersion(usuario.getVersion());
        return dto;
    }

    public Usuario toModel(UsuarioDTO dto) {
        if (dto == null) return null;
        
        Usuario usuario = new Usuario();
        usuario.setId(dto.getId());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(dto.getPassword()); // Se hasheará en el servicio
        usuario.setRol(dto.getRol());
        usuario.setVendedorId(dto.getVendedorId());
        usuario.setConcesionarioId(dto.getConcesionarioId());
        usuario.setActivo(dto.isActivo());
        usuario.setVersion(dto.getVersion());
        return usuario;
    }
} 