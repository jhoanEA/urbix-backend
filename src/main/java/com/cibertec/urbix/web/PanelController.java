package com.cibertec.urbix.web;

import com.cibertec.urbix.service.PanelService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PanelController {

    private static final int CANTIDAD_RECIENTES = 5;

    private final PanelService panel;

    public PanelController(PanelService panel) {
        this.panel = panel;
    }

    @GetMapping({"/", "/panel"})
    public String panel(Model modelo) {
        modelo.addAttribute("total", panel.total());
        modelo.addAttribute("reportadas", panel.contar("Reportada"));
        modelo.addAttribute("enProceso", panel.contar("En Proceso"));
        modelo.addAttribute("solucionadas", panel.contar("Solucionada"));
        modelo.addAttribute("recientes", panel.ultimas(CANTIDAD_RECIENTES));
        return "panel";
    }
}