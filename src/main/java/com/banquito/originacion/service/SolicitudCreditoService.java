package com.banquito.originacion.service;

import com.banquito.originacion.controller.dto.AuditoriaDTO;
import com.banquito.originacion.controller.dto.SolicitudCreditoDTO;
import com.banquito.originacion.controller.mapper.SolicitudCreditoMapper;
import com.banquito.originacion.enums.AccionAuditoriaEnum;
import com.banquito.originacion.enums.EstadoSolicitudEnum;
import com.banquito.originacion.exception.CreateEntityException;
import com.banquito.originacion.exception.ResourceNotFoundException;
import com.banquito.originacion.exception.UpdateEntityException;
import com.banquito.originacion.model.ClienteProspecto;
import com.banquito.originacion.model.SolicitudCredito;
import com.banquito.originacion.model.Vehiculo;
import com.banquito.originacion.repository.ClienteProspectoRepository;
import com.banquito.originacion.repository.SolicitudCreditoRepository;
import com.banquito.originacion.repository.VehiculoRepository;
import com.banquito.originacion.repository.VendedorRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Validated
public class SolicitudCreditoService {
    
    private static final Logger log = LoggerFactory.getLogger(SolicitudCreditoService.class);
    private static final BigDecimal PORCENTAJE_MAXIMO_VEHICULO = new BigDecimal("0.80");
    private static final BigDecimal PORCENTAJE_MAXIMO_CUOTA_INGRESO = new BigDecimal("0.40");
    private static final BigDecimal PORCENTAJE_ENTRADA_ESTANDAR = new BigDecimal("0.20");

    private final SolicitudCreditoRepository solicitudRepository;
    private final SolicitudCreditoMapper solicitudMapper;
    private final AuditoriaService auditoriaService;
    private final ClienteProspectoRepository clienteProspectoRepository;
    private final VehiculoRepository vehiculoRepository;
    private final VendedorRepository vendedorRepository;
    
    // Tasas de interés según perfil de riesgo
    private static final Map<String, BigDecimal> TASAS_POR_PERFIL = new HashMap<>();
    static {
        TASAS_POR_PERFIL.put("A", new BigDecimal("0.095")); // 9.5%
        TASAS_POR_PERFIL.put("B", new BigDecimal("0.115")); // 11.5%
        TASAS_POR_PERFIL.put("C", new BigDecimal("0.135")); // 13.5%
    }

    public SolicitudCreditoService(SolicitudCreditoRepository solicitudRepository,
                                  SolicitudCreditoMapper solicitudMapper,
                                  AuditoriaService auditoriaService,
                                  ClienteProspectoRepository clienteProspectoRepository,
                                  VehiculoRepository vehiculoRepository,
                                  VendedorRepository vendedorRepository) {
        this.solicitudRepository = solicitudRepository;
        this.solicitudMapper = solicitudMapper;
        this.auditoriaService = auditoriaService;
        this.clienteProspectoRepository = clienteProspectoRepository;
        this.vehiculoRepository = vehiculoRepository;
        this.vendedorRepository = vendedorRepository;
    }

    // === CREACIÓN Y GESTIÓN ===
    
    /**
     * Crea una nueva solicitud de crédito en estado BORRADOR
     * 
     * Implementa:
     * Genera un número único para la solicitud
     * Crea la solicitud en estado BORRADOR
     * Registra auditoría de la creación
     * - Calcula automáticamente: monto solicitado, tasa anual, cuota mensual, total a pagar y relación cuota ingreso
     */
    @Transactional
    public SolicitudCreditoDTO crearSolicitud(@Valid SolicitudCreditoDTO solicitudDTO) {
        log.info("Creando nueva solicitud de crédito");
        try {
            // Validar que el vendedor exista
            if (solicitudDTO.getIdVendedor() == null) {
                throw new CreateEntityException("SolicitudCredito", "El ID del vendedor es requerido");
            }
            if (!vendedorRepository.existsById(solicitudDTO.getIdVendedor())) {
                throw new CreateEntityException("SolicitudCredito", "El vendedor con id=" + solicitudDTO.getIdVendedor() + " no existe");
            }
            
            // Generamos número único de solicitud
            solicitudDTO.setNumeroSolicitud(generarNumeroUnicoSolicitud());
            
            // Establecemos estado BORRADOR
            solicitudDTO.setEstado(EstadoSolicitudEnum.BORRADOR);
            
            // Establecemos fecha actual
            solicitudDTO.setFechaSolicitud(LocalDateTime.now());
            
            // Calculamos monto solicitado basado en valor del vehículo y entrada
            if (solicitudDTO.getIdVehiculo() != null) {
                Vehiculo vehiculo = vehiculoRepository.findById(solicitudDTO.getIdVehiculo())
                    .orElseThrow(() -> new ResourceNotFoundException("Vehículo no encontrado con id: " + solicitudDTO.getIdVehiculo()));
                
                BigDecimal entrada = solicitudDTO.getEntrada() != null ? solicitudDTO.getEntrada() : BigDecimal.ZERO;
                BigDecimal montoSolicitado = vehiculo.getValor().subtract(entrada);
                
                // Validar que el monto solicitado no exceda el 80% del valor del vehículo
                BigDecimal montoMaximo = vehiculo.getValor().multiply(PORCENTAJE_MAXIMO_VEHICULO);
                if (montoSolicitado.compareTo(montoMaximo) > 0) {
                    log.warn("Monto solicitado {} excede el 80% del valor del vehículo {}", montoSolicitado, montoMaximo);
                    montoSolicitado = montoMaximo;
                }
                
                solicitudDTO.setMontoSolicitado(montoSolicitado);
            }
            
            // Calculamos valores que no deberían ser ingresados por el cliente
            String perfilRiesgo = determinarPerfilRiesgo(solicitudDTO.getScoreExterno());
            BigDecimal tasaAnual = calcularTasaSegunPerfil(perfilRiesgo);
            solicitudDTO.setTasaAnual(tasaAnual);
            
            BigDecimal cuotaMensual = calcularCuotaMensual(
                    solicitudDTO.getMontoSolicitado(),
                    tasaAnual,
                    solicitudDTO.getPlazoMeses());
            solicitudDTO.setCuotaMensual(cuotaMensual);
            
            BigDecimal totalPagar = cuotaMensual.multiply(new BigDecimal(solicitudDTO.getPlazoMeses()));
            solicitudDTO.setTotalPagar(totalPagar);
            
            // Si tenemos acceso a los ingresos del cliente, calcular relación cuota/ingreso
            if (solicitudDTO.getIdClienteProspecto() != null) {
                ClienteProspecto cliente = clienteProspectoRepository.findById(solicitudDTO.getIdClienteProspecto())
                    .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + solicitudDTO.getIdClienteProspecto()));
                if (cliente != null && cliente.getIngresos() != null && cliente.getIngresos().compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal ingresoNeto = cliente.getIngresos().subtract(cliente.getEgresos() != null ? cliente.getEgresos() : BigDecimal.ZERO);
                    BigDecimal relacionCuotaIngreso = cuotaMensual.divide(ingresoNeto, 4, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal("100"));
                    solicitudDTO.setRelacionCuotaIngreso(relacionCuotaIngreso);
                }
            }
            
            // Convertimos a entidad y guardamos
            SolicitudCredito solicitud = solicitudMapper.toModel(solicitudDTO);
            solicitud.setId(null); // Aseguramos que sea nuevo
            
            SolicitudCredito guardada = solicitudRepository.save(solicitud);
            
            // Registramos auditoría
            registrarAuditoria("solicitudes_creditos", AccionAuditoriaEnum.INSERT);
            
            return solicitudMapper.toDTO(guardada);
        } catch (Exception e) {
            throw new CreateEntityException("SolicitudCredito", 
                    "Error al crear la solicitud de crédito: " + e.getMessage());
        }
    }

    /**
     * Actualiza una solicitud existente
     * 
     * Implementa:
     * Validar si puede modificarse según estado
     * Registrar cambio con justificación
     * - Recalcula automáticamente: monto solicitado, tasa anual, cuota mensual, total a pagar y relación cuota ingreso
     */
    @Transactional
    public SolicitudCreditoDTO actualizarSolicitud(Integer id, @Valid SolicitudCreditoDTO solicitudDTO) {
        log.info("Actualizando solicitud de crédito con id: {}", id);
        try {
            // Recuperar la entidad original
            SolicitudCredito solicitudExistente = solicitudRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Solicitud no encontrada con id: " + id));

            // Validar si puede modificarse según estado
            if (!puedeModificarse(solicitudExistente.getEstado())) {
                throw new UpdateEntityException("SolicitudCredito", 
                        "No se puede modificar una solicitud en estado " + solicitudExistente.getEstado());
            }

            // Actualizar solo los campos permitidos
            solicitudExistente.setIdClienteProspecto(solicitudDTO.getIdClienteProspecto());
            solicitudExistente.setIdVehiculo(solicitudDTO.getIdVehiculo());
            solicitudExistente.setEntrada(solicitudDTO.getEntrada());
            solicitudExistente.setPlazoMeses(solicitudDTO.getPlazoMeses());
            solicitudExistente.setScoreExterno(solicitudDTO.getScoreExterno());
            solicitudExistente.setIdVendedor(solicitudDTO.getIdVendedor());

            // Recalcular valores automáticos
            if (solicitudDTO.getIdVehiculo() != null) {
                Vehiculo vehiculo = vehiculoRepository.findById(solicitudDTO.getIdVehiculo())
                    .orElseThrow(() -> new ResourceNotFoundException("Vehículo no encontrado con id: " + solicitudDTO.getIdVehiculo()));
                BigDecimal entrada = solicitudDTO.getEntrada() != null ? solicitudDTO.getEntrada() : BigDecimal.ZERO;
                BigDecimal montoSolicitado = vehiculo.getValor().subtract(entrada);
                BigDecimal montoMaximo = vehiculo.getValor().multiply(PORCENTAJE_MAXIMO_VEHICULO);
                if (montoSolicitado.compareTo(montoMaximo) > 0) {
                    log.warn("Monto solicitado {} excede el 80% del valor del vehículo {}", montoSolicitado, montoMaximo);
                    montoSolicitado = montoMaximo;
                }
                solicitudExistente.setMontoSolicitado(montoSolicitado);
            }

            String perfilRiesgo = determinarPerfilRiesgo(solicitudDTO.getScoreExterno());
            BigDecimal tasaAnual = calcularTasaSegunPerfil(perfilRiesgo);
            solicitudExistente.setTasaAnual(tasaAnual);

            BigDecimal cuotaMensual = calcularCuotaMensual(
                    solicitudExistente.getMontoSolicitado(),
                    tasaAnual,
                    solicitudExistente.getPlazoMeses());
            solicitudExistente.setCuotaMensual(cuotaMensual);

            BigDecimal totalPagar = cuotaMensual.multiply(new BigDecimal(solicitudExistente.getPlazoMeses()));
            solicitudExistente.setTotalPagar(totalPagar);

            if (solicitudDTO.getIdClienteProspecto() != null) {
                ClienteProspecto cliente = clienteProspectoRepository.findById(solicitudDTO.getIdClienteProspecto())
                    .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + solicitudDTO.getIdClienteProspecto()));
                if (cliente != null && cliente.getIngresos() != null && cliente.getIngresos().compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal ingresoNeto = cliente.getIngresos().subtract(cliente.getEgresos() != null ? cliente.getEgresos() : BigDecimal.ZERO);
                    BigDecimal relacionCuotaIngreso = cuotaMensual.divide(ingresoNeto, 4, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal("100"));
                    solicitudExistente.setRelacionCuotaIngreso(relacionCuotaIngreso);
                }
            }

            // Guardar la entidad actualizada
            SolicitudCredito guardada = solicitudRepository.save(solicitudExistente);

            // Registrar auditoría
            registrarAuditoria("solicitudes_creditos", AccionAuditoriaEnum.UPDATE);

            return solicitudMapper.toDTO(guardada);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new UpdateEntityException("SolicitudCredito", 
                    "Error al actualizar la solicitud de crédito: " + e.getMessage());
        }
    }

    /**
     * Cambia el estado de una solicitud
     * 
     * Implementa:
     * Validar transición de estado permitida
     * Registrar cambio con trazabilidad
     * Notificar cambio de estado
     */
    @Transactional
    public void cambiarEstado(Integer id, EstadoSolicitudEnum nuevoEstado, String motivo, String usuario) {
        log.info("Cambiando estado de solicitud {} a {}", id, nuevoEstado);
        try {
            // Verificar existencia
            SolicitudCredito solicitud = solicitudRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Solicitud no encontrada con id: " + id));
            
            // Validar transición de estado permitida
            if (!esTransicionValida(solicitud.getEstado(), nuevoEstado)) {
                throw new UpdateEntityException("SolicitudCredito", 
                        "Transición no válida de " + solicitud.getEstado() + " a " + nuevoEstado);
            }
            
            // Actualizar estado
            solicitud.setEstado(nuevoEstado);
            solicitudRepository.save(solicitud);
            
            // Aquí debería registrarse la trazabilidad del cambio de estado
            // Esto debe ser implementado por otro grupo según lo indicado
            log.info("Se debe registrar la trazabilidad para el cambio de estado: {} -> {} con motivo: {}", 
                    solicitud.getEstado(), nuevoEstado, motivo);
            
            // Aquí se implementaría la notificación a involucrados
            // notificarCambioEstado(solicitud, nuevoEstado, usuario);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new UpdateEntityException("SolicitudCredito", 
                    "Error al cambiar estado de la solicitud: " + e.getMessage());
        }
    }
    
    // === SIMULACIÓN Y COTIZACIÓN ===
    
    /**
     * Simula escenarios de crédito con diferentes opciones a partir de IDs
     * 
     * Implementa:
     * Validar monto <= 80% del valor del vehículo
     * Validar cuota <= 40% de los ingresos
     * Generar 3 escenarios mínimo
     */
    public Map<String, Object> simularCredito(Integer idVehiculo, Integer idClienteProspecto, Integer plazoMaximo) {
        log.info("Simulando crédito para vehículo {} y cliente {}", idVehiculo, idClienteProspecto);
        
        Map<String, Object> resultado = new HashMap<>();
        List<Map<String, Object>> escenarios = new ArrayList<>();
        
        try {
            // Obtener los datos del vehículo
            Vehiculo vehiculo = vehiculoRepository.findById(idVehiculo)
                .orElseThrow(() -> new ResourceNotFoundException("Vehículo no encontrado con id: " + idVehiculo));
            BigDecimal montoVehiculo = vehiculo.getValor();
            
            // Obtener los datos del cliente
            ClienteProspecto cliente = clienteProspectoRepository.findById(idClienteProspecto)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + idClienteProspecto));
            BigDecimal ingresos = cliente.getIngresos();
            BigDecimal egresos = cliente.getEgresos() != null ? cliente.getEgresos() : BigDecimal.ZERO;
            
            // Por defecto usar perfil B (tasa intermedia) si no se puede determinar
            // En una implementación real, esto podría obtenerse de algún servicio externo
            String perfilRiesgo = "B";
            
            BigDecimal ingresoNeto = ingresos.subtract(egresos);
            BigDecimal montoMaximoFinanciable = montoVehiculo.multiply(PORCENTAJE_MAXIMO_VEHICULO);
            BigDecimal cuotaMaxima = ingresoNeto.multiply(PORCENTAJE_MAXIMO_CUOTA_INGRESO);
            
            // Validaciones iniciales
            if (montoMaximoFinanciable.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("El monto máximo financiable debe ser mayor a 0");
            }
            
            // Tasa según perfil de riesgo
            BigDecimal tasaAnual = calcularTasaSegunPerfil(perfilRiesgo);
            
            // Generar escenarios

            // Escenario 1: Con entrada estándar del 20% y plazo base
            BigDecimal entrada = montoVehiculo.multiply(PORCENTAJE_ENTRADA_ESTANDAR);
            BigDecimal montoCredito = montoVehiculo.subtract(entrada);
            Map<String, Object> escenarioEntrada = generarEscenario(
                    "Con entrada estándar (20%)", montoCredito, entrada, tasaAnual, 
                    plazoMaximo, cuotaMaxima, ingresoNeto);
            escenarios.add(escenarioEntrada);
            
            // Escenario 2: Sin entrada (máximo financiable 80%) y plazo base
            Map<String, Object> escenarioSinEntrada = generarEscenario(
                    "Sin entrada (máximo 80% del valor)", montoMaximoFinanciable, BigDecimal.ZERO, tasaAnual, 
                    plazoMaximo, cuotaMaxima, ingresoNeto);
            
            // Agregar advertencia específica sobre el límite de financiamiento
            if (escenarioSinEntrada.containsKey("advertencia")) {
                String advertenciaActual = (String) escenarioSinEntrada.get("advertencia");
                escenarioSinEntrada.put("advertencia", advertenciaActual + " La solicitud excede el 80% máximo financiable. Se requiere entrada mínima del 20%.");
            } else {
                escenarioSinEntrada.put("advertencia", "La solicitud excede el 80% máximo financiable. Se requiere entrada mínima del 20%.");
            }
            
            escenarios.add(escenarioSinEntrada);
            
            // Escenario 3: Plazo máximo extendido para lograr menor cuota mensual
            // Típicamente los créditos vehiculares pueden extenderse hasta 60 o 72 meses como máximo
            Integer plazoExtendido = Math.min(72, plazoMaximo + 24); // Extendemos el plazo en 2 años, máximo 72 meses
            Map<String, Object> escenarioPlazoMaximo = generarEscenario(
                    "Plazo máximo para menor cuota", montoCredito, entrada, tasaAnual, 
                    plazoExtendido, cuotaMaxima, ingresoNeto);
            escenarios.add(escenarioPlazoMaximo);
            
            resultado.put("montoVehiculo", montoVehiculo);
            resultado.put("ingresoNeto", ingresoNeto);
            resultado.put("montoMaximoFinanciable", montoMaximoFinanciable);
            resultado.put("cuotaMaxima", cuotaMaxima);
            resultado.put("tasaAnual", tasaAnual);
            resultado.put("escenarios", escenarios);
            
            return resultado;
        } catch (Exception e) {
            throw new RuntimeException("Error al simular crédito: " + e.getMessage(), e);
        }
    }

    /**
     * Simula escenarios de crédito con diferentes opciones incluyendo el score externo
     * 
     * Esta sobrecarga permite proporcionar el score externo para determinar el perfil de riesgo
     */
    public Map<String, Object> simularCredito(Integer idVehiculo, Integer idClienteProspecto, Integer plazoMaximo, BigDecimal scoreExterno) {
        log.info("Simulando crédito para vehículo {} y cliente {} con score {}", idVehiculo, idClienteProspecto, scoreExterno);
        
        Map<String, Object> resultado = new HashMap<>();
        List<Map<String, Object>> escenarios = new ArrayList<>();
        
        try {
            // Obtener los datos del vehículo
            Vehiculo vehiculo = vehiculoRepository.findById(idVehiculo)
                .orElseThrow(() -> new ResourceNotFoundException("Vehículo no encontrado con id: " + idVehiculo));
            BigDecimal montoVehiculo = vehiculo.getValor();
            
            // Obtener los datos del cliente
            ClienteProspecto cliente = clienteProspectoRepository.findById(idClienteProspecto)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + idClienteProspecto));
            BigDecimal ingresos = cliente.getIngresos();
            BigDecimal egresos = cliente.getEgresos() != null ? cliente.getEgresos() : BigDecimal.ZERO;
            
            // Determinar perfil de riesgo basado en score externo proporcionado
            String perfilRiesgo = determinarPerfilRiesgo(scoreExterno);
            
            BigDecimal ingresoNeto = ingresos.subtract(egresos);
            BigDecimal montoMaximoFinanciable = montoVehiculo.multiply(PORCENTAJE_MAXIMO_VEHICULO);
            BigDecimal cuotaMaxima = ingresoNeto.multiply(PORCENTAJE_MAXIMO_CUOTA_INGRESO);
            
            // Validaciones iniciales
            if (montoMaximoFinanciable.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("El monto máximo financiable debe ser mayor a 0");
            }
            
            // Tasa según perfil de riesgo
            BigDecimal tasaAnual = calcularTasaSegunPerfil(perfilRiesgo);
            
            // Generar escenarios

            // Escenario 1: Con entrada estándar del 20% y plazo base
            BigDecimal entrada = montoVehiculo.multiply(PORCENTAJE_ENTRADA_ESTANDAR);
            BigDecimal montoCredito = montoVehiculo.subtract(entrada);
            Map<String, Object> escenarioEntrada = generarEscenario(
                    "Con entrada estándar (20%)", montoCredito, entrada, tasaAnual, 
                    plazoMaximo, cuotaMaxima, ingresoNeto);
            escenarios.add(escenarioEntrada);
            
            // Escenario 2: Sin entrada (máximo financiable 80%) y plazo base
            Map<String, Object> escenarioSinEntrada = generarEscenario(
                    "Sin entrada (máximo 80% del valor)", montoMaximoFinanciable, BigDecimal.ZERO, tasaAnual, 
                    plazoMaximo, cuotaMaxima, ingresoNeto);
                
            // Agregar advertencia específica sobre el límite de financiamiento
            if (escenarioSinEntrada.containsKey("advertencia")) {
                String advertenciaActual = (String) escenarioSinEntrada.get("advertencia");
                escenarioSinEntrada.put("advertencia", advertenciaActual + " La solicitud excede el 80% máximo financiable. Se requiere entrada mínima del 20%.");
            } else {
                escenarioSinEntrada.put("advertencia", "La solicitud excede el 80% máximo financiable. Se requiere entrada mínima del 20%.");
            }
            
            escenarios.add(escenarioSinEntrada);
            
            // Escenario 3: Plazo máximo extendido para lograr menor cuota mensual
            // Típicamente los créditos vehiculares pueden extenderse hasta 60 o 72 meses como máximo
            Integer plazoExtendido = Math.min(72, plazoMaximo + 24); // Extendemos el plazo en 2 años, máximo 72 meses
            Map<String, Object> escenarioPlazoMaximo = generarEscenario(
                    "Plazo máximo para menor cuota", montoCredito, entrada, tasaAnual, 
                    plazoExtendido, cuotaMaxima, ingresoNeto);
            escenarios.add(escenarioPlazoMaximo);
            
            resultado.put("montoVehiculo", montoVehiculo);
            resultado.put("ingresoNeto", ingresoNeto);
            resultado.put("montoMaximoFinanciable", montoMaximoFinanciable);
            resultado.put("cuotaMaxima", cuotaMaxima);
            resultado.put("tasaAnual", tasaAnual);
            resultado.put("perfilRiesgo", perfilRiesgo);
            resultado.put("escenarios", escenarios);
            
            return resultado;
        } catch (Exception e) {
            throw new RuntimeException("Error al simular crédito: " + e.getMessage(), e);
        }
    }

    /**
     * Genera múltiples escenarios de crédito a partir de IDs
     */
    public List<Map<String, Object>> generarEscenarios(Integer idVehiculo, Integer idClienteProspecto, Integer plazoMaximo) {
        // Implementación actualizada que usa IDs
        Map<String, Object> simulacion = simularCredito(idVehiculo, idClienteProspecto, plazoMaximo);
        
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> escenarios = (List<Map<String, Object>>) simulacion.get("escenarios");
        return escenarios;
    }
    
    /**
     * Calcula la cuota mensual usando sistema francés
     */
    private BigDecimal calcularCuotaMensual(BigDecimal monto, BigDecimal tasaAnual, Integer plazo) {
        // Fórmula sistema francés: C = P * (i * (1 + i)^n) / ((1 + i)^n - 1)
        // Donde i es la tasa mensual
        BigDecimal tasaMensual = tasaAnual.divide(new BigDecimal("12"), 10, RoundingMode.HALF_UP);
        
        // Convertir tasa de porcentaje a decimal si es necesario (ej. 0.115 para 11.5%)
        if (tasaMensual.compareTo(BigDecimal.ONE) > 0) {
            tasaMensual = tasaMensual.divide(new BigDecimal("100"), 10, RoundingMode.HALF_UP);
        }
        
        BigDecimal numerador = tasaMensual.multiply(
                BigDecimal.ONE.add(tasaMensual).pow(plazo, MathContext.DECIMAL64));
        BigDecimal denominador = BigDecimal.ONE.add(tasaMensual).pow(plazo, MathContext.DECIMAL64)
                .subtract(BigDecimal.ONE);
        
        return monto.multiply(numerador)
                .divide(denominador, 2, RoundingMode.HALF_UP);
    }

    /**
     * Asigna tasa según perfil de riesgo del cliente
     */
    private BigDecimal calcularTasaSegunPerfil(String perfilRiesgo) {
        return TASAS_POR_PERFIL.getOrDefault(perfilRiesgo, new BigDecimal("0.115")); // Default: perfil B
    }

    // === EVALUACIÓN CREDITICIA ===
    
    /**
     * Evalúa una solicitud de crédito automáticamente
     * 
     * Implementa:
     * Consulta al buró de crédito
     * Clasificación por score
     * Rechazo automático por score bajo
     * Rechazo por créditos castigados
     * Trazabilidad completa
     */
    @Transactional
    public Map<String, Object> evaluarCreditoAutomatico(Integer idSolicitud) {
        log.info("Evaluando automáticamente solicitud {}", idSolicitud);
        Map<String, Object> resultado = new HashMap<>();
        
        try {
            // Verificar existencia
            SolicitudCredito solicitud = solicitudRepository.findById(idSolicitud)
                    .orElseThrow(() -> new ResourceNotFoundException("Solicitud no encontrada con id: " + idSolicitud));
            
            // 1. Validar completitud antes de evaluación
            validarCompletitudAnteEvaluacion(idSolicitud);
            
            // 2. Consultar buró de crédito (simulado)
            // En implementación real se haría la consulta externa
            BigDecimal scoreExterno = solicitud.getScoreExterno(); // Ya estaría cargado en la solicitud
            
            // 3. Clasificar por score
            String clasificacion = clasificarPorScore(scoreExterno);
            
            // 4. Aplicar reglas de rechazo automático
            boolean esAprobado = true;
            String motivoRechazo = null;
            
            // Score < 500 = Rechazo automático
            if (scoreExterno.compareTo(new BigDecimal("500")) < 0) {
                esAprobado = false;
                motivoRechazo = "Score externo insuficiente: " + scoreExterno;
            }
            
            // Créditos castigados = Rechazo automático
            if (tienereditosCastigados()) {
                esAprobado = false;
                motivoRechazo = "Cliente tiene créditos castigados";
            }
            
            // 5. Cambiar estado según resultado
            EstadoSolicitudEnum nuevoEstado = esAprobado ? 
                    EstadoSolicitudEnum.APROBADA : EstadoSolicitudEnum.RECHAZADA;
                    
            solicitud.setEstado(nuevoEstado);
            solicitudRepository.save(solicitud);
            
            // 6. Aquí debería registrarse la trazabilidad de la decisión
            // Esto debe ser implementado por otro grupo según lo indicado
            log.info("Se debe registrar la trazabilidad de la decisión para solicitud {}: {} - {}", 
                    idSolicitud, nuevoEstado, esAprobado ? "Aprobación automática" : motivoRechazo);
                    
            // Preparar respuesta
            resultado.put("idSolicitud", idSolicitud);
            resultado.put("numeroSolicitud", solicitud.getNumeroSolicitud());
            resultado.put("scoreExterno", scoreExterno);
            resultado.put("clasificacion", clasificacion);
            resultado.put("aprobado", esAprobado);
            if (!esAprobado) {
                resultado.put("motivoRechazo", motivoRechazo);
            }
            
            return resultado;
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al evaluar crédito: " + e.getMessage(), e);
        }
    }

    /**
     * Clasifica cliente según su score
     * - Score > 750: Cliente A
     * - Score 600-750: Cliente B
     * - Score < 600: Cliente C
     * - Score < 500: Rechazo automático
     */
    private String clasificarPorScore(BigDecimal scoreExterno) {
        if (scoreExterno.compareTo(new BigDecimal("750")) > 0) {
            return "A";
        } else if (scoreExterno.compareTo(new BigDecimal("600")) >= 0) {
            return "B";
        } else {
            return "C";
        }
    }

    /**
     * Obtiene el resultado de evaluación con toda la trazabilidad
     */
    public Map<String, Object> obtenerResultadoEvaluacion(Integer idSolicitud) {
        log.info("Obteniendo resultado de evaluación para solicitud {}", idSolicitud);
        
        try {
            // Verificar existencia
            SolicitudCredito solicitud = solicitudRepository.findById(idSolicitud)
                    .orElseThrow(() -> new ResourceNotFoundException("Solicitud no encontrada con id: " + idSolicitud));
            
            Map<String, Object> resultado = new HashMap<>();
            resultado.put("idSolicitud", idSolicitud);
            resultado.put("numeroSolicitud", solicitud.getNumeroSolicitud());
            resultado.put("estado", solicitud.getEstado());
            resultado.put("scoreInterno", solicitud.getScoreInterno());
            resultado.put("scoreExterno", solicitud.getScoreExterno());
            resultado.put("clasificacion", clasificarPorScore(solicitud.getScoreExterno()));
            resultado.put("relacionCuotaIngreso", solicitud.getRelacionCuotaIngreso());
            
            // Incluir trazabilidad (se implementaría con una tabla de auditoría específica)
            // resultado.put("trazabilidad", obtenerTrazabilidadSolicitud(idSolicitud));
            
            return resultado;
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener resultado de evaluación: " + e.getMessage(), e);
        }
    }

    // === INSTRUMENTACIÓN ===
    
    /**
     * Instrumenta un crédito aprobado
     * 
     * Implementa:
     * Solo solicitudes APROBADAS pueden instrumentarse
     * Fecha concesión = fecha actual + 2 días laborables
     * Crédito en Core Bancario con estado DRAFT
     */
    @Transactional
    public void instrumentarCredito(Integer idSolicitud) {
        log.info("Instrumentando crédito de solicitud {}", idSolicitud);
        
        try {
            // Verificar existencia
            SolicitudCredito solicitud = solicitudRepository.findById(idSolicitud)
                    .orElseThrow(() -> new ResourceNotFoundException("Solicitud no encontrada con id: " + idSolicitud));
            
            // Validar estado APROBADO
            if (solicitud.getEstado() != EstadoSolicitudEnum.APROBADA) {
                throw new IllegalStateException("Solo se pueden instrumentar solicitudes en estado APROBADA");
            }
            
            // Calcular fecha concesión (fecha actual + 2 días laborables)
            LocalDate fechaConcesion = calcularFechaConcesion(LocalDate.now());
            
            // Integración con Core Bancario (simulado)
            // crearCreditoEnCoreBancario(solicitud, fechaConcesion);
            
            // Cambiar estado a INSTRUMENTADA
            solicitud.setEstado(EstadoSolicitudEnum.INSTRUMENTADA);
            solicitudRepository.save(solicitud);
            
            // Registrar auditoría
            registrarAuditoria("solicitudes_creditos", AccionAuditoriaEnum.UPDATE);
            
            // Notificar involucrados
            // notificarInstrumentacion(solicitud, fechaConcesion);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al instrumentar crédito: " + e.getMessage(), e);
        }
    }

    // === CONSULTAS ===
    
    /**
     * Busca solicitud por número único
     */
    @Transactional(readOnly = true)
    public SolicitudCreditoDTO buscarPorNumero(String numeroSolicitud) {
        log.info("Buscando solicitud por número: {}", numeroSolicitud);
        
        try {
            SolicitudCredito solicitud = solicitudRepository.findByNumeroSolicitud(numeroSolicitud)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Solicitud no encontrada con número: " + numeroSolicitud));
            
            return solicitudMapper.toDTO(solicitud);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar solicitud por número: " + e.getMessage(), e);
        }
    }

    /**
     * Lista solicitudes por cliente
     */
    @Transactional(readOnly = true)
    public List<SolicitudCreditoDTO> listarPorCliente(String cedula) {
        log.info("Listando solicitudes para cliente con cédula: {}", cedula);
        
        try {
            List<SolicitudCredito> solicitudes = solicitudRepository.findByClienteProspectoCedula(cedula);
            return solicitudes.stream()
                    .map(solicitudMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error al listar solicitudes por cliente: " + e.getMessage(), e);
        }
    }

    /**
     * Lista solicitudes por estado
     */
    @Transactional(readOnly = true)
    public List<SolicitudCreditoDTO> listarPorEstado(EstadoSolicitudEnum estado) {
        log.info("Listando solicitudes en estado: {}", estado);
        
        try {
            List<SolicitudCredito> solicitudes = solicitudRepository.findByEstado(estado);
            return solicitudes.stream()
                    .map(solicitudMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error al listar solicitudes por estado: " + e.getMessage(), e);
        }
    }

    /**
     * Lista solicitudes por múltiples criterios
     */
    @Transactional(readOnly = true)
    public List<SolicitudCreditoDTO> listarPorFiltros(Map<String, Object> filtros) {
        log.info("Listando solicitudes con filtros: {}", filtros);
        
        try {
            // En una implementación real, se usaría Specification o Criteria API de JPA
            // Aquí simulamos con algunas búsquedas básicas
            
            if (filtros.containsKey("estado")) {
                EstadoSolicitudEnum estado = (EstadoSolicitudEnum) filtros.get("estado");
                return listarPorEstado(estado);
            } else if (filtros.containsKey("cedula")) {
                String cedula = (String) filtros.get("cedula");
                return listarPorCliente(cedula);
            } else {
                // Si no hay filtros definidos, devolver todas
                return solicitudRepository.findAll().stream()
                        .map(solicitudMapper::toDTO)
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar solicitudes por filtros: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    public List<SolicitudCreditoDTO> listarTodas() {
        return solicitudRepository.findAll().stream()
                .map(solicitudMapper::toDTO)
                .toList();
    }

    // === MÉTODOS AUXILIARES Y PRIVADOS ===

    /**
     * Valida si una solicitud puede modificarse según su estado
     */
    private boolean puedeModificarse(EstadoSolicitudEnum estado) {
        // Solo se pueden modificar solicitudes en BORRADOR o EN_REVISION
        return estado == EstadoSolicitudEnum.BORRADOR || estado == EstadoSolicitudEnum.EN_REVISION;
    }

    /**
     * Valida si una transición de estado es válida
     */
    private boolean esTransicionValida(EstadoSolicitudEnum estadoActual, EstadoSolicitudEnum nuevoEstado) {
        // Reglas de transición definidas
        Map<EstadoSolicitudEnum, List<EstadoSolicitudEnum>> transicionesValidas = new HashMap<>();
        
        transicionesValidas.put(EstadoSolicitudEnum.BORRADOR, 
                Arrays.asList(EstadoSolicitudEnum.EN_REVISION, EstadoSolicitudEnum.CANCELADA));
                
        transicionesValidas.put(EstadoSolicitudEnum.EN_REVISION, 
                Arrays.asList(EstadoSolicitudEnum.APROBADA, EstadoSolicitudEnum.RECHAZADA, 
                        EstadoSolicitudEnum.CANCELADA));
                
        transicionesValidas.put(EstadoSolicitudEnum.APROBADA, 
                Arrays.asList(EstadoSolicitudEnum.CANCELADA));
                
        // No hay transiciones válidas desde RECHAZADA o CANCELADA
        
        List<EstadoSolicitudEnum> transicionesPosibles = transicionesValidas.get(estadoActual);
        return transicionesPosibles != null && transicionesPosibles.contains(nuevoEstado);
    }

    /**
     * Genera un número único de solicitud
     * Formato: SOL-YYYYMMDD-XXXX (donde XXXX es un número secuencial)
     */
    private String generarNumeroUnicoSolicitud() {
        String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        // En implementación real, se obtendría un secuencial de base de datos
        String secuencial = String.format("%04d", new Random().nextInt(10000));
        return "SOL-" + fecha + "-" + secuencial;
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

    /**
     * Valida completitud documental antes de evaluación
     */
    private void validarCompletitudAnteEvaluacion(Integer idSolicitud) {
        // En implementación real, se coordinaría con DocumentacionService
        log.info("Validando completitud documental para solicitud {}", idSolicitud);
        
        // Simulación de validación exitosa
        // Si hubiera error, lanzaría excepción con detalle de documentos faltantes
    }

    /**
     * Calcula fecha de concesión = fecha actual + 2 días laborables
     */
    private LocalDate calcularFechaConcesion(LocalDate fechaInicial) {
        LocalDate fecha = fechaInicial;
        int diasLaborables = 0;
        
        // Contar 2 días laborables (excluyendo sábados y domingos)
        while (diasLaborables < 2) {
            fecha = fecha.plusDays(1);
            
            // Si no es fin de semana, cuenta como día laborable
            if (fecha.getDayOfWeek() != DayOfWeek.SATURDAY && 
                    fecha.getDayOfWeek() != DayOfWeek.SUNDAY) {
                diasLaborables++;
            }
        }
        
        return fecha;
    }
    
    /**
     * Genera un escenario de crédito con cálculos detallados
     */
    private Map<String, Object> generarEscenario(String nombreEscenario, BigDecimal montoCredito, 
            BigDecimal entrada, BigDecimal tasaAnual, Integer plazoMaximo, 
            BigDecimal cuotaMaxima, BigDecimal ingresoNeto) {
        
        Map<String, Object> escenario = new HashMap<>();
        escenario.put("nombre", nombreEscenario);
        escenario.put("montoCredito", montoCredito);
        escenario.put("entrada", entrada);
        escenario.put("tasaAnual", tasaAnual);
        
        // Calcular cuota con plazo máximo
        BigDecimal cuotaPlazoMaximo = calcularCuotaMensual(montoCredito, tasaAnual, plazoMaximo);
        
        // Si la cuota excede el máximo permitido, ajustar plazo
        Integer plazoFinal = plazoMaximo;
        BigDecimal cuotaFinal = cuotaPlazoMaximo;
        
        if (cuotaPlazoMaximo.compareTo(cuotaMaxima) > 0) {
            // La cuota excede capacidad de pago, intentar ajustar el plazo
            // Buscamos un plazo mayor que permita estar dentro de la capacidad de pago
            Integer plazoNuevo = plazoMaximo;
            BigDecimal cuotaNueva = cuotaPlazoMaximo;
            boolean plazoAjustado = false;
            
            // Incrementar plazo hasta conseguir una cuota aceptable o llegar al límite (60 meses típicamente)
            while (cuotaNueva.compareTo(cuotaMaxima) > 0 && plazoNuevo < 60) {
                plazoNuevo += 6; // Incrementar en semestres
                cuotaNueva = calcularCuotaMensual(montoCredito, tasaAnual, plazoNuevo);
                if (cuotaNueva.compareTo(cuotaMaxima) <= 0) {
                    plazoAjustado = true;
                    plazoFinal = plazoNuevo;
                    cuotaFinal = cuotaNueva;
                    break;
                }
            }
            
            if (!plazoAjustado) {
                // Si no se pudo ajustar el plazo, advertir que se necesita mayor entrada
                escenario.put("advertencia", "La cuota excede capacidad de pago máxima. Se requiere mayor entrada o un préstamo menor.");
            } else {
                escenario.put("advertencia", "Se ajustó el plazo de " + plazoMaximo + " a " + plazoFinal + " meses para mantener la cuota dentro de capacidad de pago.");
            }
        }
        
        escenario.put("plazoMeses", plazoFinal);
        escenario.put("cuotaMensual", cuotaFinal.setScale(2, RoundingMode.HALF_UP));
        
        // Calcular el total a pagar (capital + intereses)
        BigDecimal totalPagar = cuotaFinal.multiply(new BigDecimal(plazoFinal)).setScale(2, RoundingMode.HALF_UP);
        escenario.put("totalPagar", totalPagar);
        
        // Calcular el total de intereses a pagar
        BigDecimal totalIntereses = totalPagar.subtract(montoCredito).setScale(2, RoundingMode.HALF_UP);
        escenario.put("totalIntereses", totalIntereses);
        
        // Calcular relación cuota/ingreso
        if (ingresoNeto.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal relacionCuotaIngreso = cuotaFinal.divide(ingresoNeto, 4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP);
            escenario.put("relacionCuotaIngreso", relacionCuotaIngreso);
        }
            
        return escenario;
    }
    
    /**
     * Verifica si hay créditos castigados (simulado)
     */
    private boolean tienereditosCastigados() {
        // Simulación - en implementación real consultaría a buró de crédito
        return false;
    }

    /**
     * Determina el perfil de riesgo del cliente basado en su score
     */
    private String determinarPerfilRiesgo(BigDecimal scoreExterno) {
        return clasificarPorScore(scoreExterno); // Reutiliza el método existente
    }
}