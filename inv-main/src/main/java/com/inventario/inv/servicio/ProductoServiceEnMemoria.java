package com.inventario.inv.servicio;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.inventario.inv.modelo.Producto;

@Service
public class ProductoServiceEnMemoria implements ProductoService {

    private final Map<Long, Producto> productos = new LinkedHashMap<>();
    private final AtomicLong siguienteId = new AtomicLong(1);

    public ProductoServiceEnMemoria() {
        crear(new Producto(null, "Laptop", "LAP-001", 12));
        crear(new Producto(null, "Mouse", "MOU-001", 40));
        crear(new Producto(null, "Teclado", "TEC-001", 25));
        crear(new Producto(null, "Monitor", "MON-001", 8));
        crear(new Producto(null, "audifonos", "AUD-001", 30));
        crear(new Producto(null, "telefono", "TEL-001", 15));
    }

    @Override
    public List<Producto> listar() {
        return new ArrayList<>(productos.values());
    }

    @Override
    public List<Producto> buscarPorNombre(String texto) {
        if (texto == null || texto.isBlank()) {
            return listar();
        }
        String aguja = texto.toLowerCase();
        return productos.values().stream()
                .filter(p -> p.getNombre().toLowerCase().contains(aguja))
                .toList();
    }

    @Override
    public Optional<Producto> buscar(Long id) {
        return Optional.ofNullable(productos.get(id));
    }

    @Override
    public Producto crear(Producto producto) {
        validar(producto);
        comprobarCodigoLibre(producto.getCodigo(), null);
        producto.setId(siguienteId.getAndIncrement());
        productos.put(producto.getId(), producto);
        return producto;
    }

    @Override
    public Producto editar(Long id, Producto datos) {
        Producto producto = buscar(id)
                .orElseThrow(() -> new ReglaNegocioException("No existe el producto " + id));
        validar(datos);
        comprobarCodigoLibre(datos.getCodigo(), id);
        producto.setNombre(datos.getNombre());
        producto.setCodigo(datos.getCodigo());
        producto.setStock(datos.getStock());
        return producto;
    }

    @Override
    public void borrar(Long id) {
        if (productos.remove(id) == null) {
            throw new ReglaNegocioException("No existe el producto " + id);
        }
    }

    // ── reglas de negocio ────────────────────────────────────────────────
    private void validar(Producto producto) {
        if (producto.getNombre() == null || producto.getNombre().isBlank()) {
            throw new ReglaNegocioException("El nombre es obligatorio");
        }
        if (producto.getCodigo() == null || producto.getCodigo().isBlank()) {
            throw new ReglaNegocioException("El código es obligatorio");
        }
    }

    private void comprobarCodigoLibre(String codigo, Long idQueSeEdita) {
        boolean repetido = productos.values().stream()
                .anyMatch(p -> p.getCodigo().equals(codigo)
                        && !p.getId().equals(idQueSeEdita));
        if (repetido) {
            throw new ReglaNegocioException("Ya hay un producto con el código " + codigo);
        }
    }
}
