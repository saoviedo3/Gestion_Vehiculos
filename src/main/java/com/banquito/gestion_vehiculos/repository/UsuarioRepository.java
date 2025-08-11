package com.banquito.gestion_vehiculos.repository;

import com.banquito.gestion_vehiculos.model.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends MongoRepository<Usuario, String> {
    
    Optional<Usuario> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    Optional<Usuario> findByVendedorId(String vendedorId);
    
    Optional<Usuario> findByConcesionarioId(String concesionarioId);
} 