package cl.smid.catalogo.config;

import cl.smid.catalogo.dominio.puerto.entrada.CatalogoUseCase;
import cl.smid.catalogo.dominio.puerto.salida.CausaRepositorio;
import cl.smid.catalogo.dominio.puerto.salida.DerechoRepositorio;
import cl.smid.catalogo.dominio.puerto.salida.EventoPublicador;
import cl.smid.catalogo.dominio.puerto.salida.GeneradorAltKey;
import cl.smid.catalogo.dominio.puerto.salida.RelojDominio;
import cl.smid.catalogo.dominio.servicio.EnsambladorArbol;
import cl.smid.catalogo.dominio.servicio.ServicioCatalogo;
import cl.smid.catalogo.infraestructura.seguridad.PropiedadesJwt;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Raíz de composición del dominio: instancia los componentes <b>puros</b> del núcleo
 * (sin anotaciones de framework) como beans, inyectándoles los adaptadores de los puertos de
 * salida que Spring descubre automáticamente.
 *
 * <p>Mantener el cableado aquí permite que {@code ServicioCatalogo} y {@code EnsambladorArbol}
 * permanezcan como POJOs verificables en aislamiento, mientras el contenedor se encarga de
 * unir las piezas en tiempo de ejecución. También habilita las propiedades de configuración
 * tipadas ({@code smid.jwt} y {@code smid.seguridad}).</p>
 */
@Configuration
@EnableConfigurationProperties({PropiedadesJwt.class, PropiedadesSeguridad.class})
public class DominioConfig {

    @Bean
    public EnsambladorArbol ensambladorArbol() {
        return new EnsambladorArbol();
    }

    @Bean
    public CatalogoUseCase catalogoUseCase(DerechoRepositorio derechoRepositorio,
                                           CausaRepositorio causaRepositorio,
                                           RelojDominio relojDominio,
                                           EventoPublicador eventoPublicador,
                                           EnsambladorArbol ensambladorArbol,
                                           GeneradorAltKey generadorAltKey) {
        return new ServicioCatalogo(
                derechoRepositorio, causaRepositorio, relojDominio,
                eventoPublicador, ensambladorArbol, generadorAltKey);
    }
}
