package com.cibertec.urbix.web;

import com.cibertec.urbix.service.FichaService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/fichas")
public class FichaWebController {

    private final FichaService fichas;

    public FichaWebController(FichaService fichas) {
        this.fichas = fichas;
    }

    @GetMapping("/{codigo}.pdf")
    public ResponseEntity<byte[]> descargar(@PathVariable String codigo) {
        byte[] pdf = fichas.generarFicha(codigo);
        HttpHeaders cabeceras = new HttpHeaders();
        cabeceras.setContentType(MediaType.APPLICATION_PDF);
        cabeceras.setContentLength(pdf.length);
        cabeceras.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=urbix-ficha.pdf");
        return new ResponseEntity<>(pdf, cabeceras, HttpStatus.OK);
    }
}