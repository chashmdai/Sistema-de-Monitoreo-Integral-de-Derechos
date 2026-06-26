package cl.smid.requerimientos.config;

import cl.smid.requerimientos.dominio.puerto.entrada.GestionRequerimientosUseCase;
import cl.smid.requerimientos.dominio.puerto.salida.CatalogoDerechos;
import cl.smid.requerimientos.dominio.puerto.salida.CorrelativoFolioRepositorio;
import cl.smid.requerimientos.dominio.puerto.salida.DirectorioIdentidad;
import cl.smid.requerimientos.dominio.puerto.salida.DirectorioPersonas;
import cl.smid.requerimientos.dominio.puerto.salida.DirectorioSedes;
import cl.smid.requerimientos.dominio.puerto.salida.EventoPublicador;
import cl.smid.requerimientos.dominio.puerto.salida.GeneradorAltKey;
import cl.smid.requerimientos.dominio.puerto.salida.RelojDominio;
import cl.smid.requerimientos.dominio.puerto.salida.RequerimientoRepositorio;
import cl.smid.requerimientos.dominio.servicio.GeneradorFolio;
import cl.smid.requerimientos.dominio.servicio.ServicioRequerimientos;
import cl.smid.requerimientos.infraestructura.cliente.PropiedadesIntegracion;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Cablea los servicios de dominio (POJO puros) como beans, inyectándoles los adaptadores que
 * implementan sus puertos de salida. Mantiene el dominio libre de anotaciones de Spring: aquí —en la
 * frontera de configuración— es donde se ensambla el grafo de dependencias.
 */
@Configuration
public class DominioConfig {

    /**
     * Generador de folios. La fecha de corte de la serie oficial proviene de la configuración
     * ({@code smid.folio.inicio-oficial}); antes de esa fecha la política asigna serie BETA.
     *
     * @param directorioSedes puerto de resolución del código de sede
     * @param correlativos    puerto de reserva atómica del correlativo
     * @param folio           propiedades de política de folio
     * @return el generador de folios de dominio
     */
    @Bean
    public GeneradorFolio generadorFolio(DirectorioSedes directorioSedes,
                                         CorrelativoFolioRepositorio correlativos,
                                         PropiedadesFolio folio) {
        return new GeneradorFolio(directorioSedes, correlativos, folio.inicioOficial());
    }

    /**
     * Servicio de aplicación del dominio, expuesto por su puerto de entrada. La política de
     * verificación del profesional ({@code permisiva}/{@code estricta}) se deriva de la
     * configuración de integración.
     *
     * @return la implementación del caso de uso
     */
    @Bean
    public GestionRequerimientosUseCase gestionRequerimientos(RequerimientoRepositorio repositorio,
                                                              GeneradorFolio generadorFolio,
                                                              DirectorioPersonas directorioPersonas,
                                                              CatalogoDerechos catalogoDerechos,
                                                              DirectorioIdentidad directorioIdentidad,
                                                              EventoPublicador eventoPublicador,
                                                              RelojDominio reloj,
                                                              GeneradorAltKey generadorAltKey,
                                                              PropiedadesIntegracion integracion) {
        return new ServicioRequerimientos(repositorio, generadorFolio, directorioPersonas,
                catalogoDerechos, directorioIdentidad, eventoPublicador, reloj, generadorAltKey,
                integracion.verificacionEstricta());
    }
}
