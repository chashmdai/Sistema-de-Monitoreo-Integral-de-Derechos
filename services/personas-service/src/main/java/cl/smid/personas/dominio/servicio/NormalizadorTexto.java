package cl.smid.personas.dominio.servicio;

import java.text.Normalizer;
import java.util.Locale;

/**
 * Utilidades puras de normalización de texto para la deduplicación y la búsqueda difusa
 * (Núcleo 5.5). Sin estado y sin dependencias de framework.
 *
 * <p>La normalización canónica: descompone tildes (NFD) y elimina los diacríticos, preserva la
 * letra ñ, pasa a minúsculas con {@link Locale#ROOT}, colapsa espacios internos múltiples a uno y
 * recorta los extremos. Así "José  PÉREZ" y "jose perez" convergen a la misma forma comparable.</p>
 */
public final class NormalizadorTexto {

    private NormalizadorTexto() {
        // Clase de utilidades: no instanciable.
    }

    /**
     * Normaliza un texto a su forma comparable (sin acentos, en minúscula, espacios
     * colapsados). Devuelve cadena vacía si la entrada es nula o en blanco.
     */
    public static String normalizar(String texto) {
        if (texto == null) {
            return "";
        }
        String textoConEnieProtegida = texto
                .replace('ñ', '\uE000')
                .replace('Ñ', '\uE001');
        String sinAcentos = Normalizer.normalize(textoConEnieProtegida, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        String conEnieRestaurada = sinAcentos
                .replace('\uE000', 'ñ')
                .replace('\uE001', 'Ñ');
        return conEnieRestaurada.toLowerCase(Locale.ROOT)
                .replaceAll("\\s+", " ")
                .trim();
    }

    /**
     * Variante que, además de normalizar, elimina todo lo que no sea letra o dígito (deja sólo
     * caracteres alfanuméricos sin separadores). Útil para construir claves de comparación
     * estables frente a signos de puntuación.
     */
    public static String normalizarAlfanumerico(String texto) {
        String base = normalizar(texto);
        return base.replaceAll("[^a-z0-9ñ]", "");
    }
}
