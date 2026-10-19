package com.cibertec.urbix.service;

import com.cibertec.urbix.dto.FichaReporte;
import com.cibertec.urbix.dto.IncidenciaResponse;
import com.cibertec.urbix.model.Incidencia;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FichaService {

    private static final String PLANTILLA = "/reporte/ficha.jrxml";

    private static final SimpleDateFormat FORMATO =
            new SimpleDateFormat("dd/MM/yyyy HH:mm", new Locale("es", "PE"));

    private final IncidenciaService incidencias;

    public FichaService(IncidenciaService incidencias) {
        this.incidencias = incidencias;
    }

    @Transactional(readOnly = true)
    public byte[] generarFicha(String uuid) {
        Incidencia incidencia = incidencias.buscarPorCodigo(uuid);
        IncidenciaResponse datos = IncidenciaResponse.de(incidencia);

        FichaReporte ficha = new FichaReporte();
        ficha.setCodigo(datos.getCodigo());
        ficha.setCategoria(datos.getCategoria());
        ficha.setZona(datos.getZona());
        ficha.setDescripcion(datos.getDescripcion());
        ficha.setPrioridad(datos.getPrioridad());
        ficha.setEstado(datos.getEstado());
        ficha.setInspector(incidencia.getUsuario() == null
                ? "Sin registrar" : incidencia.getUsuario().getNombre());
        ficha.setFecha(datos.getFecha());

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("generadoEn", FORMATO.format(new Date()));

        try {
            JasperReport reporte = JasperCompileManager.compileReport(plantilla());
            JasperPrint print = JasperFillManager.fillReport(reporte, parametros,
                    new JRBeanCollectionDataSource(List.of(ficha)));
            return JasperExportManager.exportReportToPdf(print);
        } catch (JRException e) {
            throw new IllegalStateException("No se pudo generar la ficha.", e);
        }
    }

    private InputStream plantilla() {
        InputStream flujo = FichaService.class.getResourceAsStream(PLANTILLA);
        if (flujo == null) {
            throw new IllegalStateException("No se encontro la plantilla de la ficha.");
        }
        return flujo;
    }
}