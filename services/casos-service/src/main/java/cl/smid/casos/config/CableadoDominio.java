package cl.smid.casos.config;

import cl.smid.casos.dominio.puerto.salida.ClienteRequerimientos;
import cl.smid.casos.dominio.puerto.salida.CorrelativoExpedientePort;
import cl.smid.casos.dominio.puerto.salida.DirectorioSedes;
import cl.smid.casos.dominio.puerto.salida.GeneradorIdentificadores;
import cl.smid.casos.dominio.puerto.salida.PublicadorEventos;
import cl.smid.casos.dominio.puerto.salida.Reloj;
import cl.smid.casos.dominio.puerto.salida.RepositorioCasos;
import cl.smid.casos.dominio.servicio.EvaluadorAlcance;
import cl.smid.casos.dominio.servicio.GeneradorNumeroExpediente;
import cl.smid.casos.dominio.servicio.MaquinaEstadosCaso;
import cl.smid.casos.dominio.servicio.ServicioCasos;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

/**
 * Cableado del dominio: el dominio es POJO puro (sin anotaciones de Spring), de modo que aquí se
 * declaran sus piezas como beans y se ensambla {@link ServicioCasos} a partir de los adaptadores de
 * salida y los servicios de dominio. Centralizar el cableado mantiene el dominio libre de framework.
 */
@Configuration
@EnableConfigurationProperties({
        PropiedadesSedes.class,
        PropiedadesSeguridad.class,
        PropiedadesEventos.class,
        PropiedadesExpediente.class,
        PropiedadesEnriquecimiento.class
})
public class CableadoDominio {

    @Bean
    public MaquinaEstadosCaso maquinaEstadosCaso() {
        return new MaquinaEstadosCaso();
    }

    @Bean
    public GeneradorNumeroExpediente generadorNumeroExpediente() {
        return new GeneradorNumeroExpediente();
    }

    @Bean
    public EvaluadorAlcance evaluadorAlcance() {
        return new EvaluadorAlcance();
    }

    /**
     * Servicio de aplicación que implementa los tres puertos de entrada
     * ({@code MaterializarCaso}, {@code ConsultarCasos}, {@code TransicionarCaso}); Spring lo inyecta
     * por tipo donde se requiera cualquiera de ellos.
     */
    @Bean
    public ServicioCasos servicioCasos(RepositorioCasos repositorioCasos,
                                       CorrelativoExpedientePort correlativoExpediente,
                                       DirectorioSedes directorioSedes,
                                       PublicadorEventos publicadorEventos,
                                       ClienteRequerimientos clienteRequerimientos,
                                       Reloj reloj,
                                       GeneradorIdentificadores generadorIdentificadores,
                                       MaquinaEstadosCaso maquinaEstadosCaso,
                                       GeneradorNumeroExpediente generadorNumeroExpediente,
                                       EvaluadorAlcance evaluadorAlcance,
                                       PropiedadesSeguridad propiedadesSeguridad,
                                       PropiedadesExpediente propiedadesExpediente) {
        return new ServicioCasos(repositorioCasos, correlativoExpediente, directorioSedes,
                publicadorEventos, clienteRequerimientos, reloj, generadorIdentificadores,
                maquinaEstadosCaso, generadorNumeroExpediente, evaluadorAlcance,
                Set.copyOf(propiedadesSeguridad.rolesCoordinacion()),
                propiedadesExpediente.inicioOficial());
    }
}
