package cl.smid.instituciones.config;

import cl.smid.instituciones.dominio.puerto.salida.GeneradorIdentificadores;
import cl.smid.instituciones.dominio.puerto.salida.PublicadorEventos;
import cl.smid.instituciones.dominio.puerto.salida.RelojDominio;
import cl.smid.instituciones.dominio.puerto.salida.RepositorioInstituciones;
import cl.smid.instituciones.dominio.puerto.salida.RepositorioPuntosFocales;
import cl.smid.instituciones.dominio.puerto.salida.RepositorioTipos;
import cl.smid.instituciones.dominio.servicio.ServicioInstituciones;
import cl.smid.instituciones.infraestructura.seguridad.PropiedadesSeguridad;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Cableado del dominio: construye el {@link ServicioInstituciones} (POJO puro, sin
 * anotaciones de framework) inyectándole sus puertos de salida y el conjunto de roles
 * administradores. Es el único lugar donde la infraestructura conoce la construcción del
 * dominio (override #11: el dominio no depende de Spring).
 */
@Configuration
public class CableadoDominio {

    /**
     * Crea el servicio de dominio que implementa los tres casos de uso. Al exponerse como
     * un único bean concreto, Spring lo inyecta indistintamente por cualquiera de las
     * interfaces de entrada que implementa.
     */
    @Bean
    ServicioInstituciones servicioInstituciones(RepositorioTipos repositorioTipos,
                                                RepositorioInstituciones repositorioInstituciones,
                                                RepositorioPuntosFocales repositorioPuntosFocales,
                                                PublicadorEventos publicadorEventos,
                                                RelojDominio reloj,
                                                GeneradorIdentificadores generador,
                                                PropiedadesSeguridad propiedadesSeguridad) {
        return new ServicioInstituciones(
                repositorioTipos,
                repositorioInstituciones,
                repositorioPuntosFocales,
                publicadorEventos,
                reloj,
                generador,
                propiedadesSeguridad.rolesAdmin());
    }
}
