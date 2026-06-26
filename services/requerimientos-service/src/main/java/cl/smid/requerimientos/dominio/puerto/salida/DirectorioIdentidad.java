package cl.smid.requerimientos.dominio.puerto.salida;

import java.util.Optional;

/**
 * Puerto de salida hacia Identidad (6.1). Valida que el profesional al que se asigna un
 * requerimiento pertenezca a la unidad de destino (USR.02).
 *
 * <p>Costura documentada: el endpoint de Identidad aún no está publicado de forma estable. La
 * política de verificación es conmutable ({@code smid.integracion.verificacion-profesional}):
 * en modo {@code permisiva} se tolera la indisponibilidad (no bloquea la asignación); en modo
 * {@code estricta} se exige resolver al profesional y comparar su unidad.</p>
 */
public interface DirectorioIdentidad {

    /**
     * Resuelve un profesional por su alt_key.
     *
     * @param altKey alt_key del profesional
     * @return la proyección del profesional, o vacío si no se pudo resolver
     */
    Optional<ProfesionalIdentidad> resolverProfesional(String altKey);
}
