package com.banquito.gestion_vehiculos.service;

import com.banquito.gestion_vehiculos.enums.RolEnum;
import com.banquito.gestion_vehiculos.exception.CreateEntityException;
import com.banquito.gestion_vehiculos.model.Concesionario;
import com.banquito.gestion_vehiculos.repository.ConcesionarioRepository;
import com.banquito.gestion_vehiculos.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {

    private final AuthService authService;
    private final UsuarioRepository usuarioRepository;
    private final ConcesionarioRepository concesionarioRepository;

    public AuthorizationService(AuthService authService, 
                              UsuarioRepository usuarioRepository,
                              ConcesionarioRepository concesionarioRepository) {
        this.authService = authService;
        this.usuarioRepository = usuarioRepository;
        this.concesionarioRepository = concesionarioRepository;
    }

    /**
     * Valida que el usuario tenga acceso al concesionario específico
     */
    public void validateConcesionarioAccess(String userEmail, String concesionarioRuc) {
        if (authService.isAdmin(userEmail)) {
            return; // Los admins pueden acceder a cualquier concesionario
        }

        // Para vendedores, verificar que pertenezcan al concesionario
        String userConcesionarioId = authService.getConcesionarioIdForVendedor(userEmail);
        if (userConcesionarioId == null) {
            throw new CreateEntityException("Autorización", "Usuario no tiene concesionario asignado");
        }

        Concesionario concesionario = concesionarioRepository.findByRuc(concesionarioRuc)
                .orElseThrow(() -> new CreateEntityException("Autorización", "Concesionario no encontrado"));

        if (!concesionario.getId().equals(userConcesionarioId)) {
            throw new CreateEntityException("Autorización", "No tienes permisos para acceder a este concesionario");
        }
    }

    /**
     * Valida que el usuario sea admin
     */
    public void validateAdminAccess(String userEmail) {
        if (!authService.isAdmin(userEmail)) {
            throw new CreateEntityException("Autorización", "Se requieren permisos de administrador");
        }
    }

    /**
     * Obtiene el RUC del concesionario del usuario vendedor
     */
    public String getConcesionarioRucForVendedor(String userEmail) {
        if (authService.isAdmin(userEmail)) {
            return null; // Los admins pueden acceder a cualquier concesionario
        }

        String concesionarioId = authService.getConcesionarioIdForVendedor(userEmail);
        if (concesionarioId == null) {
            throw new CreateEntityException("Autorización", "Usuario no tiene concesionario asignado");
        }

        Concesionario concesionario = concesionarioRepository.findById(concesionarioId)
                .orElseThrow(() -> new CreateEntityException("Autorización", "Concesionario no encontrado"));

        return concesionario.getRuc();
    }

    /**
     * Verifica si el usuario es admin
     */
    public boolean isAdmin(String userEmail) {
        return authService.isAdmin(userEmail);
    }

    /**
     * Verifica si el usuario es vendedor
     */
    public boolean isVendedor(String userEmail) {
        return usuarioRepository.findByEmail(userEmail)
                .map(usuario -> usuario.getRol() == RolEnum.VENDEDOR)
                .orElse(false);
    }
} 