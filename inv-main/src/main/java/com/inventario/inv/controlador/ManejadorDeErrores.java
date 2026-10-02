package com.inventario.inv.controlador;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class ManejadorDeErrores {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String portadaGrande(RedirectAttributes flash) {
        flash.addFlashAttribute("error", "La portada no puede pasar de 1 MB");
        return "redirect:/productos/nueva";
    }
}
