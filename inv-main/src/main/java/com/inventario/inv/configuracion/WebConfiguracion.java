package com.inventario.inv.configuracion;

import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguracion implements WebMvcConfigurer {

    private final String carpeta;

    public WebConfiguracion(@Value("${inventario.portadas}") String carpeta) {
        this.carpeta = carpeta;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registro) {
        String ruta = Path.of(carpeta).toAbsolutePath().normalize().toUri().toString();
        if (!ruta.endsWith("/")) {
            ruta += "/";   // si la carpeta aún no existe, toUri() no pone la barra
        }
        registro.addResourceHandler("/portadas/**").addResourceLocations(ruta);
    }
}