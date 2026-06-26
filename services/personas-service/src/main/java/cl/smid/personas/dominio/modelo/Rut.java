package cl.smid.personas.dominio.modelo;

import cl.smid.personas.dominio.excepcion.RutInvalidoException;

/**
 * Objeto de valor que representa un RUT chileno <b>ya normalizado y validado</b> por el
 * algoritmo de módulo 11 (Núcleo 5.5).
 *
 * <p>Formato canónico: sin puntos, con guion y dígito verificador en mayúscula
 * (p. ej. {@code "12345678-9"} o {@code "5126663-K"}). El RUT es nulable a nivel de
 * persona: este objeto solo se construye cuando el RUT está presente.</p>
 *
 * <p>La construcción se hace mediante {@link #normalizarYValidar(String)}, que lanza
 * {@link RutInvalidoException} (código {@code PER-002}) si el RUT no supera la verificación.</p>
 *
 * @param cuerpo   parte numérica sin dígito verificador (p. ej. {@code "12345678"})
 * @param dv       dígito verificador en mayúscula ({@code '0'..'9'} o {@code 'K'})
 * @param canonico forma canónica completa {@code cuerpo + "-" + dv}
 */
public record Rut(String cuerpo, char dv, String canonico) {

    /** Longitud máxima del cuerpo aceptada (rango institucional chileno). */
    private static final int MAX_DIGITOS_CUERPO = 8;

    /**
     * Normaliza la entrada y valida su dígito verificador por módulo 11.
     *
     * @param entrada RUT en cualquier formato de captura (con o sin puntos/guion)
     * @return el RUT normalizado y validado
     * @throws RutInvalidoException si el formato es inválido o el DV no corresponde
     */
    public static Rut normalizarYValidar(String entrada) {
        if (entrada == null || entrada.isBlank()) {
            throw new RutInvalidoException("El RUT no puede estar vacío.");
        }

        // 1) Saneamiento: se eliminan puntos, espacios y guion; se pasa a mayúscula.
        //    El dígito verificador es, por convención, siempre el último carácter.
        String limpio = entrada.replaceAll("[.\\s\\-]", "").toUpperCase();

        if (limpio.length() < 2) {
            throw new RutInvalidoException("El RUT '" + entrada + "' es demasiado corto.");
        }

        String cuerpo = limpio.substring(0, limpio.length() - 1);
        char dvEntrada = limpio.charAt(limpio.length() - 1);

        // 2) Validación estructural: cuerpo numérico de 1..8 dígitos; DV dígito o 'K'.
        if (!cuerpo.matches("\\d{1," + MAX_DIGITOS_CUERPO + "}")) {
            throw new RutInvalidoException("El cuerpo del RUT '" + entrada + "' no es numérico válido.");
        }
        if (!(Character.isDigit(dvEntrada) || dvEntrada == 'K')) {
            throw new RutInvalidoException("El dígito verificador del RUT '" + entrada + "' es inválido.");
        }

        // 3) Verificación por módulo 11.
        char dvEsperado = calcularDigitoVerificador(cuerpo);
        if (dvEsperado != dvEntrada) {
            throw new RutInvalidoException(
                    "El RUT '" + entrada + "' no supera la validación de dígito verificador (módulo 11).");
        }

        return new Rut(cuerpo, dvEntrada, cuerpo + "-" + dvEntrada);
    }

    /**
     * Calcula el dígito verificador de un cuerpo numérico mediante módulo 11.
     * Recorre el cuerpo de derecha a izquierda multiplicando por la serie cíclica
     * 2,3,4,5,6,7. Resultado: {@code '0'..'9'} o {@code 'K'} (cuando el resto es 10).
     *
     * @param cuerpo cuerpo numérico (solo dígitos, ya validado)
     * @return el dígito verificador esperado
     */
    public static char calcularDigitoVerificador(String cuerpo) {
        int suma = 0;
        int multiplicador = 2;
        for (int i = cuerpo.length() - 1; i >= 0; i--) {
            suma += (cuerpo.charAt(i) - '0') * multiplicador;
            multiplicador = (multiplicador == 7) ? 2 : multiplicador + 1;
        }
        int resto = 11 - (suma % 11);
        if (resto == 11) {
            return '0';
        }
        if (resto == 10) {
            return 'K';
        }
        return (char) ('0' + resto);
    }
}
