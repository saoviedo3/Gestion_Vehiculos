package com.banquito.gestion_vehiculos.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${server.port:80}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Gestión de Vehículos - Concesionario")
                        .description("API REST para la gestión de vehículos, concesionarios y vendedores")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Concesionario")
                                .email("soporte@banquito.com.ec")
                                .url("https://www.banquito.com.ec"))
                        .license(new License()
                                .name("Licencia Interna")
                                .url("https://www.banquito.com.ec/licencias")))
                .servers(List.of(
                        new Server()
                                .url("http://banquito-alb-1166574131.us-east-2.elb.amazonaws.com/api/vehiculos")
                                .description("Servidor de Producción - AWS ALB"),
                        new Server()
                                .url("http://localhost:" + serverPort + "/api/vehiculos")
                                .description("Servidor Local")
                ));
    }
}
