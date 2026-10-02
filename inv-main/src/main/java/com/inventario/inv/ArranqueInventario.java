package com.inventario.inv;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ArranqueInventario implements CommandLineRunner {
    
    @Override
    public void run(String... args)  {
        System.out.println("===INVENTARIO LISTO ===");
    }
    
}
