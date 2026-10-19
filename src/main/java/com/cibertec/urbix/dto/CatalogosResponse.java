package com.cibertec.urbix.dto;

import java.util.List;

public record CatalogosResponse(List<String> categorias, List<String> zonas) {
}