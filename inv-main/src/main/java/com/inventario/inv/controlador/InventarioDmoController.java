package com.inventario.inv.controlador;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InventarioDmoController {

    private static final List<String> PRODUCTOS = List.of("Laptop", "Mouse", "Teclado", "Monitor", "audifonos", "telefono");

    @GetMapping("/api/demo/productos")
    public List<String> productos() {
        return PRODUCTOS;
    }

    @GetMapping("/api/demo/productos/{indice}")
    public String producto(@PathVariable int indice) {
        return PRODUCTOS.get(indice);
    }

}
