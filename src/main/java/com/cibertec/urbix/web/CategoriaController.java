package com.cibertec.urbix.web;

import com.cibertec.urbix.model.Categoria;
import com.cibertec.urbix.repository.CategoriaRepository;
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
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaRepository categorias;

    public CategoriaController(CategoriaRepository categorias) {
        this.categorias = categorias;
    }

    private boolean sesion(HttpSession sesion) {
        return sesion.getAttribute("usuario") != null;
    }

    @GetMapping
    public String listar(HttpSession sesion, Model modelo) {
        if (!sesion(sesion)) {
            return "redirect:/login";
        }
        modelo.addAttribute("lista", categorias.findAll());
        return "categorias";
    }

    @PostMapping("/guardar")
    public String guardar(@RequestParam String nombre, HttpSession sesion, Model modelo) {
        if (!sesion(sesion)) {
            return "redirect:/login";
        }
        if (nombre.isBlank() || categorias.existsByNombreCategoria(nombre.trim())) {
            modelo.addAttribute("lista", categorias.findAll());
            modelo.addAttribute("error", "Nombre vacío o repetido.");
            return "categorias";
        }
        Categoria categoria = new Categoria();
        categoria.setNombreCategoria(nombre.trim());
        categorias.save(categoria);
        return "redirect:/categorias";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, HttpSession sesion, Model modelo) {
        if (!sesion(sesion)) {
            return "redirect:/login";
        }
        try {
            categorias.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            modelo.addAttribute("lista", categorias.findAll());
            modelo.addAttribute("error", "No se puede borrar: ya se usó en incidencias.");
            return "categorias";
        }
        return "redirect:/categorias";
    }
}
