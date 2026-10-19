package com.cibertec.urbix.media;

import org.springframework.http.MediaType;

public final class TipoImagen {

    private TipoImagen() {
    }

    public static MediaType de(byte[] imagen) {
        if (imagen == null || imagen.length < 4) {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
        if ((imagen[0] & 0xFF) == 0x89 && imagen[1] == 'P' && imagen[2] == 'N' && imagen[3] == 'G') {
            return MediaType.IMAGE_PNG;
        }
        if ((imagen[0] & 0xFF) == 0xFF && (imagen[1] & 0xFF) == 0xD8) {
            return MediaType.IMAGE_JPEG;
        }
        if (imagen[0] == 'G' && imagen[1] == 'I' && imagen[2] == 'F') {
            return MediaType.IMAGE_GIF;
        }
        return MediaType.APPLICATION_OCTET_STREAM;
    }
}