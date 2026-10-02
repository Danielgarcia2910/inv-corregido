package com.inventario.inv.controlador.api;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inventario.inv.controlador.dto.ProductoDTO;
import com.inventario.inv.controlador.dto.ProductoEntrada;
import com.inventario.inv.modelo.Producto;
import com.inventario.inv.servicio.ProductoService;
import com.inventario.inv.servicio.ReglaNegocioException;

@RestController
@RequestMapping("/api/productos")
public class ProductoRestController {

    private final ProductoService servicio;

    public ProductoRestController(ProductoService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<ProductoDTO> listar(@RequestParam(defaultValue = "") String buscar) {
        return servicio.buscarPorNombre(buscar).stream().map(ProductoDTO::de).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> uno(@PathVariable Long id) {
        return servicio.buscar(id)
                .map(ProductoDTO::de)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());   // 404, no 200 vacío
    }

    @PostMapping
    public ResponseEntity<ProductoDTO> crear(@RequestBody ProductoEntrada entrada) {
        Producto creado = servicio.crear(
                new Producto(null, entrada.nombre(), entrada.codigo(), entrada.stock()));
        return ResponseEntity
                .created(URI.create("/api/productos/" + creado.getId()))   // 201 + Location
                .body(ProductoDTO.de(creado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoDTO> editar(@PathVariable Long id,
                                              @RequestBody ProductoEntrada entrada) {
        if (servicio.buscar(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Producto actualizado = servicio.editar(id,
                new Producto(null, entrada.nombre(), entrada.codigo(), entrada.stock()));
        return ResponseEntity.ok(ProductoDTO.de(actualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrar(@PathVariable Long id) {
        if (servicio.buscar(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        servicio.borrar(id);
        return ResponseEntity.noContent().build();            // 204
    }

    // Una regla de negocio rota (código repetido, nombre vacío...) es un 400, no un 500.
    @ExceptionHandler(ReglaNegocioException.class)
    public ResponseEntity<String> reglaNegocio(ReglaNegocioException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
