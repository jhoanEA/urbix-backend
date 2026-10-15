package com.cibertec.urbix.web;

import com.cibertec.urbix.model.Incidencia;
import com.cibertec.urbix.repository.IncidenciaRepository;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/incidencias")
public class IncidenciaWebController {

    private final IncidenciaRepository incidencias;

    private static final Set<String> ESTADOS = Set.of("Reportada", "En Proceso", "Solucionada");

    public IncidenciaWebController(IncidenciaRepository incidencias) {
        this.incidencias = incidencias;
    }

    private boolean sesion(HttpSession sesion) {
        return sesion.getAttribute("usuario") != null;
    }

    @GetMapping
    public String listar(HttpSession sesion, Model modelo) {
        if (!sesion(sesion)) {
            return "redirect:/login";
        }
        List<Incidencia> lista = incidencias.findAllByOrderByIdIncidenciaDesc();
        modelo.addAttribute("lista", lista);
        modelo.addAttribute("estados", ESTADOS);
        return "incidencias";
    }

    @PostMapping("/{id}/estado")
    public String cambiarEstado(@PathVariable Integer id, @RequestParam String estado,
            @RequestParam String cuadrilla, HttpSession sesion) {
        if (!sesion(sesion)) {
            return "redirect:/login";
        }
        incidencias.findById(id).ifPresent(i -> {
            if (ESTADOS.contains(estado)) {
                i.setEstado(estado);
            }
            i.setCuadrillaAsignada(cuadrilla.trim().isEmpty() ? null : cuadrilla.trim());
            incidencias.save(i);
        });
        return "redirect:/incidencias";
    }
}
