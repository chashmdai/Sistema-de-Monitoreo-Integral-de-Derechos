package cl.smid.esnna.util;

import cl.smid.esnna.exception.EsnnaProcessingException;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;

/**
 * Identificador determinista de un lote de documentos: sha256 del join ordenado
 * de los sha256 individuales (orden-independiente: el mismo conjunto de PDFs
 * produce el mismo id sin importar el orden de carga).
 *
 * Extraído de EsnnaMotorService para que el loteId calculado en el submit del
 * job (sobre bytes, antes de extraer texto) sea LA MISMA clave que usa el
 * DraftStore: idempotencia de jobs activos y caché de resultados comparten
 * identidad. Compatible con el draftId histórico (mismo algoritmo, mismos
 * hashes de archivo).
 */
public final class LoteHasher {

    private LoteHasher() {
    }

    public static String hash(List<String> sha256Documentos) {
        try {
            List<String> hashes = new ArrayList<>(sha256Documentos);
            hashes.sort(String::compareTo);
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(md.digest(String.join("|", hashes).getBytes()));
        } catch (Exception e) {
            throw new EsnnaProcessingException("No se pudo calcular el identificador del lote.", e);
        }
    }

    public static String sha256(byte[] bytes) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(md.digest(bytes));
        } catch (Exception e) {
            throw new EsnnaProcessingException("No se pudo calcular el hash del documento.", e);
        }
    }
}