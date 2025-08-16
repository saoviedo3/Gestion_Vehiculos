package com.banquito.gestion_vehiculos.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Test", description = "Endpoints de prueba para verificar Swagger")
@RestController
@RequestMapping("/api/test")
public class TestController {

    @Operation(summary = "Endpoint de prueba", description = "Endpoint simple para verificar que Swagger esté funcionando")
    @GetMapping("/hello")
    public String hello() {
        return "¡Hola! Swagger está funcionando correctamente";
    }

    @Operation(summary = "Estado del sistema", description = "Verifica el estado del sistema")
    @GetMapping("/status")
    public String status() {
        return "Sistema funcionando correctamente";
    }
}
