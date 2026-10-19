package com.cibertec.urbix.config;

import com.cibertec.urbix.model.Categoria;
import com.cibertec.urbix.model.Rol;
import com.cibertec.urbix.model.Usuario;
import com.cibertec.urbix.model.Zona;
import com.cibertec.urbix.repository.CategoriaRepository;
import com.cibertec.urbix.repository.RolRepository;
import com.cibertec.urbix.repository.UsuarioRepository;
import com.cibertec.urbix.repository.ZonaRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CargadorDatos implements CommandLineRunner {

    private static final List<String> CATEGORIAS = List.of(
            "Pistas y Calzadas",
            "Veredas y Rampas",
            "Alumbrado Público",
            "Buzones",
            "Señalización",
            "Mobiliario Urbano"
    );

    private static final List<String[]> ZONAS = List.of(
            new String[]{"Cercado", "Sector 1 - Cercado"},
            new String[]{"Santa Anita", "Sector 2 - Santa Anita"},
            new String[]{"San Juan de Lurigancho", "Sector 3 - San Juan de Lurigancho"},
            new String[]{"Villa El Salvador", "Sector 4 - Villa El Salvador"},
            new String[]{"Los Olivos", "Sector 5 - Los Olivos"}
    );

    private final RolRepository roles;
    private final UsuarioRepository usuarios;
    private final CategoriaRepository categorias;
    private final ZonaRepository zonas;
    private final PasswordEncoder clave;

    private final String adminCorreo;
    private final String adminClave;
    private final String inspectorCorreo;
    private final String inspectorClave;

    public CargadorDatos(RolRepository roles, UsuarioRepository usuarios,
            CategoriaRepository categorias, ZonaRepository zonas, PasswordEncoder clave,
            @Value("${urbix.admin.correo:admin@urbix.pe}") String adminCorreo,
            @Value("${urbix.admin.clave:admin123}") String adminClave,
            @Value("${urbix.inspector.correo:campo@urbix.pe}") String inspectorCorreo,
            @Value("${urbix.inspector.clave:1234}") String inspectorClave) {
        this.roles = roles;
        this.usuarios = usuarios;
        this.categorias = categorias;
        this.zonas = zonas;
        this.clave = clave;
        this.adminCorreo = adminCorreo;
        this.adminClave = adminClave;
        this.inspectorCorreo = inspectorCorreo;
        this.inspectorClave = inspectorClave;
    }

    @Override
    public void run(String... args) {
        guardarRol(Rol.ADMINISTRADOR);
        guardarRol(Rol.INSPECTOR);
        CATEGORIAS.forEach(this::guardarCategoria);
        ZONAS.forEach(zona -> guardarZona(zona[0], zona[1]));
        guardarUsuario("Administrador", adminCorreo, adminClave, Rol.ADMINISTRADOR);
        guardarUsuario("Inspector de Campo", inspectorCorreo, inspectorClave, Rol.INSPECTOR);
    }

    private void guardarRol(String nombre) {
        if (roles.existsByNombreRol(nombre)) {
            return;
        }
        Rol rol = new Rol();
        rol.setNombreRol(nombre);
        rol.setActivo(true);
        roles.save(rol);
    }

    private void guardarCategoria(String nombre) {
        if (categorias.existsByNombreCategoria(nombre)) {
            return;
        }
        Categoria categoria = new Categoria();
        categoria.setNombreCategoria(nombre);
        categoria.setActivo(true);
        categorias.save(categoria);
    }

    private void guardarZona(String distrito, String sector) {
        if (zonas.existsByNombreSector(sector)) {
            return;
        }
        Zona zona = new Zona();
        zona.setDistrito(distrito);
        zona.setNombreSector(sector);
        zona.setActivo(true);
        zonas.save(zona);
    }

    private void guardarUsuario(String nombre, String correo, String clavePlana, String rolNombre) {
        if (usuarios.existsByCorreo(correo)) {
            return;
        }
        Rol rol = roles.findByNombreRol(rolNombre).orElseThrow();
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setCorreo(correo);
        usuario.setClaveHash(clave.encode(clavePlana));
        usuario.setActivo(true);
        usuario.setRol(rol);
        usuarios.save(usuario);
    }
}