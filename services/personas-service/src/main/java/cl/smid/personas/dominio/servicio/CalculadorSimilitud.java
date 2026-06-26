package cl.smid.personas.dominio.servicio;

/**
 * Cálculo puro de similitud textual mediante el algoritmo <b>Jaro-Winkler</b>, usado en la
 * deduplicación difusa por nombre (Núcleo 5.5). Devuelve un valor en {@code [0,1]}: 1.0 es
 * identidad y 0.0 es disparidad total.
 *
 * <p>Jaro-Winkler favorece a las cadenas que comparten prefijo, lo que se ajusta bien a
 * nombres con errores de tipeo al final o variaciones de apellidos. La entrada debe venir ya
 * normalizada (ver {@link NormalizadorTexto}).</p>
 */
public final class CalculadorSimilitud {

    /** Factor de escalado del prefijo común (valor estándar de Winkler). */
    private static final double FACTOR_PREFIJO = 0.1;

    /** Largo máximo de prefijo común considerado (estándar de Winkler). */
    private static final int MAX_PREFIJO = 4;

    private CalculadorSimilitud() {
        // Clase de utilidades: no instanciable.
    }

    /**
     * Similitud Jaro-Winkler entre dos cadenas.
     *
     * @param a primera cadena (ya normalizada)
     * @param b segunda cadena (ya normalizada)
     * @return similitud en {@code [0,1]}
     */
    public static double similitud(String a, String b) {
        if (a == null) {
            a = "";
        }
        if (b == null) {
            b = "";
        }
        if (a.equals(b)) {
            return a.isEmpty() ? 0.0 : 1.0;
        }
        if (a.isEmpty() || b.isEmpty()) {
            return 0.0;
        }

        double jaro = distanciaJaro(a, b);

        // Realce de Winkler por prefijo común (hasta 4 caracteres).
        int prefijo = 0;
        int limite = Math.min(MAX_PREFIJO, Math.min(a.length(), b.length()));
        for (int i = 0; i < limite; i++) {
            if (a.charAt(i) == b.charAt(i)) {
                prefijo++;
            } else {
                break;
            }
        }
        return jaro + (prefijo * FACTOR_PREFIJO * (1.0 - jaro));
    }

    /**
     * Distancia de Jaro base entre dos cadenas no vacías.
     */
    private static double distanciaJaro(String a, String b) {
        int largoA = a.length();
        int largoB = b.length();

        // Ventana de coincidencia: floor(max(|a|,|b|)/2) - 1.
        int ventana = Math.max(largoA, largoB) / 2 - 1;
        if (ventana < 0) {
            ventana = 0;
        }

        boolean[] coincideA = new boolean[largoA];
        boolean[] coincideB = new boolean[largoB];

        // 1) Conteo de coincidencias dentro de la ventana.
        int coincidencias = 0;
        for (int i = 0; i < largoA; i++) {
            int inicio = Math.max(0, i - ventana);
            int fin = Math.min(i + ventana + 1, largoB);
            for (int j = inicio; j < fin; j++) {
                if (coincideB[j]) {
                    continue;
                }
                if (a.charAt(i) != b.charAt(j)) {
                    continue;
                }
                coincideA[i] = true;
                coincideB[j] = true;
                coincidencias++;
                break;
            }
        }

        if (coincidencias == 0) {
            return 0.0;
        }

        // 2) Conteo de transposiciones (mitad de los desórdenes entre coincidencias).
        int transposiciones = 0;
        int k = 0;
        for (int i = 0; i < largoA; i++) {
            if (!coincideA[i]) {
                continue;
            }
            while (!coincideB[k]) {
                k++;
            }
            if (a.charAt(i) != b.charAt(k)) {
                transposiciones++;
            }
            k++;
        }
        double t = transposiciones / 2.0;
        double m = coincidencias;

        return (m / largoA + m / largoB + (m - t) / m) / 3.0;
    }
}
