package com.banquito.gestion_vehiculos.service;

import java.util.List;
import java.util.ArrayList;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banquito.gestion_vehiculos.dto.ConcesionarioDTO;
import com.banquito.gestion_vehiculos.dto.VendedorDTO;
import com.banquito.gestion_vehiculos.dto.VehiculoDTO;
import com.banquito.gestion_vehiculos.enums.EstadoConcesionarioEnum;
import com.banquito.gestion_vehiculos.mapper.ConcesionarioMapper;
import com.banquito.gestion_vehiculos.mapper.VehiculoMapper;
import com.banquito.gestion_vehiculos.exception.CreateEntityException;
import com.banquito.gestion_vehiculos.exception.ResourceNotFoundException;
import com.banquito.gestion_vehiculos.exception.UpdateEntityException;
import com.banquito.gestion_vehiculos.model.Concesionario;
import com.banquito.gestion_vehiculos.model.Vehiculo;
import com.banquito.gestion_vehiculos.model.Vendedor;
import com.banquito.gestion_vehiculos.mapper.VendedorMapper;
import com.banquito.gestion_vehiculos.repository.ConcesionarioRepository;
import com.banquito.gestion_vehiculos.repository.IdentificadorVehiculoRepository;

@Service
public class ConcesionarioService {

    private final ConcesionarioRepository concesionarioRepository;
    private final ConcesionarioMapper concesionarioMapper;
    private final VehiculoMapper vehiculoMapper;
    private final VendedorMapper vendedorMapper;

    public ConcesionarioService(
            ConcesionarioRepository concesionarioRepository,
            ConcesionarioMapper concesionarioMapper,
            VehiculoMapper vehiculoMapper,
            VendedorMapper vendedorMapper,
            IdentificadorVehiculoRepository identificadorVehiculoRepository) {
        this.concesionarioRepository = concesionarioRepository;
        this.concesionarioMapper = concesionarioMapper;
        this.vehiculoMapper = vehiculoMapper;
        this.vendedorMapper = vendedorMapper;
    }

    // --------- Métodos para Concesionario ---------

    public List<ConcesionarioDTO> findAllConcesionarios() {
        try {
            List<Concesionario> lista = concesionarioRepository.findAll();
            return lista.stream().map(concesionarioMapper::toDTO).toList();
        } catch (Exception e) {
            throw new ResourceNotFoundException("Error al buscar todos los concesionarios");
        }
    }

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
            if (concesionarioRepository.existsByRuc(dto.getRuc())) {
                throw new CreateEntityException("Concesionario", "Ya existe un concesionario con el mismo RUC: " + dto.getRuc());
            }
            if (concesionarioRepository.existsByEmailContacto(dto.getEmailContacto())) {
                throw new CreateEntityException("Concesionario", "Ya existe un concesionario con el mismo email de contacto: " + dto.getEmailContacto());
            }
            if (concesionarioRepository.existsByTelefono(dto.getTelefono())) {
                throw new CreateEntityException("Concesionario", "Ya existe un concesionario con el mismo teléfono: " + dto.getTelefono());
            }
            Concesionario objeto = concesionarioMapper.toModel(dto);
            objeto.setId(null);
            if (objeto.getVersion() == null) {
                objeto.setVersion(0L);
            }
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
            concesionario.setVersion(concesionario.getVersion() == null ? 1L : concesionario.getVersion() + 1);
            Concesionario actualizado = concesionarioRepository.save(concesionario);
            return concesionarioMapper.toDTO(actualizado);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new UpdateEntityException("Concesionario", "Error al actualizar el concesionario. Detalle: " + e.getMessage());
        }
    }

    // --------- Métodos para Vehículo ---------

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

    public List<VehiculoDTO> findVehiculosByRuc(String ruc) {
        Concesionario concesionario = concesionarioRepository.findByRuc(ruc)
            .orElseThrow(() -> new ResourceNotFoundException("Concesionario no encontrado con RUC=" + ruc));
        List<Vehiculo> lista = concesionario.getVehiculos();
        if (lista == null) return List.of();
        return lista.stream().map(vehiculoMapper::toDTO).toList();
    }

    @Transactional
    public VehiculoDTO createVehiculoInConcesionario(String ruc, VehiculoDTO dto) {
        System.out.println("Intentando crear vehículo en concesionario con RUC: " + ruc);
        System.out.println("Datos del vehículo: " + dto.toString());
        
        Concesionario concesionario = concesionarioRepository.findByRuc(ruc)
            .orElseThrow(() -> new ResourceNotFoundException("Concesionario no encontrado con RUC=" + ruc));
        
        System.out.println("Concesionario encontrado: " + concesionario.getRazonSocial());
        
        // Comentar temporalmente la validación de unicidad de placa
        // if (identificadorVehiculoRepository.findByPlaca(dto.getPlaca()) != null) {
        //     throw new CreateEntityException("Vehiculo", "Ya existe un vehículo con la misma placa: " + dto.getPlaca());
        // }
        
        System.out.println("Procediendo a crear vehículo...");
        
        Vehiculo vehiculo = vehiculoMapper.toModel(dto);
        vehiculo.setId(java.util.UUID.randomUUID().toString());
        if (vehiculo.getVersion() == null) vehiculo.setVersion(0L);
        
        System.out.println("Vehículo mapeado, agregando al concesionario...");
        
        if (concesionario.getVehiculos() == null) concesionario.setVehiculos(new java.util.ArrayList<>());
        concesionario.getVehiculos().add(vehiculo);
        
        System.out.println("Guardando concesionario...");
        concesionarioRepository.save(concesionario);
        
        System.out.println("Vehículo creado exitosamente con ID: " + vehiculo.getId());
        return vehiculoMapper.toDTO(vehiculo);
    }

    @Transactional
    public VehiculoDTO updateVehiculoInConcesionario(String ruc, String idVehiculo, VehiculoDTO dto) {
        Concesionario concesionario = concesionarioRepository.findByRuc(ruc)
            .orElseThrow(() -> new ResourceNotFoundException("Concesionario no encontrado con RUC=" + ruc));
        List<Vehiculo> vehiculos = concesionario.getVehiculos();
        if (vehiculos == null) throw new ResourceNotFoundException("No hay vehículos en el concesionario");
        Vehiculo vehiculo = vehiculos.stream().filter(v -> v.getId().equals(idVehiculo)).findFirst()
            .orElseThrow(() -> new ResourceNotFoundException("Vehículo no encontrado con id=" + idVehiculo));
        vehiculo.setMarca(dto.getMarca());
        vehiculo.setModelo(dto.getModelo());
        vehiculo.setAnio(dto.getAnio());
        vehiculo.setValor(dto.getValor());
        vehiculo.setColor(dto.getColor());
        vehiculo.setExtras(dto.getExtras());
        vehiculo.setEstado(dto.getEstado());
        vehiculo.setTipo(dto.getTipo());
        vehiculo.setCombustible(dto.getCombustible());
        vehiculo.setVersion(vehiculo.getVersion() == null ? 1L : vehiculo.getVersion() + 1);
        concesionarioRepository.save(concesionario);
        return vehiculoMapper.toDTO(vehiculo);
    }

    @Transactional
    public VehiculoDTO desactivarVehiculoInConcesionario(String ruc, String idVehiculo) {
        Concesionario concesionario = concesionarioRepository.findByRuc(ruc)
            .orElseThrow(() -> new ResourceNotFoundException("Concesionario no encontrado con RUC=" + ruc));
        List<Vehiculo> vehiculos = concesionario.getVehiculos();
        if (vehiculos == null) throw new ResourceNotFoundException("No hay vehículos en el concesionario");
        Vehiculo vehiculo = vehiculos.stream().filter(v -> v.getId().equals(idVehiculo)).findFirst()
            .orElseThrow(() -> new ResourceNotFoundException("Vehículo no encontrado con id=" + idVehiculo));
        vehiculo.setEstado(com.banquito.gestion_vehiculos.enums.EstadoVehiculoEnum.INACTIVO);
        vehiculo.setVersion(vehiculo.getVersion() == null ? 1L : vehiculo.getVersion() + 1);
        concesionarioRepository.save(concesionario);
        return vehiculoMapper.toDTO(vehiculo);
    }

    public VehiculoDTO findVehiculoByPlacaInConcesionario(String ruc, String placa) {
        Concesionario concesionario = concesionarioRepository.findByRuc(ruc)
            .orElseThrow(() -> new ResourceNotFoundException("Concesionario no encontrado con RUC=" + ruc));
        List<Vehiculo> vehiculos = concesionario.getVehiculos();
        if (vehiculos == null) throw new ResourceNotFoundException("No hay vehículos en el concesionario");
        Vehiculo vehiculo = vehiculos.stream()
            .filter(v -> v.getPlaca() != null && v.getPlaca().equalsIgnoreCase(placa))
            .findFirst()
            .orElseThrow(() -> new ResourceNotFoundException("Vehículo no encontrado con placa=" + placa));
        return vehiculoMapper.toDTO(vehiculo);
    }

    public List<VendedorDTO> findVendedoresByRuc(String ruc) {
        Concesionario concesionario = concesionarioRepository.findByRuc(ruc)
            .orElseThrow(() -> new ResourceNotFoundException("Concesionario no encontrado con RUC=" + ruc));
        if (concesionario.getVendedores() == null) return List.of();
        return concesionario.getVendedores().stream().map(vendedorMapper::toDTO).toList();
    }

    @Transactional
    public VendedorDTO createVendedorInConcesionario(String ruc, VendedorDTO dto) {
        Concesionario concesionario = concesionarioRepository.findByRuc(ruc)
            .orElseThrow(() -> new ResourceNotFoundException("Concesionario no encontrado con RUC=" + ruc));
        if (concesionario.getVendedores() == null) concesionario.setVendedores(new java.util.ArrayList<>());
        // Validación de unicidad de cédula
        for (Concesionario c : concesionarioRepository.findAll()) {
            if (c.getVendedores() != null) {
                boolean cedulaExistente = c.getVendedores().stream().anyMatch(v -> v.getCedula().equals(dto.getCedula()));
                if (cedulaExistente) {
                    throw new CreateEntityException("Vendedor", "Ya existe un vendedor con la misma cédula: " + dto.getCedula());
                }
            }
        }
        Vendedor vendedor = vendedorMapper.toModel(dto);
        vendedor.setId(java.util.UUID.randomUUID().toString());
        if (vendedor.getVersion() == null) vendedor.setVersion(0L);
        concesionario.getVendedores().add(vendedor);
        concesionarioRepository.save(concesionario);
        return vendedorMapper.toDTO(vendedor);
    }

    @Transactional
    public VendedorDTO updateVendedorInConcesionario(String ruc, String idVendedor, VendedorDTO dto) {
        Concesionario concesionario = concesionarioRepository.findByRuc(ruc)
            .orElseThrow(() -> new ResourceNotFoundException("Concesionario no encontrado con RUC=" + ruc));
        if (concesionario.getVendedores() == null) throw new ResourceNotFoundException("No hay vendedores en el concesionario");
        Vendedor vendedor = concesionario.getVendedores().stream()
            .filter(v -> v.getId().equals(idVendedor)).findFirst()
            .orElseThrow(() -> new ResourceNotFoundException("Vendedor no encontrado con id=" + idVendedor));
        vendedor.setNombre(dto.getNombre());
        vendedor.setTelefono(dto.getTelefono());
        vendedor.setEmail(dto.getEmail());
        vendedor.setEstado(dto.getEstado());
        vendedor.setVersion(vendedor.getVersion() == null ? 1L : vendedor.getVersion() + 1);
        concesionarioRepository.save(concesionario);
        return vendedorMapper.toDTO(vendedor);
    }

    @Transactional
    public VendedorDTO desactivarVendedorInConcesionario(String ruc, String idVendedor) {
        Concesionario concesionario = concesionarioRepository.findByRuc(ruc)
            .orElseThrow(() -> new ResourceNotFoundException("Concesionario no encontrado con RUC=" + ruc));
        if (concesionario.getVendedores() == null) throw new ResourceNotFoundException("No hay vendedores en el concesionario");
        Vendedor vendedor = concesionario.getVendedores().stream()
            .filter(v -> v.getId().equals(idVendedor)).findFirst()
            .orElseThrow(() -> new ResourceNotFoundException("Vendedor no encontrado con id=" + idVendedor));
        vendedor.setEstado(com.banquito.gestion_vehiculos.enums.EstadoVendedorEnum.INACTIVO);
        vendedor.setVersion(vendedor.getVersion() == null ? 1L : vendedor.getVersion() + 1);
        concesionarioRepository.save(concesionario);
        return vendedorMapper.toDTO(vendedor);
    }

    @Transactional
    public VendedorDTO updateVendedorInConcesionarioByCedula(String ruc, String cedula, VendedorDTO dto) {
        Concesionario concesionario = concesionarioRepository.findByRuc(ruc)
            .orElseThrow(() -> new ResourceNotFoundException("Concesionario no encontrado con RUC=" + ruc));
        List<Vendedor> vendedores = concesionario.getVendedores();
        if (vendedores == null) throw new ResourceNotFoundException("No hay vendedores en el concesionario");
        Vendedor vendedor = vendedores.stream().filter(v -> v.getCedula().equals(cedula)).findFirst()
            .orElseThrow(() -> new ResourceNotFoundException("Vendedor no encontrado con cédula=" + cedula));
        vendedor.setNombre(dto.getNombre());
        vendedor.setTelefono(dto.getTelefono());
        vendedor.setEmail(dto.getEmail());
        vendedor.setEstado(dto.getEstado());
        vendedor.setVersion(vendedor.getVersion() == null ? 1L : vendedor.getVersion() + 1);
        concesionarioRepository.save(concesionario);
        return vendedorMapper.toDTO(vendedor);
    }

    @Transactional
    public VendedorDTO desactivarVendedorInConcesionarioByCedula(String ruc, String cedula) {
        Concesionario concesionario = concesionarioRepository.findByRuc(ruc)
            .orElseThrow(() -> new ResourceNotFoundException("Concesionario no encontrado con RUC=" + ruc));
        List<Vendedor> vendedores = concesionario.getVendedores();
        if (vendedores == null) throw new ResourceNotFoundException("No hay vendedores en el concesionario");
        Vendedor vendedor = vendedores.stream().filter(v -> v.getCedula().equals(cedula)).findFirst()
            .orElseThrow(() -> new ResourceNotFoundException("Vendedor no encontrado con cédula=" + cedula));
        vendedor.setEstado(com.banquito.gestion_vehiculos.enums.EstadoVendedorEnum.INACTIVO);
        vendedor.setVersion(vendedor.getVersion() == null ? 1L : vendedor.getVersion() + 1);
        concesionarioRepository.save(concesionario);
        return vendedorMapper.toDTO(vendedor);
    }

    public VendedorDTO findVendedorByCedulaInConcesionario(String ruc, String cedula) {
        Concesionario concesionario = concesionarioRepository.findByRuc(ruc)
            .orElseThrow(() -> new ResourceNotFoundException("Concesionario no encontrado con RUC=" + ruc));
        List<Vendedor> vendedores = concesionario.getVendedores();
        if (vendedores == null) throw new ResourceNotFoundException("No hay vendedores en el concesionario");
        Vendedor vendedor = vendedores.stream().filter(v -> v.getCedula().equals(cedula)).findFirst()
            .orElseThrow(() -> new ResourceNotFoundException("Vendedor no encontrado con cédula=" + cedula));
        return vendedorMapper.toDTO(vendedor);
    }

    public List<VendedorDTO> findVendedoresByEstadoInConcesionario(String ruc, String estado) {
        Concesionario concesionario = concesionarioRepository.findByRuc(ruc)
            .orElseThrow(() -> new ResourceNotFoundException("Concesionario no encontrado con RUC=" + ruc));
        List<Vendedor> vendedores = concesionario.getVendedores();
        if (vendedores == null) return List.of();
        return vendedores.stream()
            .filter(v -> v.getEstado() != null && v.getEstado().name().equalsIgnoreCase(estado))
            .map(vendedorMapper::toDTO)
            .toList();
    }

    public VendedorDTO findVendedorByEmailInConcesionario(String ruc, String email) {
        Concesionario concesionario = concesionarioRepository.findByRuc(ruc)
            .orElseThrow(() -> new ResourceNotFoundException("Concesionario no encontrado con RUC=" + ruc));
        List<Vendedor> vendedores = concesionario.getVendedores();
        if (vendedores == null) throw new ResourceNotFoundException("No hay vendedores en el concesionario");
        Vendedor vendedor = vendedores.stream().filter(v -> v.getEmail().equalsIgnoreCase(email)).findFirst()
            .orElseThrow(() -> new ResourceNotFoundException("Vendedor no encontrado con email=" + email));
        return vendedorMapper.toDTO(vendedor);
    }

    @Transactional
    public VehiculoDTO updateVehiculoInConcesionarioByPlaca(String ruc, String placa, VehiculoDTO dto) {
        Concesionario concesionario = concesionarioRepository.findByRuc(ruc)
            .orElseThrow(() -> new ResourceNotFoundException("Concesionario no encontrado con RUC=" + ruc));
        List<Vehiculo> vehiculos = concesionario.getVehiculos();
        if (vehiculos == null) throw new ResourceNotFoundException("No hay vehículos en el concesionario");
        Vehiculo vehiculo = vehiculos.stream().filter(v -> v.getPlaca() != null && v.getPlaca().equalsIgnoreCase(placa)).findFirst()
            .orElseThrow(() -> new ResourceNotFoundException("Vehículo no encontrado con placa=" + placa));
        vehiculo.setMarca(dto.getMarca());
        vehiculo.setModelo(dto.getModelo());
        vehiculo.setCilindraje(dto.getCilindraje());
        vehiculo.setAnio(dto.getAnio());
        vehiculo.setValor(dto.getValor());
        vehiculo.setColor(dto.getColor());
        vehiculo.setExtras(dto.getExtras());
        vehiculo.setEstado(dto.getEstado());
        vehiculo.setTipo(dto.getTipo());
        vehiculo.setCombustible(dto.getCombustible());
        vehiculo.setCondicion(dto.getCondicion());
        vehiculo.setVersion(vehiculo.getVersion() == null ? 1L : vehiculo.getVersion() + 1);
        concesionarioRepository.save(concesionario);
        return vehiculoMapper.toDTO(vehiculo);
    }

    @Transactional
    public VehiculoDTO desactivarVehiculoInConcesionarioByPlaca(String ruc, String placa) {
        Concesionario concesionario = concesionarioRepository.findByRuc(ruc)
            .orElseThrow(() -> new ResourceNotFoundException("Concesionario no encontrado con RUC=" + ruc));
        List<Vehiculo> vehiculos = concesionario.getVehiculos();
        if (vehiculos == null) throw new ResourceNotFoundException("No hay vehículos en el concesionario");
        Vehiculo vehiculo = vehiculos.stream().filter(v -> v.getPlaca() != null && v.getPlaca().equalsIgnoreCase(placa)).findFirst()
            .orElseThrow(() -> new ResourceNotFoundException("Vehículo no encontrado con placa=" + placa));
        vehiculo.setEstado(com.banquito.gestion_vehiculos.enums.EstadoVehiculoEnum.NO_DISPONIBLE);
        vehiculo.setVersion(vehiculo.getVersion() == null ? 1L : vehiculo.getVersion() + 1);
        concesionarioRepository.save(concesionario);
        return vehiculoMapper.toDTO(vehiculo);
    }

    public List<VehiculoDTO> findVehiculosByEstadoInConcesionario(String ruc, String estado) {
        Concesionario concesionario = concesionarioRepository.findByRuc(ruc)
            .orElseThrow(() -> new ResourceNotFoundException("Concesionario no encontrado con RUC=" + ruc));
        List<Vehiculo> vehiculos = concesionario.getVehiculos();
        if (vehiculos == null) return List.of();
        return vehiculos.stream()
            .filter(v -> v.getEstado() != null && v.getEstado().name().equalsIgnoreCase(estado))
            .map(vehiculoMapper::toDTO)
            .toList();
    }

    public List<VehiculoDTO> findVehiculosByCondicionInConcesionario(String ruc, String condicion) {
        Concesionario concesionario = concesionarioRepository.findByRuc(ruc)
            .orElseThrow(() -> new ResourceNotFoundException("Concesionario no encontrado con RUC=" + ruc));
        List<Vehiculo> vehiculos = concesionario.getVehiculos();
        if (vehiculos == null) return List.of();
        return vehiculos.stream()
            .filter(v -> v.getCondicion() != null && v.getCondicion().name().equalsIgnoreCase(condicion))
            .map(vehiculoMapper::toDTO)
            .toList();
    }

    // --------- Métodos para Admin (todos los datos) ---------

    public List<VendedorDTO> findAllVendedores() {
        try {
            List<Concesionario> concesionarios = concesionarioRepository.findAll();
            List<VendedorDTO> todosVendedores = new ArrayList<>();
            
            for (Concesionario concesionario : concesionarios) {
                if (concesionario.getVendedores() != null) {
                    List<VendedorDTO> vendedoresConcesionario = concesionario.getVendedores()
                        .stream()
                        .map(vendedorMapper::toDTO)
                        .toList();
                    todosVendedores.addAll(vendedoresConcesionario);
                }
            }
            
            return todosVendedores;
        } catch (Exception e) {
            throw new ResourceNotFoundException("Error al buscar todos los vendedores");
        }
    }

    public List<VehiculoDTO> findAllVehiculos() {
        try {
            List<Concesionario> concesionarios = concesionarioRepository.findAll();
            List<VehiculoDTO> todosVehiculos = new ArrayList<>();
            
            for (Concesionario concesionario : concesionarios) {
                if (concesionario.getVehiculos() != null) {
                    List<VehiculoDTO> vehiculosConcesionario = concesionario.getVehiculos()
                        .stream()
                        .map(vehiculoMapper::toDTO)
                        .toList();
                    todosVehiculos.addAll(vehiculosConcesionario);
                }
            }
            
            return todosVehiculos;
        } catch (Exception e) {
            throw new ResourceNotFoundException("Error al buscar todos los vehículos");
        }
    }

    public ConcesionarioDTO findConcesionarioByVendedorEmail(String email) {
        try {
            List<Concesionario> concesionarios = concesionarioRepository.findAll();
            
            for (Concesionario concesionario : concesionarios) {
                if (concesionario.getVendedores() != null) {
                    boolean vendedorEncontrado = concesionario.getVendedores().stream()
                        .anyMatch(vendedor -> email.equalsIgnoreCase(vendedor.getEmail()));
                    
                    if (vendedorEncontrado) {
                        return concesionarioMapper.toDTO(concesionario);
                    }
                }
            }
            
            throw new ResourceNotFoundException("No se encontró concesionario para el vendedor con email: " + email);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ResourceNotFoundException("Error al buscar concesionario por email de vendedor: " + email);
        }
    }
}

