package cl.smid.antecedentes.config;

import cl.smid.antecedentes.dominio.modelo.PoliticaRoles;
import cl.smid.antecedentes.dominio.puerto.entrada.GestionFichasUseCase;
import cl.smid.antecedentes.dominio.puerto.entrada.GestionHallazgosUseCase;
import cl.smid.antecedentes.dominio.puerto.entrada.GestionReferenciasUseCase;
import cl.smid.antecedentes.dominio.puerto.salida.CorrelativoFolioPort;
import cl.smid.antecedentes.dominio.puerto.salida.GeneradorIdentificadores;
import cl.smid.antecedentes.dominio.puerto.salida.PublicadorEventos;
import cl.smid.antecedentes.dominio.puerto.salida.RelojDominio;
import cl.smid.antecedentes.dominio.puerto.salida.RepositorioFichas;
import cl.smid.antecedentes.dominio.puerto.salida.RepositorioHallazgos;
import cl.smid.antecedentes.dominio.puerto.salida.RepositorioReferencias;
import cl.smid.antecedentes.dominio.servicio.EvaluadorAlcance;
import cl.smid.antecedentes.dominio.servicio.GeneradorFolio;
import cl.smid.antecedentes.dominio.servicio.MaquinaEstadosFicha;
import cl.smid.antecedentes.dominio.servicio.ServicioFichas;
import cl.smid.antecedentes.dominio.servicio.ServicioHallazgos;
import cl.smid.antecedentes.dominio.servicio.ServicioReferencias;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Cableado del dominio (hexagonal estricto): el dominio es POJO puro sin anotaciones de Spring;
 * <strong>todo</strong> su wiring vive aqui. Los adaptadores de puertos de salida son beans
 * {@code @Component} que Spring inyecta en estos metodos {@code @Bean}.
 */
@Configuration
public class CableadoDominio {

    @Bean
    public MaquinaEstadosFicha maquinaEstadosFicha() {
        return new MaquinaEstadosFicha();
    }

    @Bean
    public EvaluadorAlcance evaluadorAlcance() {
        return new EvaluadorAlcance();
    }

    @Bean
    public PoliticaRoles politicaRoles(PropiedadesSeguridad propiedades) {
        return new PoliticaRoles(propiedades.rolesRevisionSet(), propiedades.rolesAdminSet());
    }

    @Bean
    public GeneradorFolio generadorFolio(CorrelativoFolioPort correlativoFolioPort, RelojDominio relojDominio) {
        return new GeneradorFolio(correlativoFolioPort, relojDominio);
    }

    @Bean
    public GestionFichasUseCase gestionFichasUseCase(RepositorioFichas repositorioFichas,
                                                     RepositorioHallazgos repositorioHallazgos,
                                                     RepositorioReferencias repositorioReferencias,
                                                     MaquinaEstadosFicha maquinaEstadosFicha,
                                                     EvaluadorAlcance evaluadorAlcance,
                                                     GeneradorFolio generadorFolio,
                                                     RelojDominio relojDominio,
                                                     GeneradorIdentificadores generadorIdentificadores,
                                                     PublicadorEventos publicadorEventos,
                                                     PoliticaRoles politicaRoles) {
        return new ServicioFichas(repositorioFichas, repositorioHallazgos, repositorioReferencias,
                maquinaEstadosFicha, evaluadorAlcance, generadorFolio, relojDominio, generadorIdentificadores,
                publicadorEventos, politicaRoles);
    }

    @Bean
    public GestionHallazgosUseCase gestionHallazgosUseCase(RepositorioHallazgos repositorioHallazgos,
                                                           RepositorioReferencias repositorioReferencias,
                                                           GeneradorFolio generadorFolio,
                                                           RelojDominio relojDominio,
                                                           GeneradorIdentificadores generadorIdentificadores,
                                                           PublicadorEventos publicadorEventos,
                                                           PoliticaRoles politicaRoles) {
        return new ServicioHallazgos(repositorioHallazgos, repositorioReferencias, generadorFolio, relojDominio,
                generadorIdentificadores, publicadorEventos, politicaRoles);
    }

    @Bean
    public GestionReferenciasUseCase gestionReferenciasUseCase(RepositorioReferencias repositorioReferencias,
                                                               RelojDominio relojDominio,
                                                               GeneradorIdentificadores generadorIdentificadores,
                                                               PoliticaRoles politicaRoles) {
        return new ServicioReferencias(repositorioReferencias, relojDominio, generadorIdentificadores, politicaRoles);
    }
}
