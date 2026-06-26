package cl.smid.requerimientos.dominio.puerto.salida;

import java.util.Optional;

/**
 * Puerto de salida hacia personas-service (6.2). El dominio resuelve referencias por alt_key
 * (requirente y NNA) para tomar snapshots de resiliencia y verificar que la persona existe y
 * es visible para el usuario. El dominio no conoce HTTP; la infraestructura propaga el JWT del
 * usuario para que Personas aplique su propio filtro territorial.
 */
public interface DirectorioPersonas {

    /**
     * Resuelve una persona por su alt_key contra personas-service.
     *
     * @param altKey alt_key de la persona
     * @return la proyección mínima de la persona, o vacío si no existe o está fuera de alcance
     */
    Optional<PersonaResuelta> resolver(String altKey);
}
