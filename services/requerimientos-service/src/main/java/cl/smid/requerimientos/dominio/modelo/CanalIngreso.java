package cl.smid.requerimientos.dominio.modelo;

/**
 * Canal por el cual ingresa el requerimiento (Núcleo 6.2).
 *
 * <p>Es nulable en {@code BORRADOR} (flexibilidad de ingreso) y obligatorio para
 * transitar a {@code INGRESADO}.</p>
 */
public enum CanalIngreso {
    /** Ingreso a través del formulario web. */
    WEB,
    /** Ingreso presencial registrado por un funcionario. */
    PRESENCIAL
}
