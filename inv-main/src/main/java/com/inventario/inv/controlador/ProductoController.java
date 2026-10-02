package com.inventario.inv.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.inventario.inv.modelo.Producto;
import com.inventario.inv.servicio.ProductoService;
import com.inventario.inv.servicio.ReglaNegocioException;
import com.inventario.inv.servicio.AlmacenamientoService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoService servicio;
    private final AlmacenamientoService almacen;

    public ProductoController(ProductoService servicio, AlmacenamientoService almacen) {
        this.servicio = servicio;
        this.almacen = almacen;
    }

    @GetMapping
    public String catalogo(@RequestParam(defaultValue = "") String buscar, Model modelo) {
        modelo.addAttribute("productos", servicio.buscarPorNombre(buscar));
        modelo.addAttribute("buscar", buscar);
        return "productos/lista";
    }

    @GetMapping("/{id}")
    public String ficha(@PathVariable Long id, Model modelo) {
        Producto producto = servicio.buscar(id)
                .orElseThrow(() -> new ReglaNegocioException("No existe el producto " + id));
        modelo.addAttribute("producto", producto);
        return "productos/ficha";
    }

    @GetMapping("/nueva")
    public String formularioNuevo(Model modelo) {
        modelo.addAttribute("producto", new Producto());
        return "productos/formulario";
    }

    @PostMapping
    public String crear(@Valid @ModelAttribute("producto") Producto producto,
                        BindingResult errores,          
                        @RequestParam(required = false) MultipartFile archivoPortada,
                        RedirectAttributes flash) {

        if (errores.hasErrors()) {
            return "productos/formulario";              
        }
        try {
            almacen.comprobar(archivoPortada);        
        } catch (ReglaNegocioException e) {
            errores.rejectValue("portada", "tipo", e.getMessage());
            return "productos/formulario";
        }
        try {
            Producto creado = servicio.crear(producto);
            String nombre = almacen.guardarPortada(creado.getId(), archivoPortada);
            if (nombre != null) {
                creado.setPortada(nombre);
            }
        } catch (ReglaNegocioException e) {
            errores.rejectValue("codigo", "duplicado", e.getMessage());
            return "productos/formulario";
        }
        flash.addFlashAttribute("aviso", "Producto añadido");
        return "redirect:/productos";                  
    }

    @GetMapping("/{id}/editar")
    public String formularioEditar(@PathVariable Long id, Model modelo) {
        Producto producto = servicio.buscar(id)
                .orElseThrow(() -> new ReglaNegocioException("No existe el producto " + id));
        modelo.addAttribute("producto", producto);
        return "productos/formulario";
    }

    @PostMapping("/{id}")
    public String editar(@PathVariable Long id,
                         @Valid @ModelAttribute("producto") Producto producto,
                         BindingResult errores,
                         RedirectAttributes flash) {

        
        producto.setId(id);

        if (errores.hasErrors()) {
            return "productos/formulario";
        }
        try {
            servicio.editar(id, producto);
        } catch (ReglaNegocioException e) {
            errores.rejectValue("codigo", "duplicado", e.getMessage());
            return "productos/formulario";
        }
        flash.addFlashAttribute("aviso", "Cambios guardados");
        return "redirect:/productos/" + id;
    }

    @ExceptionHandler(ReglaNegocioException.class)
    public String reglaNegocio(ReglaNegocioException e, RedirectAttributes flash) {
        flash.addFlashAttribute("error", e.getMessage());
        return "redirect:/productos";
    }
}