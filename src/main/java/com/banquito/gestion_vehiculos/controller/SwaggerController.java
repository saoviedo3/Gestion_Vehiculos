package com.banquito.gestion_vehiculos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class SwaggerController {

    @GetMapping("/")
    public RedirectView redirectToSwagger() {
        return new RedirectView("/swagger-ui.html");
    }

    @GetMapping("/api")
    public RedirectView redirectToSwaggerFromApi() {
        return new RedirectView("/swagger-ui.html");
    }

    @GetMapping("/api/vehiculos")
    public RedirectView redirectToSwaggerFromVehiculos() {
        return new RedirectView("/swagger-ui.html");
    }
}
