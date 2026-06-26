package cl.smid.personas.config;

import cl.smid.personas.dominio.puerto.salida.BuscadorDuplicados;
import cl.smid.personas.dominio.puerto.salida.EventoPublicador;
import cl.smid.personas.dominio.puerto.salida.GeneradorAltKey;
import cl.smid.personas.dominio.puerto.salida.PersonaRepositorio;
import cl.smid.personas.dominio.puerto.salida.RelojDominio;
import cl.smid.personas.dominio.servicio.BuscadorDuplicadosBaseLocal;
import cl.smid.personas.dominio.servicio.ServicioPersonas;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Raíz de composición del dominio (composition root). El núcleo es deliberadamente puro: sus
 * clases ({@link ServicioPersonas}, {@link BuscadorDuplicadosBaseLocal}) no llevan anotaciones de
 * Spring. Aquí se instancian como beans inyectándoles sus puertos, que la infraestructura provee.
 *
 * <p>Así se mantiene la regla hexagonal: el dominio no conoce el framework; es la capa de
 * configuración quien lo ensambla. Cambiar una implementación de puerto (p. ej. el publicador de
 * eventos o el buscador de duplicados) no toca el dominio.</p>
 */
@Configuration
public class DominioConfig {

    /**
     * Adaptador de dominio del puerto de deduplicación que coteja la base local. Se expone como
     * bean para inyectarlo en el servicio; si en el futuro se añade un buscador que además
     * consulte a {@code siger-service}, bastará con sustituir este bean.
     */
    @Bean
    public BuscadorDuplicados buscadorDuplicados(PersonaRepositorio repositorio) {
        return new BuscadorDuplicadosBaseLocal(repositorio);
    }

    /**
     * Caso de uso de gestión de personas, ensamblado a partir de sus puertos de salida. La
     * demarcación transaccional la aporta el controlador; este bean es lógica de negocio pura.
     */
    @Bean
    public ServicioPersonas servicioPersonas(PersonaRepositorio repositorio,
                                             BuscadorDuplicados buscadorDuplicados,
                                             EventoPublicador eventoPublicador,
                                             RelojDominio reloj,
                                             GeneradorAltKey generadorAltKey) {
        return new ServicioPersonas(repositorio, buscadorDuplicados, eventoPublicador, reloj, generadorAltKey);
    }
}
