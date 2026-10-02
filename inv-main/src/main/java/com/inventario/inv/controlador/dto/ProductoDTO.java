package com.inventario.inv.controlador.dto;

import com.inventario.inv.modelo.Producto;

public record ProductoDTO(Long id, String nombre, String codigo,
                          Integer stock, boolean disponible) {

    public static ProductoDTO de(Producto p) {
        return new ProductoDTO(p.getId(), p.getNombre(), p.getCodigo(),
                               p.getStock(), p.isDisponible());
    }
}
