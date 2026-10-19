package com.cibertec.urbix.dto;

import com.cibertec.urbix.model.Incidencia;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class IncidenciaResponse {

    private static final DateTimeFormatter FORMATO =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private Integer id;
    private String codigo;
    private String categoria;
    private String zona;
    private String descripcion;
    private String prioridad;
    private String estado;
    private String inspector;
    private String ubicacion;
    private String fecha;
    private boolean conFoto;
    private byte[] foto;

    public IncidenciaResponse() {
    }

    public static IncidenciaResponse de(Incidencia incidencia) {
        IncidenciaResponse respuesta = new IncidenciaResponse();
        respuesta.id = incidencia.getIdIncidencia();
        respuesta.codigo = incidencia.getClientUuid();
        respuesta.categoria = texto(incidencia.getCategoria() == null
                ? null : incidencia.getCategoria().getNombreCategoria());
        respuesta.zona = texto(incidencia.getZona() == null
                ? null : incidencia.getZona().getNombreSector());
        respuesta.descripcion = incidencia.getDescripcion();
        respuesta.prioridad = incidencia.getPrioridad();
        respuesta.estado = incidencia.getEstado();
        respuesta.inspector = texto(incidencia.getUsuario() == null
                ? null : incidencia.getUsuario().getNombre());
        respuesta.ubicacion = ubicacion(incidencia);
        respuesta.fecha = incidencia.getFechaRegistro() == null
                ? "" : FORMATO.format(incidencia.getFechaRegistro());
        respuesta.conFoto = incidencia.getFoto() != null && incidencia.getFoto().length > 0;
        return respuesta;
    }

    public static List<IncidenciaResponse> de(List<Incidencia> lista) {
        return lista.stream().map(IncidenciaResponse::de).toList();
    }

    public boolean tieneUbicacion() {
        return ubicacion != null && !ubicacion.isBlank();
    }

    public String getLatitud() {
        return tieneUbicacion() ? coordenada(0) : "";
    }

    public String getLongitud() {
        return tieneUbicacion() ? coordenada(1) : "";
    }

    public String getEnlaceMapa() {
        if (!tieneUbicacion()) {
            return "";
        }
        return "https://www.openstreetmap.org/?mlat=" + getLatitud()
                + "&mlon=" + getLongitud()
                + "#map=17/" + getLatitud() + "/" + getLongitud();
    }

    private String coordenada(int indice) {
        String[] partes = ubicacion.split(",");
        return indice < partes.length ? partes[indice].trim() : "";
    }

    private static String ubicacion(Incidencia incidencia) {
        if (incidencia.getLatitud() == null || incidencia.getLongitud() == null) {
            return "";
        }
        return String.format("%.4f, %.4f", incidencia.getLatitud(), incidencia.getLongitud());
    }

    private static String texto(String valor) {
        return valor == null ? "" : valor;
    }

    public Integer getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getZona() {
        return zona;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public String getEstado() {
        return estado;
    }

    public String getInspector() {
        return inspector;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public String getFecha() {
        return fecha;
    }

    public boolean isConFoto() {
        return conFoto;
    }

    public byte[] getFoto() {
        return foto;
    }
}