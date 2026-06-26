package cl.smid.instituciones.dominio.modelo;

import cl.smid.instituciones.dominio.excepcion.ReglaNegocioException;

import java.util.Objects;

/**
 * Objeto de valor que representa un RUT chileno ya validado por <strong>módulo 11</strong>.
 *
 * <p>Reutiliza exactamente la lógica del patrón Personas (6.2): limpieza de la cadena,
 * cómputo del dígito verificador con la serie 2..7 y reglas {@code resto==11 -> '0'} y
 * {@code resto==10 -> 'K'}. El RUT es <strong>opcional</strong> a nivel de institución;
 * por eso esta clase solo se construye cuando hay un valor presente. Un RUT
 * estructuralmente inválido es un error de regla de negocio (INS-422).</p>
 *
 * <p>Inmutable. {@code cuerpo} almacena solo los dígitos (sin puntos ni guion) y
 * {@code dv} el dígito verificador en mayúscula ('0'..'9' o 'K').</p>
 */
public final class Rut {

    private final String cuerpo;
    private final char dv;

    private Rut(String cuerpo, char dv) {
        this.cuerpo = cuerpo;
        this.dv = dv;
    }

    /**
     * Construye y valida un RUT a partir de un texto en cualquier formato usual
     * ({@code 12.345.678-5}, {@code 12345678-5} o {@code 123456785}).
     *
     * @param entrada texto del RUT (no nulo ni en blanco)
     * @return el RUT validado
     * @throws ReglaNegocioException si el RUT no supera la validación de módulo 11
     */
    public static Rut desde(String entrada) {
        if (entrada == null || entrada.isBlank()) {
            throw new ReglaNegocioException("El RUT no puede estar vacío.");
        }
        // Limpieza: quitar puntos, guiones y espacios; normalizar a mayúscula (para la 'K').
        String limpio = entrada.trim().toUpperCase()
                .replace(".", "")
                .replace("-", "")
                .replace(" ", "");

        if (limpio.length() < 2) {
            throw new ReglaNegocioException("El RUT '" + entrada + "' es demasiado corto para ser válido.");
        }

        String cuerpo = limpio.substring(0, limpio.length() - 1);
        char dvIngresado = limpio.charAt(limpio.length() - 1);

        if (!esCuerpoNumerico(cuerpo)) {
            throw new ReglaNegocioException("El RUT '" + entrada + "' contiene caracteres no numéricos en el cuerpo.");
        }
        if (!(Character.isDigit(dvIngresado) || dvIngresado == 'K')) {
            throw new ReglaNegocioException("El dígito verificador del RUT '" + entrada + "' es inválido.");
        }

        char dvEsperado = calcularDigitoVerificador(cuerpo);
        if (dvEsperado != dvIngresado) {
            throw new ReglaNegocioException(
                "El RUT '" + entrada + "' no supera la validación de dígito verificador (módulo 11).");
        }
        return new Rut(cuerpo, dvIngresado);
    }

    /**
     * Reconstruye un RUT desde persistencia <strong>sin re-validar</strong>: los datos
     * almacenados ya superaron la validación de módulo 11 al escribirse. Evita que una
     * lectura pueda fallar por validación.
     *
     * @param cuerpo dígitos del cuerpo ya almacenados
     * @param dv     dígito verificador ya almacenado
     * @return el RUT reconstruido
     */
    public static Rut reconstruir(String cuerpo, char dv) {
        return new Rut(cuerpo, Character.toUpperCase(dv));
    }

    private static boolean esCuerpoNumerico(String cuerpo) {
        if (cuerpo.isEmpty()) {
            return false;
        }
        for (int i = 0; i < cuerpo.length(); i++) {
            if (!Character.isDigit(cuerpo.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Cómputo del dígito verificador por módulo 11 recorriendo el cuerpo de
     * derecha a izquierda con la serie de factores 2, 3, 4, 5, 6, 7 (cíclica).
     */
    private static char calcularDigitoVerificador(String cuerpo) {
        int suma = 0;
        int factor = 2;
        for (int i = cuerpo.length() - 1; i >= 0; i--) {
            suma += (cuerpo.charAt(i) - '0') * factor;
            factor = (factor == 7) ? 2 : factor + 1;
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

    /** @return solo los dígitos del cuerpo (sin puntos, guion ni dígito verificador). */
    public String cuerpo() {
        return cuerpo;
    }

    /** @return el dígito verificador en mayúscula ('0'..'9' o 'K'). */
    public char dv() {
        return dv;
    }

    /** @return el RUT en formato canónico {@code cuerpo-dv} (por ejemplo {@code 12345678-5}). */
    public String canonico() {
        return cuerpo + "-" + dv;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Rut otro)) {
            return false;
        }
        return dv == otro.dv && cuerpo.equals(otro.cuerpo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cuerpo, dv);
    }

    @Override
    public String toString() {
        return canonico();
    }
}
