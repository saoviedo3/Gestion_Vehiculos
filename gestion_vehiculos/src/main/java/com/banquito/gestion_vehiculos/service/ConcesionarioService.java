package com.banquito.gestion_vehiculos.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banquito.gestion_vehiculos.dto.ConcesionarioDTO;
import com.banquito.gestion_vehiculos.dto.VendedorDTO;
import com.banquito.gestion_vehiculos.dto.VehiculoDTO;
import com.banquito.gestion_vehiculos.enums.EstadoConcesionarioEnum;
import com.banquito.gestion_vehiculos.enums.EstadoVendedorEnum;
import com.banquito.gestion_vehiculos.mapper.ConcesionarioMapper;
import com.banquito.gestion_vehiculos.mapper.VendedorMapper;
import com.banquito.gestion_vehiculos.mapper.VehiculoMapper;
import com.banquito.gestion_vehiculos.exception.CreateEntityException;
import com.banquito.gestion_vehiculos.exception.ResourceNotFoundException;
import com.banquito.gestion_vehiculos.exception.UpdateEntityException;
import com.banquito.gestion_vehiculos.model.Concesionario;
import com.banquito.gestion_vehiculos.model.Vendedor;
import com.banquito.gestion_vehiculos.model.Vehiculo;
import com.banquito.gestion_vehiculos.repository.ConcesionarioRepository;
import com.banquito.gestion_vehiculos.repository.VendedorRepository;
import com.banquito.gestion_vehiculos.repository.VehiculoRepository;

@Service
public class ConcesionarioService {

    private final ConcesionarioRepository concesionarioRepository;
    private final ConcesionarioMapper concesionarioMapper;
    private final VendedorRepository vendedorRepository;
    private final VendedorMapper vendedorMapper;
    private final VehiculoRepository vehiculoRepository;
    private final VehiculoMapper vehiculoMapper;

    public ConcesionarioService(
            ConcesionarioRepository concesionarioRepository,
            ConcesionarioMapper concesionarioMapper,
            VendedorRepository vendedorRepository,
            VendedorMapper vendedorMapper,
            VehiculoRepository vehiculoRepository,
            VehiculoMapper vehiculoMapper) {
        this.concesionarioRepository = concesionarioRepository;
        this.concesionarioMapper = concesionarioMapper;
        this.vendedorRepository = vendedorRepository;
        this.vendedorMapper = vendedorMapper;
        this.vehiculoRepository = vehiculoRepository;
        this.vehiculoMapper = vehiculoMapper;
    }

    // --------- Métodos para Concesionario ---------

    public ConcesionarioDTO findConcesionarioByRuc(String ruc) {
        try {
            Concesionario concesionario = concesionarioRepository.findByRuc(ruc)
                    .orElseThrow(() -> new ResourceNotFoundException("Concesionario no encontrado con RUC=" + ruc));
            return concesionarioMapper.toDTO(concesionario);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ResourceNotFoundException("Error al buscar concesionario por RUC: " + ruc);
        }
    }

    public List<ConcesionarioDTO> findConcesionariosByEstado(EstadoConcesionarioEnum estado) {
        try {
            List<Concesionario> lista = concesionarioRepository.findByEstado(estado);
            return lista.stream().map(concesionarioMapper::toDTO).toList();
        } catch (Exception e) {
            throw new ResourceNotFoundException("Error al buscar concesionarios por estado: " + estado);
        }
    }

    public List<ConcesionarioDTO> findConcesionariosByRazonSocial(String parteRazon) {
        try {
            List<Concesionario> lista = concesionarioRepository.findByRazonSocialContainingIgnoreCase(parteRazon);
            return lista.stream().map(concesionarioMapper::toDTO).toList();
        } catch (Exception e) {
            throw new ResourceNotFoundException("Error al buscar concesionarios por razón social: " + parteRazon);
        }
    }

    public ConcesionarioDTO findConcesionarioByEmail(String emailContacto) {
        try {
            Concesionario concesionario = concesionarioRepository.findByEmailContacto(emailContacto)
                .orElseThrow(() -> new ResourceNotFoundException("Concesionario no encontrado con email de contacto=" + emailContacto));
            return concesionarioMapper.toDTO(concesionario);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ResourceNotFoundException("Error al buscar concesionario por email: " + emailContacto);
        }
    }

    @Transactional
    public ConcesionarioDTO desactivateConcesionario(String ruc) {
        try {
            Concesionario concesionario = concesionarioRepository.findByRuc(ruc)
                .orElseThrow(() -> new ResourceNotFoundException("Concesionario no encontrado con RUC=" + ruc));
            concesionario.setEstado(EstadoConcesionarioEnum.INACTIVO);
            Concesionario actualizado = concesionarioRepository.save(concesionario);
            return concesionarioMapper.toDTO(actualizado);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new UpdateEntityException("Concesionario", "Error al desactivar el concesionario. Detalle: " + e.getMessage());
        }
    }

    @Transactional
    public ConcesionarioDTO createConcesionario(ConcesionarioDTO dto) {
        try {
            if (concesionarioRepository.existsByEmailContacto(dto.getEmailContacto())) {
                throw new CreateEntityException("Concesionario", "Ya existe un concesionario con el mismo email de contacto: " + dto.getEmailContacto());
            }
            if (concesionarioRepository.existsByTelefono(dto.getTelefono())) {
                throw new CreateEntityException("Concesionario", "Ya existe un concesionario con el mismo teléfono: " + dto.getTelefono());
            }
            Concesionario objeto = concesionarioMapper.toModel(dto);
            objeto.setId(null);
            objeto.setVersion(null);
            Concesionario guardado = concesionarioRepository.save(objeto);
            return concesionarioMapper.toDTO(guardado);
        } catch (CreateEntityException e) {
            throw e;
        } catch (Exception e) {
            throw new CreateEntityException("Concesionario", "Error al crear el concesionario. Detalle: " + e.getMessage());
        }
    }

    @Transactional
    public ConcesionarioDTO updateConcesionario(String ruc, ConcesionarioDTO dto) {
        try {
            Concesionario concesionario = concesionarioRepository.findByRuc(ruc)
                .orElseThrow(() -> new ResourceNotFoundException("Concesionario no encontrado con RUC=" + ruc));
            if (!concesionario.getEmailContacto().equals(dto.getEmailContacto()) &&
                concesionarioRepository.existsByEmailContacto(dto.getEmailContacto())) {
                throw new CreateEntityException("Concesionario", "Email ya en uso");
            }
            if (!concesionario.getTelefono().equals(dto.getTelefono()) &&
                concesionarioRepository.existsByTelefono(dto.getTelefono())) {
                throw new CreateEntityException("Concesionario", "Teléfono ya en uso");
            }
            concesionario.setRazonSocial(dto.getRazonSocial());
            concesionario.setDireccion(dto.getDireccion());
            concesionario.setTelefono(dto.getTelefono());
            concesionario.setEmailContacto(dto.getEmailContacto());
            concesionario.setEstado(dto.getEstado());
            concesionario.setVersion(dto.getVersion());
            Concesionario actualizado = concesionarioRepository.save(concesionario);
            return concesionarioMapper.toDTO(actualizado);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new UpdateEntityException("Concesionario", "Error al actualizar el concesionario. Detalle: " + e.getMessage());
        }
    }

    // --------- Métodos para Vendedor ---------

    public VendedorDTO findVendedorById(String id) {
        try {
            Vendedor vendedor = vendedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vendedor no encontrado con id=" + id));
            return vendedorMapper.toDTO(vendedor);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ResourceNotFoundException("Error al buscar vendedor: " + id);
        }
    }

    public VendedorDTO findVendedorByEmail(String email) {
        try {
            Vendedor vendedor = vendedorRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Vendedor no encontrado con email=" + email));
            return vendedorMapper.toDTO(vendedor);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ResourceNotFoundException("Error al buscar vendedor por email: " + email);
        }
    }

    public List<VendedorDTO> findVendedoresByConcesionario(String idConcesionario) {
        try {
            Concesionario concesionario = concesionarioRepository.findById(idConcesionario)
                .orElseThrow(() -> new ResourceNotFoundException("Concesionario no encontrado con id=" + idConcesionario));
            List<Vendedor> lista = concesionario.getVendedores();
            if (lista == null) return List.of();
            return lista.stream().map(vendedorMapper::toDTO).toList();
        } catch (Exception e) {
            throw new ResourceNotFoundException("Error al buscar vendedores del concesionario: " + idConcesionario);
        }
    }

    public List<VendedorDTO> findVendedoresByEstado(EstadoVendedorEnum estado) {
        try {
            List<Vendedor> lista = vendedorRepository.findByEstado(estado);
            return lista.stream().map(vendedorMapper::toDTO).toList();
        } catch (Exception e) {
            throw new ResourceNotFoundException("Error al buscar vendedores por estado: " + estado);
        }
    }

    @Transactional
    public VendedorDTO desactivateVendedor(String id) {
        try {
            Vendedor vendedor = vendedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vendedor no encontrado con id=" + id));
            vendedor.setEstado(EstadoVendedorEnum.INACTIVO);
            Vendedor actualizado = vendedorRepository.save(vendedor);
            return vendedorMapper.toDTO(actualizado);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new UpdateEntityException("Vendedor", "Error al desactivar el vendedor. Detalle: " + e.getMessage());
        }
    }

    @Transactional
    public VendedorDTO createVendedor(VendedorDTO dto) {
        try {
            if (vendedorRepository.existsByEmail(dto.getEmail())) {
                throw new CreateEntityException("Vendedor", "Ya existe un vendedor con el mismo email: " + dto.getEmail());
            }
            if (vendedorRepository.existsByTelefono(dto.getTelefono())) {
                throw new CreateEntityException("Vendedor", "Ya existe un vendedor con el mismo teléfono: " + dto.getTelefono());
            }
            Vendedor objeto = vendedorMapper.toModel(dto);
            objeto.setId(null);
            objeto.setVersion(null);
            Vendedor guardado = vendedorRepository.save(objeto);
            return vendedorMapper.toDTO(guardado);
        } catch (CreateEntityException e) {
            throw e;
        } catch (Exception e) {
            throw new CreateEntityException("Vendedor", "Error al crear el vendedor. Detalle: " + e.getMessage());
        }
    }

    @Transactional
    public VendedorDTO updateVendedor(String id, VendedorDTO dto) {
        try {
            Vendedor vendedor = vendedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vendedor no encontrado con id=" + id));
            if (!vendedor.getEmail().equals(dto.getEmail()) && vendedorRepository.existsByEmail(dto.getEmail())) {
                throw new CreateEntityException("Vendedor", "Email ya en uso");
            }
            if (!vendedor.getTelefono().equals(dto.getTelefono()) && vendedorRepository.existsByTelefono(dto.getTelefono())) {
                throw new CreateEntityException("Vendedor", "Teléfono ya en uso");
            }
            vendedor.setNombre(dto.getNombre());
            vendedor.setTelefono(dto.getTelefono());
            vendedor.setEmail(dto.getEmail());
            vendedor.setEstado(dto.getEstado());
            vendedor.setVersion(dto.getVersion());
            Vendedor actualizado = vendedorRepository.save(vendedor);
            return vendedorMapper.toDTO(actualizado);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new UpdateEntityException("Vendedor", "Error al actualizar el vendedor. Detalle: " + e.getMessage());
        }
    }

    public List<VehiculoDTO> findVehiculosByConcesionario(String idConcesionario) {
        try {
            Concesionario concesionario = concesionarioRepository.findById(idConcesionario)
                .orElseThrow(() -> new ResourceNotFoundException("Concesionario no encontrado con id=" + idConcesionario));
            List<Vehiculo> lista = concesionario.getVehiculos();
            if (lista == null) return List.of();
            return lista.stream().map(vehiculoMapper::toDTO).toList();
        } catch (Exception e) {
            throw new ResourceNotFoundException("Error al buscar vehículos del concesionario: " + idConcesionario);
        }
    }
}
