package com.cibertec.urbix.web;

import com.cibertec.urbix.dto.ZonaRequest;
import com.cibertec.urbix.exception.DatosInvalidosException;
import com.cibertec.urbix.service.ZonaService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/zonas")
public class ZonaController {

    private final ZonaService zonas;

    public ZonaController(ZonaService zonas) {
        this.zonas = zonas;
    }

    @GetMapping
    public String listar(Model modelo) {
        modelo.addAttribute("lista", zonas.listar());
        return "zonas";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid ZonaRequest solicitud, Model modelo) {
        try {
            zonas.registrar(solicitud);
        } catch (DatosInvalidosException e) {
            modelo.addAttribute("lista", zonas.listar());
            modelo.addAttribute("error", e.getMessage());
            return "zonas";
        }
        return "redirect:/zonas";
    }

    @PostMapping("/{id}/estado")
    public String estado(@PathVariable Integer id, @RequestParam boolean activo) {
        zonas.cambiarActivo(id, activo);
        return "redirect:/zonas";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Integer id, Model modelo) {
        try {
            zonas.eliminar(id);
        } catch (RuntimeException e) {
            modelo.addAttribute("lista", zonas.listar());
            modelo.addAttribute("error", "No se puede borrar: ya se usa en incidencias. Desactívela.");
            return "zonas";
        }
        return "redirect:/zonas";
    }
}