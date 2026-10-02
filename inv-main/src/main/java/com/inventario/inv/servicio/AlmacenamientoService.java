package com.inventario.inv.servicio;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AlmacenamientoService {

    private static final Set<String> TIPOS = Set.of("image/jpeg", "image/png", "image/webp");

    private final Path carpeta;

    public AlmacenamientoService(@Value("${inventario.portadas}") String ruta) throws IOException {
        this.carpeta = Path.of(ruta).toAbsolutePath().normalize();
        Files.createDirectories(this.carpeta);
    }

    /** Lanza ReglaNegocioException si hay archivo y no es una imagen admitida. */
    public void comprobar(MultipartFile archivo) {
        if (archivo == null || archivo.isEmpty()) {
            return;
        }
        String tipo = archivo.getContentType();
        if (tipo == null || !TIPOS.contains(tipo)) {
            throw new ReglaNegocioException("La portada debe ser JPG, PNG o WEBP");
        }
    }

    /** Guarda la portada y devuelve el nombre del archivo (null si no se subió ninguna). */
    public String guardarPortada(Long idProducto, MultipartFile archivo) {
        if (archivo == null || archivo.isEmpty()) {
            return null;
        }
        comprobar(archivo);

        // El nombre lo pone el servidor, NUNCA el navegador.
        String nombre = "producto-" + idProducto + extension(archivo.getContentType());
        Path destino = carpeta.resolve(nombre);

        try (var in = archivo.getInputStream()) {
            Files.copy(in, destino, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new ReglaNegocioException("No se pudo guardar la portada");
        }
        return nombre;
    }

    private String extension(String tipo) {
        return switch (tipo) {
            case "image/png" -> ".png";
            case "image/webp" -> ".webp";
            default -> ".jpg";
        };
    }
}