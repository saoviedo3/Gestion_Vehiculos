package com.banquito.gestion_vehiculos.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banquito.gestion_vehiculos.dto.IdentificadorVehiculoDTO;
import com.banquito.gestion_vehiculos.dto.VehiculoDTO;
import com.banquito.gestion_vehiculos.enums.EstadoVehiculoEnum;
import com.banquito.gestion_vehiculos.mapper.IdentificadorVehiculoMapper;
import com.banquito.gestion_vehiculos.mapper.VehiculoMapper;
import com.banquito.gestion_vehiculos.exception.CreateEntityException;
import com.banquito.gestion_vehiculos.exception.ResourceNotFoundException;
import com.banquito.gestion_vehiculos.exception.UpdateEntityException;
import com.banquito.gestion_vehiculos.model.Concesionario;
import com.banquito.gestion_vehiculos.model.IdentificadorVehiculo;
import com.banquito.gestion_vehiculos.model.Vehiculo;
import com.banquito.gestion_vehiculos.repository.ConcesionarioRepository;
import com.banquito.gestion_vehiculos.repository.IdentificadorVehiculoRepository;
import com.banquito.gestion_vehiculos.repository.VehiculoRepository;

@Service
public class VehiculoService {

    private final ConcesionarioRepository concesionarioRepository;
    private final IdentificadorVehiculoRepository identificadorRepository;
    private final VehiculoRepository vehiculoRepository;
    private final VehiculoMapper vehiculoMapper;
    private final IdentificadorVehiculoMapper identificadorVehiculoMapper;

    public VehiculoService(
            ConcesionarioRepository concesionarioRepository,
            IdentificadorVehiculoRepository identificadorRepository,
            VehiculoRepository vehiculoRepository,
            VehiculoMapper vehiculoMapper,
            IdentificadorVehiculoMapper identificadorVehiculoMapper) {
        this.concesionarioRepository = concesionarioRepository;
        this.identificadorRepository = identificadorRepository;
        this.vehiculoRepository = vehiculoRepository;
        this.vehiculoMapper = vehiculoMapper;
        this.identificadorVehiculoMapper = identificadorVehiculoMapper;
    }

    // -------- Métodos para IdentificadorVehiculo --------

    public IdentificadorVehiculoDTO getIdentificadorById(String id) {
        try {
            IdentificadorVehiculo identificador = identificadorRepository.findById(id)
                    .orElseThrow(
                            () -> new ResourceNotFoundException("IdentificadorVehiculo no encontrado con id=" + id));
            return identificadorVehiculoMapper.toDTO(identificador);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ResourceNotFoundException("Error al buscar el identificador: " + id);
        }
    }

    @Transactional
    public IdentificadorVehiculoDTO createIdentificador(IdentificadorVehiculoDTO dto) {
        try {
            if (identificadorRepository.existsByVin(dto.getVin()))
                throw new CreateEntityException("IdentificadorVehiculo",
                        "Ya existe un identificador con el mismo VIN: " + dto.getVin());
            if (identificadorRepository.existsByNumeroMotor(dto.getNumeroMotor()))
                throw new CreateEntityException("IdentificadorVehiculo",
                        "Ya existe un identificador con el mismo número de motor: " + dto.getNumeroMotor());
            if (identificadorRepository.existsByPlaca(dto.getPlaca()))
                throw new CreateEntityException("IdentificadorVehiculo",
                        "Ya existe un identificador con la misma placa: " + dto.getPlaca());
            IdentificadorVehiculo identificador = identificadorVehiculoMapper.toModel(dto);
            identificador.setId(null);
            IdentificadorVehiculo identificadorGuardado = identificadorRepository.save(identificador);
            return identificadorVehiculoMapper.toDTO(identificadorGuardado);
        } catch (CreateEntityException e) {
            throw e;
        } catch (Exception e) {
            throw new CreateEntityException("IdentificadorVehiculo",
                    "Error al crear el identificador. Detalle: " + e.getMessage());
        }
    }

    // -------- Métodos para Vehiculo --------

    public VehiculoDTO findVehiculoByPlaca(String placa) {
        try {
            IdentificadorVehiculo identificador = identificadorRepository.findByPlaca(placa)
                .orElseThrow(() -> new ResourceNotFoundException("IdentificadorVehiculo no encontrado con placa=" + placa));
            Vehiculo vehiculo = vehiculoRepository.findByIdentificadorVehiculo_Id(identificador.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehículo no encontrado con identificador de placa=" + placa));
            return vehiculoMapper.toDTO(vehiculo);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ResourceNotFoundException("Error al buscar vehículo por placa: " + placa);
        }
    }

    public List<VehiculoDTO> findVehiculosByEstado(EstadoVehiculoEnum estado) {
        try {
            List<Vehiculo> vehiculos = vehiculoRepository.findByEstado(estado);
            return vehiculoMapper.toDTOList(vehiculos);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Error al buscar vehículos por estado: " + estado);
        }
    }

    public List<VehiculoDTO> findVehiculosByConcesionario(String idConcesionario) {
        try {
            Concesionario concesionario = concesionarioRepository.findById(idConcesionario)
                .orElseThrow(() -> new ResourceNotFoundException("Concesionario no encontrado con id=" + idConcesionario));
            if (concesionario.getVehiculos() == null) return List.of();
            return vehiculoMapper.toDTOList(concesionario.getVehiculos());
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ResourceNotFoundException("Error al buscar vehículos del concesionario: " + idConcesionario);
        }
    }

    @Transactional
    public VehiculoDTO createVehiculo(VehiculoDTO dto) {
        try {
            String idIdentificador = (dto.getIdentificadorVehiculo() != null) ? dto.getIdentificadorVehiculo().getId() : null;
            if (idIdentificador == null || !identificadorRepository.existsById(idIdentificador)) {
                throw new CreateEntityException("Vehiculo", "IdentificadorVehiculo no existe con id=" + idIdentificador);
            }
            Vehiculo vehiculo = vehiculoMapper.toModel(dto);
            vehiculo.setId(null);
            Vehiculo vehiculoGuardado = vehiculoRepository.save(vehiculo);
            return vehiculoMapper.toDTO(vehiculoGuardado);
        } catch (CreateEntityException e) {
            throw e;
        } catch (Exception e) {
            throw new CreateEntityException("Vehiculo", "Error al crear el vehículo. Detalle: " + e.getMessage());
        }
    }

    @Transactional
    public VehiculoDTO updateVehiculo(String placa, VehiculoDTO dto) {
        try {
            IdentificadorVehiculo identificador = identificadorRepository.findByPlaca(placa)
                .orElseThrow(() -> new ResourceNotFoundException("IdentificadorVehiculo no encontrado con placa=" + placa));
            Vehiculo vehiculo = vehiculoRepository.findByIdentificadorVehiculo_Id(identificador.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehículo no encontrado con identificador de placa=" + placa));
            IdentificadorVehiculo nuevoIdentificador = null;
            if (dto.getIdentificadorVehiculo() != null && dto.getIdentificadorVehiculo().getId() != null) {
                nuevoIdentificador = identificadorRepository.findById(dto.getIdentificadorVehiculo().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("IdentificadorVehiculo no encontrado con id=" + dto.getIdentificadorVehiculo().getId()));
            }
            vehiculoMapper.updateEntity(vehiculo, dto, nuevoIdentificador);
            Vehiculo vehiculoActualizado = vehiculoRepository.save(vehiculo);
            return vehiculoMapper.toDTO(vehiculoActualizado);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new UpdateEntityException("Vehiculo", "Error al actualizar el vehículo. Detalle: " + e.getMessage());
        }
    }
}
