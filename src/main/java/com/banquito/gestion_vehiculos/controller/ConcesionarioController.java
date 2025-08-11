package com.banquito.gestion_vehiculos.controller;

import com.banquito.gestion_vehiculos.dto.ConcesionarioDTO;
import com.banquito.gestion_vehiculos.dto.VendedorDTO;
import com.banquito.gestion_vehiculos.dto.VehiculoDTO;
import com.banquito.gestion_vehiculos.enums.EstadoConcesionarioEnum;
import com.banquito.gestion_vehiculos.service.ConcesionarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import com.banquito.gestion_vehiculos.repository.IdentificadorVehiculoRepository;
import com.banquito.gestion_vehiculos.model.IdentificadorVehiculo;
import com.banquito.gestion_vehiculos.dto.IdentificadorVehiculoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import com.banquito.gestion_vehiculos.model.Vehiculo;
import com.banquito.gestion_vehiculos.mapper.IdentificadorVehiculoMapper;
import com.banquito.gestion_vehiculos.repository.VendedorRepository;
import com.banquito.gestion_vehiculos.model.Vendedor;
import com.banquito.gestion_vehiculos.mapper.VendedorMapper;
import com.banquito.gestion_vehiculos.model.Concesionario;
import com.banquito.gestion_vehiculos.repository.ConcesionarioRepository;
import com.banquito.gestion_vehiculos.exception.ResourceNotFoundException;

@Tag(name = "Concesionarios", description = "Operaciones relacionadas con concesionarios, vendedores y vehículos")
@RestController
@RequestMapping("/api/concesionarios/v1")
public class ConcesionarioController {

    private final ConcesionarioService concesionarioService;
    @Autowired
    private IdentificadorVehiculoRepository identificadorVehiculoRepository;
    @Autowired
    private IdentificadorVehiculoMapper identificadorVehiculoMapper;
    @Autowired
    private VendedorRepository vendedorRepository;
    @Autowired
    private VendedorMapper vendedorMapper;
    @Autowired
    private ConcesionarioRepository concesionarioRepository;

    public ConcesionarioController(ConcesionarioService concesionarioService) {
        this.concesionarioService = concesionarioService;
    }

    @Operation(summary = "Listar todos los concesionarios", description = "Obtiene todos los concesionarios (solo admin)")
    @GetMapping
    public ResponseEntity<List<ConcesionarioDTO>> getAllConcesionarios() {
        return ResponseEntity.ok(concesionarioService.findAllConcesionarios());
    }

    @Operation(summary = "Buscar concesionario por RUC", description = "Obtiene un concesionario usando su RUC")
    @GetMapping("/ruc/{ruc}")
    public ResponseEntity<ConcesionarioDTO> getByRuc(@PathVariable String ruc) {
        return ResponseEntity.ok(concesionarioService.findConcesionarioByRuc(ruc));
    }

    @Operation(summary = "Buscar concesionarios por estado", description = "Obtiene una lista de concesionarios filtrados por estado")
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<ConcesionarioDTO>> getByEstado(@PathVariable EstadoConcesionarioEnum estado) {
        return ResponseEntity.ok(concesionarioService.findConcesionariosByEstado(estado));
    }

    @Operation(summary = "Buscar concesionarios por razón social", description = "Obtiene una lista de concesionarios filtrados por razón social")
    @GetMapping("/razon-social/{razonSocial}")
    public ResponseEntity<List<ConcesionarioDTO>> getByRazonSocial(@PathVariable String razonSocial) {
        return ResponseEntity.ok(concesionarioService.findConcesionariosByRazonSocial(razonSocial));
    }

    @Operation(summary = "Buscar concesionario por email de contacto", description = "Obtiene un concesionario usando su email de contacto")
    @GetMapping("/email/{emailContacto}")
    public ResponseEntity<ConcesionarioDTO> getByEmail(@PathVariable String emailContacto) {
        return ResponseEntity.ok(concesionarioService.findConcesionarioByEmail(emailContacto));
    }

    @Operation(summary = "Crear un nuevo concesionario", description = "Crea un nuevo concesionario con los datos proporcionados")
    @PostMapping
    public ResponseEntity<ConcesionarioDTO> create(@Valid @RequestBody ConcesionarioDTO dto) {
        return ResponseEntity.ok(concesionarioService.createConcesionario(dto));
    }

    @Operation(summary = "Actualizar concesionario por RUC", description = "Actualiza los datos de un concesionario usando su RUC")
    @PutMapping("/ruc/{ruc}")
    public ResponseEntity<ConcesionarioDTO> update(@PathVariable String ruc, @Valid @RequestBody ConcesionarioDTO dto) {
        return ResponseEntity.ok(concesionarioService.updateConcesionario(ruc, dto));
    }

    @Operation(summary = "Desactivar concesionario por RUC", description = "Desactiva un concesionario usando su RUC")
    @PutMapping("/ruc/{ruc}/desactivar")
    public ResponseEntity<ConcesionarioDTO> desactivar(@PathVariable String ruc) {
        return ResponseEntity.ok(concesionarioService.desactivateConcesionario(ruc));
    }

    @Operation(summary = "Listar vendedores por RUC de concesionario", description = "Obtiene los vendedores asociados a un concesionario")
    @GetMapping("/ruc/{ruc}/vendedores")
    public ResponseEntity<List<VendedorDTO>> getVendedoresByRuc(@PathVariable String ruc) {
        return ResponseEntity.ok(concesionarioService.findVendedoresByRuc(ruc));
    }

    @Operation(summary = "Listar vehículos por RUC de concesionario", description = "Obtiene los vehículos asociados a un concesionario")
    @GetMapping("/ruc/{ruc}/vehiculos")
    public ResponseEntity<List<VehiculoDTO>> getVehiculosByRuc(@PathVariable String ruc) {
        return ResponseEntity.ok(concesionarioService.findVehiculosByRuc(ruc));
    }

    @Operation(summary = "Crear vendedor en concesionario", description = "Crea un nuevo vendedor en un concesionario específico")
    @PostMapping("/ruc/{ruc}/vendedores")
    public ResponseEntity<VendedorDTO> createVendedor(@PathVariable String ruc, @Valid @RequestBody VendedorDTO dto) {
        return ResponseEntity.ok(concesionarioService.createVendedorInConcesionario(ruc, dto));
    }

    @Operation(summary = "Actualizar vendedor por cédula", description = "Actualiza los datos de un vendedor en un concesionario usando la cédula")
    @PutMapping("/ruc/{ruc}/vendedores/{cedula}")
    public ResponseEntity<VendedorDTO> updateVendedor(@PathVariable String ruc, @PathVariable String cedula, @Valid @RequestBody VendedorDTO dto) {
        return ResponseEntity.ok(concesionarioService.updateVendedorInConcesionarioByCedula(ruc, cedula, dto));
    }

    @Operation(summary = "Desactivar vendedor por cédula", description = "Desactiva un vendedor en un concesionario usando la cédula")
    @PutMapping("/ruc/{ruc}/vendedores/{cedula}/desactivar")
    public ResponseEntity<VendedorDTO> desactivarVendedor(@PathVariable String ruc, @PathVariable String cedula) {
        return ResponseEntity.ok(concesionarioService.desactivarVendedorInConcesionarioByCedula(ruc, cedula));
    }

    @Operation(summary = "Buscar vendedor por cédula", description = "Obtiene un vendedor en un concesionario usando la cédula")
    @GetMapping("/ruc/{ruc}/vendedores/cedula/{cedula}")
    public ResponseEntity<VendedorDTO> getVendedorByCedula(@PathVariable String ruc, @PathVariable String cedula) {
        return ResponseEntity.ok(concesionarioService.findVendedorByCedulaInConcesionario(ruc, cedula));
    }

    @Operation(summary = "Listar vendedores por estado", description = "Obtiene los vendedores de un concesionario filtrados por estado")
    @GetMapping("/ruc/{ruc}/vendedores/estado/{estado}")
    public ResponseEntity<List<VendedorDTO>> getVendedoresByEstado(@PathVariable String ruc, @PathVariable String estado) {
        return ResponseEntity.ok(concesionarioService.findVendedoresByEstadoInConcesionario(ruc, estado));
    }

    @Operation(summary = "Buscar vendedor por email", description = "Obtiene un vendedor en un concesionario usando el email")
    @GetMapping("/ruc/{ruc}/vendedores/email/{email}")
    public ResponseEntity<VendedorDTO> getVendedorByEmail(@PathVariable String ruc, @PathVariable String email) {
        return ResponseEntity.ok(concesionarioService.findVendedorByEmailInConcesionario(ruc, email));
    }

    @Operation(summary = "Crear vehículo en concesionario", description = "Crea un nuevo vehículo en un concesionario específico")
    @PostMapping("/ruc/{ruc}/vehiculos")
    public ResponseEntity<VehiculoDTO> createVehiculo(@PathVariable String ruc, @Valid @RequestBody VehiculoDTO dto) {
        System.out.println("Recibiendo petición para crear vehículo en concesionario RUC: " + ruc);
        System.out.println("Datos recibidos: " + dto.toString());
        return ResponseEntity.ok(concesionarioService.createVehiculoInConcesionario(ruc, dto));
    }

    @Operation(summary = "Actualizar vehículo por placa", description = "Actualiza los datos de un vehículo en un concesionario usando la placa")
    @PutMapping("/ruc/{ruc}/vehiculos/{placa}")
    public ResponseEntity<VehiculoDTO> updateVehiculo(@PathVariable String ruc, @PathVariable String placa, @Valid @RequestBody VehiculoDTO dto) {
        return ResponseEntity.ok(concesionarioService.updateVehiculoInConcesionarioByPlaca(ruc, placa, dto));
    }

    @Operation(summary = "Desactivar vehículo por placa", description = "Desactiva un vehículo en un concesionario usando la placa")
    @PutMapping("/ruc/{ruc}/vehiculos/{placa}/desactivar")
    public ResponseEntity<VehiculoDTO> desactivarVehiculo(@PathVariable String ruc, @PathVariable String placa) {
        return ResponseEntity.ok(concesionarioService.desactivarVehiculoInConcesionarioByPlaca(ruc, placa));
    }

    @Operation(summary = "Listar vehículos por estado", description = "Obtiene los vehículos de un concesionario filtrados por estado")
    @GetMapping("/ruc/{ruc}/vehiculos/estado/{estado}")
    public ResponseEntity<List<VehiculoDTO>> getVehiculosByEstado(@PathVariable String ruc, @PathVariable String estado) {
        return ResponseEntity.ok(concesionarioService.findVehiculosByEstadoInConcesionario(ruc, estado));
    }

    @Operation(summary = "Listar vehículos por condición", description = "Obtiene los vehículos de un concesionario filtrados por condición")
    @GetMapping("/ruc/{ruc}/vehiculos/condicion/{condicion}")
    public ResponseEntity<List<VehiculoDTO>> getVehiculosByCondicion(@PathVariable String ruc, @PathVariable String condicion) {
        return ResponseEntity.ok(concesionarioService.findVehiculosByCondicionInConcesionario(ruc, condicion));
    }

    @Operation(summary = "Buscar vehículo por placa", description = "Obtiene un vehículo en un concesionario usando la placa")
    @GetMapping("/ruc/{ruc}/vehiculos/placa/{placa}")
    public ResponseEntity<VehiculoDTO> getVehiculoByPlaca(@PathVariable String ruc, @PathVariable String placa) {
        return ResponseEntity.ok(concesionarioService.findVehiculoByPlacaInConcesionario(ruc, placa));
    }

    @Operation(summary = "Listar todos los identificadores de vehículos", description = "Obtiene todos los identificadores de vehículos del sistema")
    @GetMapping("/identificadores-vehiculo")
    public ResponseEntity<List<IdentificadorVehiculoDTO>> getAllIdentificadoresVehiculo() {
        List<IdentificadorVehiculo> identificadores = identificadorVehiculoRepository.findAll();
        List<IdentificadorVehiculoDTO> dtos = identificadores.stream()
            .map(identificadorVehiculoMapper::toDTO)
            .toList();
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Crear identificador de vehículo", description = "Crea un identificador de vehículo (placa, chasis, motor)")
    @PostMapping("/identificadores-vehiculo")
    public ResponseEntity<?> createIdentificadorVehiculo(@RequestBody IdentificadorVehiculoDTO dto) {
        // Comentar temporalmente la validación de duplicados para permitir crear vehículos
        // if (identificadorVehiculoRepository.findByPlaca(dto.getPlaca()) != null) {
        //     return ResponseEntity.badRequest().body("Ya existe un identificador con la placa: " + dto.getPlaca());
        // }
        IdentificadorVehiculo identificador = new IdentificadorVehiculo();
        identificador.setPlaca(dto.getPlaca());
        identificador.setChasis(dto.getChasis());
        identificador.setMotor(dto.getMotor());
        IdentificadorVehiculo saved = identificadorVehiculoRepository.save(identificador);
        return ResponseEntity.ok(saved);
    }

    @Operation(summary = "Buscar identificador de vehículo por id", description = "Obtiene un identificador de vehículo usando su id")
    @GetMapping("/identificadores-vehiculo/{id}")
    public ResponseEntity<IdentificadorVehiculoDTO> getIdentificadorVehiculoById(@PathVariable String id) {
        return identificadorVehiculoRepository.findById(id)
            .map(identificador -> ResponseEntity.ok(identificadorVehiculoMapper.toDTO(identificador)))
            .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Listar todos los vendedores del sistema", description = "Obtiene todos los vendedores de todos los concesionarios (solo admin)")
    @GetMapping("/vendedores")
    public ResponseEntity<List<VendedorDTO>> getAllVendedores() {
        return ResponseEntity.ok(concesionarioService.findAllVendedores());
    }

    @Operation(summary = "Listar todos los vehículos del sistema", description = "Obtiene todos los vehículos de todos los concesionarios (solo admin)")
    @GetMapping("/vehiculos")
    public ResponseEntity<List<VehiculoDTO>> getAllVehiculos() {
        return ResponseEntity.ok(concesionarioService.findAllVehiculos());
    }

    @Operation(summary = "Buscar vendedores por nombre en concesionario", description = "Obtiene una lista de vendedores de un concesionario cuyo nombre contiene el texto dado (ignore case)")
    @GetMapping("/ruc/{ruc}/vendedores/nombre/{nombre}")
    public ResponseEntity<List<VendedorDTO>> getVendedoresByNombre(@PathVariable String ruc, @PathVariable String nombre) {
        Concesionario concesionario = concesionarioRepository.findByRuc(ruc)
            .orElseThrow(() -> new ResourceNotFoundException("Concesionario no encontrado con RUC=" + ruc));
        if (concesionario.getVendedores() == null) return ResponseEntity.ok(List.of());
        List<VendedorDTO> dtos = concesionario.getVendedores().stream()
            .filter(v -> {
                if (nombre.length() == 1) {
                    return v.getNombre() != null && v.getNombre().toLowerCase().startsWith(nombre.toLowerCase());
                } else {
                    return v.getNombre() != null && v.getNombre().toLowerCase().contains(nombre.toLowerCase());
                }
            })
            .map(vendedorMapper::toDTO)
            .toList();
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Buscar concesionario por email de vendedor", description = "Obtiene el concesionario al que pertenece un vendedor usando su email")
    @GetMapping("/vendedor-email/{email}")
    public ResponseEntity<ConcesionarioDTO> getConcesionarioByVendedorEmail(@PathVariable String email) {
        return ResponseEntity.ok(concesionarioService.findConcesionarioByVendedorEmail(email));
    }
} 