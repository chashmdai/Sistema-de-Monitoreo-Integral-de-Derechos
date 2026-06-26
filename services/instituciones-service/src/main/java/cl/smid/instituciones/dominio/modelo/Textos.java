package cl.smid.instituciones.dominio.modelo;

import java.util.regex.Pattern;

/**
 * Utilidades de texto del dominio: normalización de nombres para unicidad,
 * limpieza de campos opcionales y validación ligera de correo electrónico.
 *
 * <p>La unicidad <em>insensible a mayúsculas y acentos</em> de los nombres la
 * garantiza la collation de la base ({@code utf8mb4_0900_ai_ci}); aquí solo se
 * normaliza el formato (recorte y colapso de espacios) para almacenar valores
 * consistentes.</p>
 */
public final class Textos {

    /** Patrón pragmático de correo: {@code local@dominio.tld}. */
    private static final Pattern EMAIL = Pattern.compile(
            "^[\\w.+%-]+@[\\w.-]+\\.[A-Za-z]{2,}$");

    private static final Pattern ESPACIOS = Pattern.compile("\\s+");

    private Textos() {
    }

    /**
     * Normaliza un nombre obligatorio: recorta extremos y colapsa secuencias de
     * espacios internos a uno solo.
     *
     * @param valor texto de entrada (se asume no nulo; el llamador valida obligatoriedad)
     * @return el nombre normalizado
     */
    public static String normalizarNombre(String valor) {
        return ESPACIOS.matcher(valor.trim()).replaceAll(" ");
    }

    /**
     * Limpia un campo opcional: recorta extremos y devuelve {@code null} si queda vacío.
     *
     * @param valor texto de entrada (puede ser nulo)
     * @return el texto recortado o {@code null}
     */
    public static String limpiarOpcional(String valor) {
        if (valor == null) {
            return null;
        }
        String limpio = valor.trim();
        return limpio.isEmpty() ? null : limpio;
    }

    /**
     * Indica si un texto tiene formato de correo electrónico válido.
     *
     * @param valor texto a evaluar (no nulo)
     * @return {@code true} si cumple el patrón de correo
     */
    public static boolean esEmailValido(String valor) {
        return EMAIL.matcher(valor).matches();
    }

    /**
     * Indica si un texto es nulo o está compuesto solo por espacios.
     *
     * @param valor texto a evaluar
     * @return {@code true} si es nulo o en blanco
     */
    public static boolean esVacio(String valor) {
        return valor == null || valor.isBlank();
    }
}
