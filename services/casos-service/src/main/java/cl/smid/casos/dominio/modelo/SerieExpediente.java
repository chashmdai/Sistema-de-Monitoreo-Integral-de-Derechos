package cl.smid.casos.dominio.modelo;

/**
 * Serie de numeración del expediente. Igual que en requerimientos, la serie BETA (marcha blanca)
 * se mantiene <strong>aislada</strong> de la serie OFICIAL para no contaminar la numeración oficial.
 *
 * <p>La serie de un Caso se hereda del requerimiento de origen: un requerimiento marcado
 * {@code esBeta} engendra un Caso en serie BETA.</p>
 */
public enum SerieExpediente {
    OFICIAL,
    BETA
}
