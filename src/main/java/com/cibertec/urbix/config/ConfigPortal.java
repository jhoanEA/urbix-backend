package com.cibertec.urbix.config;

import com.cibertec.urbix.web.FiltroPortal;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ConfigPortal implements WebMvcConfigurer {

    private final FiltroPortal filtroPortal;

    public ConfigPortal(FiltroPortal filtroPortal) {
        this.filtroPortal = filtroPortal;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registro) {
        registro.addInterceptor(filtroPortal)
                .addPathPatterns("/panel", "/incidencias/**", "/fichas/**", "/usuarios/**",
                        "/roles/**", "/categorias/**", "/zonas/**");
    }
}