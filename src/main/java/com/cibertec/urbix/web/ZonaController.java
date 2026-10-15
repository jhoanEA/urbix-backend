package com.cibertec.urbix.web;

import com.cibertec.urbix.model.Zona;
import com.cibertec.urbix.repository.ZonaRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.dao.DataIntegrityViolationException;
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

    private final ZonaRepository zonas;

    public ZonaController(ZonaRepository zonas) {
        this.zonas = zonas;
    }

    private boolean sesion(HttpSession sesion) {
        return sesion.getAttribute("usuario") != null;
    }

    @GetMapping
    public String listar(HttpSession sesion, Model modelo) {
        if (!sesion(sesion)) {
            return "redirect:/login";
        }
        modelo.addAttribute("lista", zonas.findAll());
        return "zonas";
    }

    @PostMapping("/guardar")
    public String guardar(@RequestParam String distrito, @RequestParam String sector,
            HttpSession sesion, Model modelo) {
        if (!sesion(sesion)) {
            return "redirect:/login";
        }
        if (sector.isBlank() || zonas.existsByNombreSector(sector.trim())) {
            modelo.addAttribute("lista", zonas.findAll());
            modelo.addAttribute("error", "Sector vacío o repetido.");
            return "zonas";
        }
        Zona zona = new Zona();
        zona.setDistrito(distrito.trim());
        zona.setNombreSector(sector.trim());
        zonas.save(zona);
        return "redirect:/zonas";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, HttpSession sesion, Model modelo) {
        if (!sesion(sesion)) {
            return "redirect:/login";
        }
        try {
            zonas.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            modelo.addAttribute("lista", zonas.findAll());
            modelo.addAttribute("error", "No se puede borrar: ya se usó en incidencias.");
            return "zonas";
        }
        return "redirect:/zonas";
    }
}
