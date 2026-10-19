package com.cibertec.urbix.web;

import com.cibertec.urbix.dto.IncidenciaResponse;
import com.cibertec.urbix.media.TipoImagen;
import com.cibertec.urbix.model.Incidencia;
import com.cibertec.urbix.service.CatalogoService;
import com.cibertec.urbix.service.IncidenciaFiltroService;
import com.cibertec.urbix.service.IncidenciaService;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    private final IncidenciaService incidencias;
    private final IncidenciaFiltroService filtro;
    private final CatalogoService catalogos;

    public IncidenciaWebController(IncidenciaService incidencias, IncidenciaFiltroService filtro,
            CatalogoService catalogos) {
        this.incidencias = incidencias;
        this.filtro = filtro;
        this.catalogos = catalogos;
    }

    @GetMapping
    public String listar(@RequestParam(required = false) String texto,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) String zona,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String prioridad,
            Model modelo) {
        List<IncidenciaResponse> lista = filtro.filtrar(texto, estado, zona, categoria, prioridad);
        modelo.addAttribute("lista", lista);
        modelo.addAttribute("total", incidencias.listar().size());
        modelo.addAttribute("estados", IncidenciaService.ESTADOS);
        modelo.addAttribute("prioridades", IncidenciaService.PRIORIDADES);
        modelo.addAttribute("categorias", catalogos.listar().categorias());
        modelo.addAttribute("zonas", catalogos.listar().zonas());
        modelo.addAttribute("textoActual", texto == null ? "" : texto);
        modelo.addAttribute("estadoActual", estado == null ? "" : estado);
        modelo.addAttribute("zonaActual", zona == null ? "" : zona);
        modelo.addAttribute("categoriaActual", categoria == null ? "" : categoria);
        modelo.addAttribute("prioridadActual", prioridad == null ? "" : prioridad);
        return "incidencias";
    }

    @PostMapping("/{id}/seguimiento")
    public String seguimiento(@PathVariable Integer id, @RequestParam String estado) {
        incidencias.actualizarSeguimiento(id, estado);
        return "redirect:/incidencias";
    }

    @GetMapping("/{codigo}/foto")
    public ResponseEntity<byte[]> foto(@PathVariable String codigo) {
        Incidencia incidencia = incidencias.conFoto(codigo);
        HttpHeaders cabeceras = new HttpHeaders();
        cabeceras.setContentType(TipoImagen.de(incidencia.getFoto()));
        cabeceras.setContentLength(incidencia.getFoto().length);
        return new ResponseEntity<>(incidencia.getFoto(), cabeceras, HttpStatus.OK);
    }
}