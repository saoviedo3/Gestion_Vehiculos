package com.banquito.originacion.service;

import com.banquito.originacion.controller.dto.AuditoriaDTO;
import com.banquito.originacion.controller.dto.ConcesionarioDTO;
import com.banquito.originacion.controller.dto.VendedorDTO;
import com.banquito.originacion.controller.mapper.ConcesionarioMapper;
import com.banquito.originacion.controller.mapper.VendedorMapper;
import com.banquito.originacion.enums.AccionAuditoriaEnum;
import com.banquito.originacion.enums.EstadoConcesionarioEnum;
import com.banquito.originacion.enums.EstadoVendedorEnum;
import com.banquito.originacion.exception.CreateEntityException;
import com.banquito.originacion.exception.ResourceNotFoundException;
import com.banquito.originacion.exception.UpdateEntityException;
import com.banquito.originacion.model.Concesionario;
import com.banquito.originacion.model.Vendedor;
import com.banquito.originacion.repository.ConcesionarioRepository;
import com.banquito.originacion.repository.VendedorRepository;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
public class ConcesionarioService {

    private final ConcesionarioRepository concesionarioRepository;
    private final ConcesionarioMapper concesionarioMapper;
    private final VendedorRepository vendedorRepository;
    private final VendedorMapper vendedorMapper;
    private final AuditoriaService auditoriaService;

    public ConcesionarioService(
            ConcesionarioRepository concesionarioRepository,
            ConcesionarioMapper concesionarioMapper,
            VendedorRepository vendedorRepository,
            VendedorMapper vendedorMapper,
            AuditoriaService auditoriaService) {
        this.concesionarioRepository = concesionarioRepository;
        this.concesionarioMapper = concesionarioMapper;
        this.vendedorRepository = vendedorRepository;
        this.vendedorMapper = vendedorMapper;
        this.auditoriaService = auditoriaService;
    }

    // ------------------------------------------- Métodos para Concesionario -------------------------------------------

    /**
     * Obtiene un concesionario por su ID.
     */

    @Transactional(readOnly = true)
    public ConcesionarioDTO findConcesionarioById(Integer id) {
        Concesionario entity = concesionarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Concesionario no encontrado con id=" + id));
        return concesionarioMapper.toDTO(entity);
    }

    /**
     * Obtiene todos los concesionarios registrados en el sistema.
     */
    @Transactional(readOnly = true)
    public List<ConcesionarioDTO> findAllConcesionarios() {
        return concesionarioRepository.findAll().stream()
                .map(concesionarioMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca concesionarios por su estado (ACTIVO / INACTIVO).
     */

    @Transactional(readOnly = true)
    public List<ConcesionarioDTO> findConcesionariosByEstado(EstadoConcesionarioEnum estado) {
        return concesionarioRepository.findByEstado(estado).stream()
                .map(concesionarioMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca concesionarios por razón social.
     */

    @Transactional(readOnly = true)
    public List<ConcesionarioDTO> findConcesionariosByRazonSocial(String parteRazon) {
        return concesionarioRepository
                .findByRazonSocialContainingIgnoreCase(parteRazon)
                .stream()
                .map(concesionarioMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca un concesionario por su email de contacto.
     */

    @Transactional(readOnly = true)
    public ConcesionarioDTO findConcesionarioByEmail(String emailContacto) {
        Concesionario entity = concesionarioRepository.findByEmailContacto(emailContacto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Concesionario no encontrado con email de contacto=" + emailContacto));
        return concesionarioMapper.toDTO(entity);
    }

    /**
     * Deshabilita un concesionario por su ID.
     */

    @Transactional
    public ConcesionarioDTO deactivateConcesionario(Integer id) {
        Concesionario c = concesionarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Concesionario no encontrado con id=" + id));
        c.setEstado(EstadoConcesionarioEnum.INACTIVO);
        return concesionarioMapper.toDTO(concesionarioRepository.save(c));
    }

    /**
     * Crea un nuevo concesionario.
     */

    @Transactional
    public ConcesionarioDTO createConcesionario(@Valid ConcesionarioDTO dto) {
        try {
            if (concesionarioRepository.existsByEmailContacto(dto.getEmailContacto())) {
                throw new CreateEntityException(
                        "Concesionario",
                        "Ya existe un concesionario con el mismo email de contacto: " + dto.getEmailContacto());
            }
            if (concesionarioRepository.existsByTelefono(dto.getTelefono())) {
                throw new CreateEntityException(
                        "Concesionario",
                        "Ya existe un concesionario con el mismo teléfono: " + dto.getTelefono());
            }

            Concesionario entity = concesionarioMapper.toModel(dto);
            entity.setId(null);
            entity.setVersion(null);
            Concesionario saved = concesionarioRepository.save(entity);

            AuditoriaDTO audDto = new AuditoriaDTO();
            audDto.setTabla(("concesionarios RAZON SOCIAL:" + dto.getRazonSocial()).substring(0, Math.min(40, ("concesionarios RAZON SOCIAL:" + dto.getRazonSocial()).length())));
            audDto.setAccion(AccionAuditoriaEnum.INSERT);
            audDto.setFechaHora(java.time.LocalDateTime.now());
            auditoriaService.createAuditoria(audDto);
            return concesionarioMapper.toDTO(saved);

        } catch (CreateEntityException e) {
            throw e;
        } catch (Exception e) {
            throw new CreateEntityException(
                    "Concesionario",
                    "Error al crear el concesionario. Detalle: " + e.getMessage());
        }
    }

    /**
     * Actualiza un concesionario existente.
     */

    @Transactional
    public ConcesionarioDTO updateConcesionario(Integer id, @Valid ConcesionarioDTO dto) {
        try {
            Concesionario existing = concesionarioRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Concesionario no encontrado con id=" + id));

            if (!existing.getEmailContacto().equals(dto.getEmailContacto())
                    && concesionarioRepository.existsByEmailContacto(dto.getEmailContacto())) {
                throw new CreateEntityException("Concesionario", "Email ya en uso");
            }
            if (!existing.getTelefono().equals(dto.getTelefono())
                    && concesionarioRepository.existsByTelefono(dto.getTelefono())) {
                throw new CreateEntityException("Concesionario", "Teléfono ya en uso");
            }

            existing.setRazonSocial(dto.getRazonSocial());
            existing.setDireccion(dto.getDireccion());
            existing.setTelefono(dto.getTelefono());
            existing.setEmailContacto(dto.getEmailContacto());
            existing.setEstado(dto.getEstado());
            existing.setVersion(dto.getVersion());

            Concesionario updated = concesionarioRepository.save(existing);

            AuditoriaDTO audDto = new AuditoriaDTO();
            audDto.setTabla(("concesionarios RAZON SOCIAL:" + dto.getRazonSocial()).substring(0, Math.min(40, ("concesionarios RAZON SOCIAL:" + dto.getRazonSocial()).length())));
            audDto.setAccion(AccionAuditoriaEnum.UPDATE);
            audDto.setFechaHora(java.time.LocalDateTime.now());
            auditoriaService.createAuditoria(audDto);

            return concesionarioMapper.toDTO(updated);

        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new UpdateEntityException(
                    "Concesionario",
                    "Error al actualizar el concesionario. Detalle: " + e.getMessage());
        }
    }

    // ------------------------------------------- Métodos para Vendedores -------------------------------------------

    /**
     * Obtiene un vendedor por su ID.
     */

    @Transactional(readOnly = true)
    public VendedorDTO findVendedorById(Integer id) {
        Vendedor entity = vendedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Vendedor no encontrado con id=" + id));
        return vendedorMapper.toDTO(entity);
    }

    /**
     * Obtiene todos los vendedores registrados en el sistema.
     */

    @Transactional(readOnly = true)
    public List<VendedorDTO> findAllVendedores() {
        return vendedorRepository.findAll().stream()
                .map(vendedorMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene un vendedor por su email.
     */
    @Transactional(readOnly = true)
    public VendedorDTO findVendedorByEmail(String email) {
        Vendedor entity = vendedorRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Vendedor no encontrado con email=" + email));
        return vendedorMapper.toDTO(entity);
    }

    /**
     * Obtiene los vendedores asociados a un concesionario específico.
     */

    @Transactional(readOnly = true)
    public List<VendedorDTO> findVendedoresByConcesionario(Integer idConcesionario) {
        return vendedorRepository.findByIdConcesionario(idConcesionario).stream()
                .map(vendedorMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene los vendedores por su estado.
     */

    @Transactional(readOnly = true)
    public List<VendedorDTO> findVendedoresByEstado(EstadoVendedorEnum estado) {
        return vendedorRepository.findByEstado(estado).stream()
                .map(vendedorMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Deshabilita un Vendedor por su ID.
     */

    @Transactional
    public VendedorDTO deactivateVendedor(Integer id) {
        Vendedor existing = vendedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Vendedor no encontrado con id=" + id));
        existing.setEstado(EstadoVendedorEnum.INACTIVO);
        // version se actualizará automáticamente por @Version
        Vendedor updated = vendedorRepository.save(existing);
        return vendedorMapper.toDTO(updated);
    }

    /**
     * Crea un nuevo vendedor en el sistema.
     * Verifica que no exista otro vendedor con el mismo email o teléfono.
     */

    @Transactional
    public VendedorDTO createVendedor(@Valid VendedorDTO dto) {
        try {
            if (vendedorRepository.existsByEmail(dto.getEmail())) {
                throw new CreateEntityException(
                        "Vendedor",
                        "Ya existe un vendedor con el mismo email: " + dto.getEmail());
            }
            if (vendedorRepository.existsByTelefono(dto.getTelefono())) {
                throw new CreateEntityException(
                        "Vendedor",
                        "Ya existe un vendedor con el mismo teléfono: " + dto.getTelefono());
            }

            Vendedor entity = vendedorMapper.toModel(dto);
            entity.setId(null);
            entity.setVersion(null);
            Vendedor saved = vendedorRepository.save(entity);

            AuditoriaDTO audDto = new AuditoriaDTO();
            audDto.setTabla(("vendedores Nombre:" + dto.getNombre()).substring(0, Math.min(40, ("vendedores Nombre:" + dto.getNombre()).length())));
            audDto.setAccion(AccionAuditoriaEnum.INSERT);
            audDto.setFechaHora(java.time.LocalDateTime.now());
            auditoriaService.createAuditoria(audDto);

            return vendedorMapper.toDTO(saved);

        } catch (CreateEntityException e) {
            throw e;
        } catch (Exception e) {
            throw new CreateEntityException(
                    "Vendedor",
                    "Error al crear el vendedor. Detalle: " + e.getMessage());
        }
    }

    /**
     * Actualiza un vendedor existente en el sistema.
     * Verifica que el vendedor exista y que no se modifiquen email o teléfono a
     * valores ya existentes.
     */

    @Transactional
    public VendedorDTO updateVendedor(Integer id, @Valid VendedorDTO dto) {
        try {
            Vendedor existing = vendedorRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Vendedor no encontrado con id=" + id));

            if (!existing.getEmail().equals(dto.getEmail())
                    && vendedorRepository.existsByEmail(dto.getEmail())) {
                throw new CreateEntityException("Vendedor", "Email ya en uso");
            }
            if (!existing.getTelefono().equals(dto.getTelefono())
                    && vendedorRepository.existsByTelefono(dto.getTelefono())) {
                throw new CreateEntityException("Vendedor", "Teléfono ya en uso");
            }

            existing.setIdConcesionario(dto.getIdConcesionario());
            existing.setNombre(dto.getNombre());
            existing.setTelefono(dto.getTelefono());
            existing.setEmail(dto.getEmail());
            existing.setEstado(dto.getEstado());
            existing.setVersion(dto.getVersion());

            Vendedor updated = vendedorRepository.save(existing);

            AuditoriaDTO audDto = new AuditoriaDTO();
            audDto.setTabla(("vendedores Nombre:" + dto.getNombre()).substring(0, Math.min(40, ("vendedores Nombre:" + dto.getNombre()).length())));
            audDto.setAccion(AccionAuditoriaEnum.UPDATE);
            audDto.setFechaHora(java.time.LocalDateTime.now());
            auditoriaService.createAuditoria(audDto);

            return vendedorMapper.toDTO(updated);

        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new UpdateEntityException(
                    "Vendedor",
                    "Error al actualizar el vendedor. Detalle: " + e.getMessage());
        }
    }

}
