package com.inventario.inv.servicio;

import java.util.List;
import java.util.Optional;

import com.inventario.inv.modelo.Producto;

public interface ProductoService {

    List<Producto> listar();

    List<Producto> buscarPorNombre(String texto);

    Optional<Producto> buscar(Long id);

    Producto crear(Producto producto);

    Producto editar(Long id, Producto producto);

    void borrar(Long id);
}
