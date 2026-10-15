package com.cibertec.urbix;

import com.cibertec.urbix.model.Categoria;
import com.cibertec.urbix.model.Rol;
import com.cibertec.urbix.model.Usuario;
import com.cibertec.urbix.model.Zona;
import com.cibertec.urbix.repository.CategoriaRepository;
import com.cibertec.urbix.repository.RolRepository;
import com.cibertec.urbix.repository.UsuarioRepository;
import com.cibertec.urbix.repository.ZonaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DatosIniciales implements CommandLineRunner {

    private final RolRepository roles;
    private final UsuarioRepository usuarios;
    private final CategoriaRepository categorias;
    private final ZonaRepository zonas;
    private final PasswordEncoder clave;

    public DatosIniciales(RolRepository roles, UsuarioRepository usuarios,
            CategoriaRepository categorias, ZonaRepository zonas, PasswordEncoder clave) {
        this.roles = roles;
        this.usuarios = usuarios;
        this.categorias = categorias;
        this.zonas = zonas;
        this.clave = clave;
    }

    @Override
    public void run(String... args) {
        if (roles.count() == 0) {
            guardarRol("Supervisor / Admin");
            guardarRol("Inspector de Campo");
        }
        if (categorias.count() == 0) {
            guardarCategoria("Pistas y Calzadas");
            guardarCategoria("Veredas y Rampas");
            guardarCategoria("Alumbrado Público");
            guardarCategoria("Buzones");
        }
        if (zonas.count() == 0) {
            guardarZona("Cercado", "Sector 1 - Cercado");
            guardarZona("Santa Anita", "Sector 2 - Santa Anita");
            guardarZona("San Juan de Lurigancho", "Sector 3 - San Juan de Lurigancho");
        }
        if (!usuarios.existsByCorreo("admin@urbix.pe")) {
            Rol admin = roles.findByNombreRol("Supervisor / Admin").orElseThrow();
            Usuario usuario = new Usuario();
            usuario.setNombre("Administrador");
            usuario.setCorreo("admin@urbix.pe");
            usuario.setClaveHash(clave.encode("admin123"));
            usuario.setActivo(true);
            usuario.setRol(admin);
            usuarios.save(usuario);
        }
    }

    private void guardarRol(String nombre) {
        Rol rol = new Rol();
        rol.setNombreRol(nombre);
        roles.save(rol);
    }

    private void guardarCategoria(String nombre) {
        Categoria categoria = new Categoria();
        categoria.setNombreCategoria(nombre);
        categorias.save(categoria);
    }

    private void guardarZona(String distrito, String sector) {
        Zona zona = new Zona();
        zona.setDistrito(distrito);
        zona.setNombreSector(sector);
        zonas.save(zona);
    }
}
