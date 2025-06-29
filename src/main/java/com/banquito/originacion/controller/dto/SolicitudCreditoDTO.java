package com.banquito.originacion.controller.dto;

import com.banquito.originacion.enums.EstadoSolicitudEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Schema(description = "DTO para la gestión de solicitudes de crédito")
public class SolicitudCreditoDTO {

    @Schema(description = "Identificador único de la solicitud", example = "1")
    private Integer id;

    @NotNull(message = "El ID del cliente/prospecto es requerido")
    @Min(value = 1, message = "El ID del cliente/prospecto debe ser mayor a 0")
    @Schema(description = "Identificador del cliente/prospecto", example = "1")
    private Integer idClienteProspecto;

    @NotNull(message = "El ID del vehículo es requerido")
    @Min(value = 1, message = "El ID del vehículo debe ser mayor a 0")
    @Schema(description = "Identificador del vehículo", example = "1")
    private Integer idVehiculo;

    @NotNull(message = "El ID del vendedor es requerido")
    @Min(value = 1, message = "El ID del vendedor debe ser mayor a 0")
    @Schema(description = "Identificador del vendedor", example = "1")
    private Integer idVendedor;

    @Schema(description = "Número único de la solicitud", example = "SOL-2024-001", maxLength = 50)
    private String numeroSolicitud;

    @Schema(description = "Monto del crédito solicitado", example = "20000.00")
    private BigDecimal montoSolicitado;

    @NotNull(message = "El plazo en meses es requerido")
    @Min(value = 1, message = "El plazo debe ser mínimo 1 mes")
    @Max(value = 999, message = "El plazo no puede exceder 999 meses")
    @Schema(description = "Plazo del crédito en meses", example = "60")
    private Integer plazoMeses;

    @Schema(description = "Fecha y hora de la solicitud", example = "2024-01-15T10:30:00")
    private LocalDateTime fechaSolicitud;

    @NotNull(message = "La entrada es requerida")
    @DecimalMin(value = "0.0", message = "La entrada no puede ser negativa")
    @DecimalMax(value = "9999999999.99", message = "La entrada excede el límite permitido")
    @Schema(description = "Valor de entrada del crédito", example = "5000.00")
    private BigDecimal entrada;

    @NotNull(message = "El score interno es requerido")
    @Schema(description = "Puntuación de riesgo interno", example = "750.50")
    private BigDecimal scoreInterno;

    @NotNull(message = "El score externo es requerido")
    @Schema(description = "Puntuación de riesgo externo", example = "680.25")
    private BigDecimal scoreExterno;

    @Schema(description = "Relación porcentual entre cuota e ingreso", example = "35.50")
    private BigDecimal relacionCuotaIngreso;

    @Schema(description = "Tasa de interés anual del crédito", example = "12.50")
    private BigDecimal tasaAnual;

    @Schema(description = "Valor de la cuota mensual", example = "450.25")
    private BigDecimal cuotaMensual;

    @Schema(description = "Monto total a pagar del crédito", example = "27015.00")
    private BigDecimal totalPagar;

    @Schema(description = "Estado de la solicitud", example = "BORRADOR")
    private EstadoSolicitudEnum estado;

    @Schema(description = "Versión para control de concurrencia", example = "1")
    private Long version;
} 