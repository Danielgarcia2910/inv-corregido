package com.inventario.inv.controlador;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
@RestController
public class InicioController {

    @GetMapping("/")
    public String inicio() {
        return "Inventario esta vivo";
    }
    
}
