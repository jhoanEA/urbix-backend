package com.cibertec.urbix.web;

import com.cibertec.urbix.exception.DatosInvalidosException;
import com.cibertec.urbix.service.CategoriaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaService categorias;

    public CategoriaController(CategoriaService categorias) {
        this.categorias = categorias;
    }

    @GetMapping
    public String listar(Model modelo) {
        modelo.addAttribute("lista", categorias.listar());
        return "categorias";
    }

    @PostMapping("/guardar")
    public String guardar(@RequestParam String nombre, Model modelo) {
        try {
            categorias.registrar(nombre);
        } catch (DatosInvalidosException e) {
            modelo.addAttribute("lista", categorias.listar());
            modelo.addAttribute("error", e.getMessage());
            return "categorias";
        }
        return "redirect:/categorias";
    }

    @PostMapping("/{id}/estado")
    public String estado(@PathVariable Integer id, @RequestParam boolean activo) {
        categorias.cambiarActivo(id, activo);
        return "redirect:/categorias";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Integer id, Model modelo) {
        try {
            categorias.eliminar(id);
        } catch (RuntimeException e) {
            modelo.addAttribute("lista", categorias.listar());
            modelo.addAttribute("error", "No se puede borrar: ya se usa en incidencias. Desactívela.");
            return "categorias";
        }
        return "redirect:/categorias";
    }
}