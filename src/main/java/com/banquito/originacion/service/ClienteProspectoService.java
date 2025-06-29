package com.banquito.originacion.service;

import com.banquito.originacion.controller.dto.AuditoriaDTO;
import com.banquito.originacion.controller.dto.ClienteProspectoDTO;
import com.banquito.originacion.controller.mapper.ClienteProspectoMapper;
import com.banquito.originacion.enums.AccionAuditoriaEnum;
import com.banquito.originacion.enums.EstadoClientesEnum;
import com.banquito.originacion.exception.CreateEntityException;
import com.banquito.originacion.exception.ResourceNotFoundException;
import com.banquito.originacion.exception.UpdateEntityException;
import com.banquito.originacion.model.ClienteProspecto;
import com.banquito.originacion.repository.ClienteProspectoRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Validated
public class ClienteProspectoService {
    
    private static final Logger log = LoggerFactory.getLogger(ClienteProspectoService.class);
    private static final BigDecimal PORCENTAJE_MAXIMO_CUOTA_INGRESO = new BigDecimal("0.40");

    // Patrones para validaciones
    private static final String PATRON_CEDULA = "^[0-9]{10}$";

    private final ClienteProspectoRepository clienteRepository;
    private final ClienteProspectoMapper clienteMapper;
    private final AuditoriaService auditoriaService;

    public ClienteProspectoService(ClienteProspectoRepository clienteRepository,
                                 ClienteProspectoMapper clienteMapper,
                                 AuditoriaService auditoriaService) {
        this.clienteRepository = clienteRepository;
        this.clienteMapper = clienteMapper;
        this.auditoriaService = auditoriaService;
    }

    // === REGISTRO Y VALIDACIÓN ===
    
    /**
     * Registra un nuevo cliente/prospecto con todas las validaciones requeridas
     */
    @Transactional
    public ClienteProspectoDTO registrarCliente(@Valid ClienteProspectoDTO clienteDTO) {
        log.info("Registrando nuevo cliente/prospecto con cédula: {}", clienteDTO.getCedula());
        try {
            // 1. Validar formato cédula ecuatoriana
            if (!validarCedulaEcuatoriana(clienteDTO.getCedula())) {
                throw new CreateEntityException("ClienteProspecto", "La cédula no tiene un formato válido ecuatoriano");
            }

            // 2. Validar edad ≥ 18 años
            int edad = calcularEdad(clienteDTO.getCedula());
            if (edad < 18) {
                throw new CreateEntityException("ClienteProspecto", "El cliente debe ser mayor de edad");
            }

            // 3. Validar solo personas naturales
            // Esta validación ya está implícita en el formato de cédula

            // 8. Validar actividad económica obligatoria
            
            // 6. Validar ingresos y egresos
            // Nota: Las validaciones básicas ya están en el DTO, aquí validamos reglas de negocio adicionales
            if (clienteDTO.getEgresos() != null && 
                    clienteDTO.getEgresos().compareTo(clienteDTO.getIngresos()) > 0) {
                throw new CreateEntityException("ClienteProspecto", "Los egresos no pueden superar los ingresos");
            }

            // Consultar si ya existe cliente con la misma cédula
            Optional<ClienteProspecto> existente = clienteRepository.findByCedula(clienteDTO.getCedula());
            if (existente.isPresent()) {
                throw new CreateEntityException("ClienteProspecto", 
                        "Ya existe un cliente con la cédula: " + clienteDTO.getCedula());
            }

            // 10. Verificar lista negra
            if (verificarListaNegra(clienteDTO.getCedula())) {
                throw new CreateEntityException("ClienteProspecto", 
                        "El cliente se encuentra en listas negras y no puede ser registrado");
            }

            // 9. Consultar en Core Bancario
            Map<String, Object> resultadoCore = consultarEnCoreBancarioInterno(clienteDTO.getCedula());
            boolean existeEnCore = (boolean) resultadoCore.getOrDefault("existeEnCore", false);
            
            // 11. Clasificar automáticamente y registrar como prospecto si no existe
            EstadoClientesEnum estadoCliente = EstadoClientesEnum.PROSPECTO;
            if (existeEnCore) {
                // Si existe en Core, verificamos la clasificación
                estadoCliente = determinarEstadoCliente(resultadoCore);
            }
            
            clienteDTO.setEstado(estadoCliente);
            clienteDTO.setVersion(0L);

            // Convertir a entidad y guardar
            ClienteProspecto cliente = clienteMapper.toModel(clienteDTO);
            cliente.setId(null); // Aseguramos que sea registro nuevo
            
            ClienteProspecto guardado = clienteRepository.save(cliente);
            
            // Registrar auditoría
            registrarAuditoria("clientes_prospectos", AccionAuditoriaEnum.INSERT);
            
            return clienteMapper.toDTO(guardado);
        } catch (CreateEntityException e) {
            throw e;
        } catch (Exception e) {
            throw new CreateEntityException("ClienteProspecto", 
                    "Error al registrar cliente: " + e.getMessage());
        }
    }

    /**
     * Valida la información de un cliente sin registrarlo
     */
    public Map<String, Object> validarCliente(String cedula) {
        log.info("Validando información de cliente con cédula: {}", cedula);
        Map<String, Object> resultado = new HashMap<>();
        
        try {
            // Validar formato de cédula
            boolean cedulaValida = validarCedulaEcuatoriana(cedula);
            if (!cedulaValida) {
                resultado.put("cedulaValida", false);
                resultado.put("mensaje", "La cédula no tiene un formato válido ecuatoriano");
                return resultado;
            }
            
            // Validar edad
            int edad = calcularEdad(cedula);
            boolean edadValida = (edad >= 18);
            resultado.put("edadValida", edadValida);
            if (!edadValida) {
                resultado.put("mensaje", "El cliente debe ser mayor de edad");
                return resultado;
            }
            
            // Verificar si ya existe en sistema
            boolean existeEnSistema = clienteRepository.existsByCedula(cedula);
            resultado.put("existeEnSistema", existeEnSistema);
            
            // Verificar lista negra
            boolean enListaNegra = verificarListaNegra(cedula);
            resultado.put("enListaNegra", enListaNegra);
            
            // Consultar Core Bancario
            Map<String, Object> resultadoCore = consultarEnCoreBancarioInterno(cedula);
            boolean existeEnCore = (boolean) resultadoCore.getOrDefault("existeEnCore", false);
            resultado.put("existeEnCore", existeEnCore);
            
            if (existeEnCore) {
                // Añadir información adicional si existe en Core
                resultado.putAll(resultadoCore);
            }
            
            // Resultado final
            boolean validacionCompleta = cedulaValida && edadValida && !enListaNegra;
            resultado.put("esValido", validacionCompleta);
            
            return resultado;
        } catch (Exception e) {
            resultado.put("error", "Error en la validación: " + e.getMessage());
            resultado.put("esValido", false);
            return resultado;
        }
    }

    // === CONSULTAS Y CLASIFICACIÓN ===
    
    /**
     * Consulta información del cliente en Core Bancario
     */
    public Map<String, Object> consultarEnCoreBancario(String cedula) {
        log.info("Consultando cliente {} en Core Bancario", cedula);
        
        // En implementación real, hacer llamada a API de Core Bancario
        return consultarEnCoreBancarioInterno(cedula);
    }

    /**
     * Clasifica al cliente según su historial y relación
     */
    public Map<String, Object> clasificarCliente(String cedula) {
        log.info("Clasificando cliente con cédula: {}", cedula);
        Map<String, Object> resultado = new HashMap<>();
        
        try {
            // Verificar si existe en nuestro sistema
            Optional<ClienteProspecto> optCliente = clienteRepository.findByCedula(cedula);
            if (optCliente.isEmpty()) {
                resultado.put("existe", false);
                resultado.put("clasificacion", "DESCONOCIDO");
                return resultado;
            }
            
            ClienteProspecto cliente = optCliente.get();
            
            // Consultar en Core para datos actualizados
            Map<String, Object> datosCore = consultarEnCoreBancarioInterno(cedula);
            boolean existeEnCore = (boolean) datosCore.getOrDefault("existeEnCore", false);
            
            EstadoClientesEnum estadoActual = cliente.getEstado();
            EstadoClientesEnum nuevoEstado;
            
            if (!existeEnCore) {
                nuevoEstado = EstadoClientesEnum.PROSPECTO;
            } else {
                nuevoEstado = determinarEstadoCliente(datosCore);
                
                // Si el estado ha cambiado, actualizar en BD y auditar
                if (nuevoEstado != estadoActual) {
                    cliente.setEstado(nuevoEstado);
                    clienteRepository.save(cliente);
                    registrarAuditoria("clientes_prospectos", AccionAuditoriaEnum.UPDATE);
                }
            }
            
            resultado.put("existe", true);
            resultado.put("idCliente", cliente.getId());
            resultado.put("clasificacionAnterior", estadoActual);
            resultado.put("clasificacionActual", nuevoEstado);
            resultado.put("nombre", cliente.getNombre() + " " + cliente.getApellido());
            resultado.putAll(datosCore);
            
            return resultado;
        } catch (Exception e) {
            resultado.put("error", "Error al clasificar cliente: " + e.getMessage());
            return resultado;
        }
    }

    /**
     * Verifica si el cliente está en lista negra
     */
    public boolean verificarListaNegra(String cedula) {
        log.info("Verificando cliente {} en listas negras", cedula);
        
        // En implementación real, consultar BD de listas negras o API externa
        // Esta es una simulación para fines de demostración
        return false; // Asumimos que no está en lista negra
    }

    // === GESTIÓN FINANCIERA ===
    
    /**
     * Actualiza la información financiera del cliente
     */
    @Transactional
    public ClienteProspectoDTO actualizarInformacionFinanciera(Integer id, BigDecimal ingresos, BigDecimal egresos) {
        log.info("Actualizando información financiera para cliente con id: {}", id);
        try {
            // Validar cliente existente
            ClienteProspecto cliente = clienteRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id));
            
            // 1. Validar ingresos > 0
            if (ingresos == null || ingresos.compareTo(BigDecimal.ZERO) <= 0) {
                throw new UpdateEntityException("ClienteProspecto", "Los ingresos deben ser mayores a $0");
            }
            
            // 2. Validar egresos ≤ ingresos
            if (egresos == null || egresos.compareTo(ingresos) > 0) {
                throw new UpdateEntityException("ClienteProspecto", "Los egresos no pueden superar los ingresos");
            }
            
            // 3. Actualizar información
            cliente.setIngresos(ingresos);
            cliente.setEgresos(egresos);
            ClienteProspecto actualizado = clienteRepository.save(cliente);
            
            // Registrar auditoría
            registrarAuditoria("clientes_prospectos", AccionAuditoriaEnum.UPDATE);
            
            return clienteMapper.toDTO(actualizado);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (UpdateEntityException e) {
            throw e;
        } catch (Exception e) {
            throw new UpdateEntityException("ClienteProspecto", 
                    "Error al actualizar información financiera: " + e.getMessage());
        }
    }

    /**
     * Valida la capacidad financiera del cliente para un crédito
     */
    public Map<String, Object> validarCapacidadFinanciera(String cedula, BigDecimal cuotaProyectada) {
        log.info("Validando capacidad financiera para cliente {} con cuota {}", cedula, cuotaProyectada);
        Map<String, Object> resultado = new HashMap<>();
        
        try {
            // 1. Obtener información del cliente
            ClienteProspecto cliente = clienteRepository.findByCedula(cedula)
                    .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con cédula: " + cedula));
            
            // 2. Calcular ingresos netos
            BigDecimal ingresos = cliente.getIngresos();
            BigDecimal egresos = cliente.getEgresos();
            BigDecimal ingresoNeto = ingresos.subtract(egresos);
            
            // 3. Calcular relación cuota/ingreso
            BigDecimal relacionCuotaIngreso = cuotaProyectada.divide(ingresoNeto, 4, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"));
            
            // 4. Validar ≤ 40% según regla de negocio
            boolean capacidadSuficiente = relacionCuotaIngreso.compareTo(
                    PORCENTAJE_MAXIMO_CUOTA_INGRESO.multiply(new BigDecimal("100"))) <= 0;
            
            resultado.put("cedula", cedula);
            resultado.put("nombre", cliente.getNombre() + " " + cliente.getApellido());
            resultado.put("ingresos", ingresos);
            resultado.put("egresos", egresos);
            resultado.put("ingresoNeto", ingresoNeto);
            resultado.put("cuotaProyectada", cuotaProyectada);
            resultado.put("relacionCuotaIngreso", relacionCuotaIngreso);
            resultado.put("maximoPermitido", PORCENTAJE_MAXIMO_CUOTA_INGRESO.multiply(new BigDecimal("100")));
            resultado.put("capacidadSuficiente", capacidadSuficiente);
            
            return resultado;
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al validar capacidad financiera: " + e.getMessage());
        }
    }

    // === CONSULTAS ===
    
    /**
     * Obtiene un cliente por su cédula
     */
    public ClienteProspectoDTO obtenerPorCedula(String cedula) {
        log.info("Buscando cliente por cédula: {}", cedula);
        
        try {
            ClienteProspecto cliente = clienteRepository.findByCedula(cedula)
                    .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con cédula: " + cedula));
            
            return clienteMapper.toDTO(cliente);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar cliente por cédula: " + e.getMessage());
        }
    }

    /**
     * Obtiene el historial completo del cliente
     */
    public List<Map<String, Object>> obtenerHistorialCliente(String cedula) {
        log.info("Obteniendo historial para cliente con cédula: {}", cedula);
        
        try {
            // Verificar si existe el cliente
            ClienteProspecto cliente = clienteRepository.findByCedula(cedula)
                    .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con cédula: " + cedula));
            
            // En una implementación real, consultar tablas de auditoría o historial
            // Aquí se simula un historial básico
            Map<String, Object> registroHistorial = new HashMap<>();
            registroHistorial.put("fecha", LocalDateTime.now().minusDays(30));
            registroHistorial.put("accion", "REGISTRO");
            registroHistorial.put("estado", cliente.getEstado());
            registroHistorial.put("detalles", "Registro inicial del cliente");
            
            // Aquí se incluirían más registros históricos en un caso real
            
            return List.of(registroHistorial);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener historial del cliente: " + e.getMessage());
        }
    }

    /**
     * Devuelve todos los clientes prospecto
     */
    public List<ClienteProspectoDTO> obtenerTodos() {
        return clienteRepository.findAll().stream()
                .map(clienteMapper::toDTO)
                .toList();
    }

    // === MÉTODOS PRIVADOS Y AUXILIARES ===
    
    /**
     * Algoritmo de validación de cédula ecuatoriana
     */
    private boolean validarCedulaEcuatoriana(String cedula) {
        // Verificar longitud y formato
        if (cedula == null || !cedula.matches(PATRON_CEDULA)) {
            return false;
        }
        
        try {
            // Extraer dígitos
            int[] digitos = new int[10];
            for (int i = 0; i < 10; i++) {
                digitos[i] = Integer.parseInt(cedula.substring(i, i + 1));
            }
            
            // Verificar código de provincia (01 al 24)
            int provincia = digitos[0] * 10 + digitos[1];
            if (provincia < 1 || provincia > 24) {
                return false;
            }
            
            // Algoritmo de validación ecuatoriano
            int suma = 0;
            for (int i = 0; i < 9; i++) {
                int coeficiente = (i % 2 == 0) ? 2 : 1;
                int valor = coeficiente * digitos[i];
                suma += (valor >= 10) ? valor - 9 : valor;
            }
            
            int verificador = (10 - (suma % 10)) % 10;
            return verificador == digitos[9];
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Calcula la edad basada en primeros dos dígitos de cédula (simulado)
     * En implementación real, la edad se obtendría de datos adicionales
     */
    private int calcularEdad(String cedula) {
        // En un caso real, la fecha de nacimiento vendría en datos adicionales
        // o se consultaría a una API externa. Aquí simulamos con fecha actual menos años aleatorios.
        return 25; // Para simplificar, asumimos siempre 25 años
    }

    /**
     * Determina el estado del cliente según información obtenida
     */
    private EstadoClientesEnum determinarEstadoCliente(Map<String, Object> datosCore) {
        boolean tieneCreditos = (boolean) datosCore.getOrDefault("tieneCreditos", false);
        boolean tieneCreditosVencidos = (boolean) datosCore.getOrDefault("tieneCreditosVencidos", false);
        
        if (!tieneCreditos) {
            return EstadoClientesEnum.NUEVO;
        } else if (tieneCreditos && tieneCreditosVencidos) {
            return EstadoClientesEnum.ACTIVO_CREDITOS_VENCIDOS;
        } else {
            return EstadoClientesEnum.ACTIVO;
        }
    }

    /**
     * Simulación de consulta a Core Bancario
     */
    private Map<String, Object> consultarEnCoreBancarioInterno(String cedula) {
        // Esta es una simulación. En producción, se conectaría al Core.
        Map<String, Object> resultado = new HashMap<>();
        
        // Simulación básica: clientes con cédula que termine en número par existen en Core
        boolean existeEnCore = Integer.parseInt(cedula.substring(9)) % 2 == 0;
        resultado.put("existeEnCore", existeEnCore);
        
        if (existeEnCore) {
            // Datos simulados para clientes que "existen" en Core
            resultado.put("tieneCreditos", true);
            resultado.put("tieneCreditosVencidos", cedula.endsWith("4") || cedula.endsWith("8"));
            resultado.put("cantidadCreditos", cedula.endsWith("2") ? 1 : 2);
            resultado.put("fechaUltimaTransaccion", LocalDate.now().minusDays(7).format(DateTimeFormatter.ISO_DATE));
        }
        
        return resultado;
    }
    
    /**
     * Registra auditoría de una operación
     */
    private void registrarAuditoria(String tabla, AccionAuditoriaEnum accion) {
        AuditoriaDTO audDto = new AuditoriaDTO();
        audDto.setTabla(tabla);
        audDto.setAccion(accion);
        audDto.setFechaHora(LocalDateTime.now());
        auditoriaService.createAuditoria(audDto);
    }
} 