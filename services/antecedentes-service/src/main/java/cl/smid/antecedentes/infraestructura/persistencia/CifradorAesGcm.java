package cl.smid.antecedentes.infraestructura.persistencia;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Cifrado de campos reservados con <strong>AES-256-GCM</strong> (DT-1):
 *
 * <ul>
 *   <li>Clave de 32 bytes desde {@code ANTECEDENTES_CIFRADO_CLAVE} (Base64).</li>
 *   <li>IV aleatorio de 12 bytes por valor (nunca reutilizado).</li>
 *   <li>Tag de autenticacion de 128 bits.</li>
 *   <li>Formato persistido: {@code Base64(iv || ciphertext || tag)}. En Java, el bloque
 *       {@code ciphertext || tag} es justamente la salida de {@code Cipher.doFinal}.</li>
 * </ul>
 *
 * Activa por defecto ({@code smid.cifrado.relato=aes-gcm}, {@code matchIfMissing=true}). El
 * relato nunca se indexa ni se registra en logs.
 */
@Component
@ConditionalOnProperty(prefix = "smid.cifrado", name = "relato", havingValue = "aes-gcm", matchIfMissing = true)
public class CifradorAesGcm implements CifradorCampos {

    private static final String TRANSFORMACION = "AES/GCM/NoPadding";
    private static final int LONGITUD_IV = 12;        // bytes
    private static final int LONGITUD_TAG_BITS = 128; // bits
    private static final int LONGITUD_CLAVE = 32;     // bytes (AES-256)

    private final SecretKey clave;
    private final SecureRandom aleatorio = new SecureRandom();

    public CifradorAesGcm(org.springframework.core.env.Environment entorno) {
        String claveBase64 = entorno.getProperty("ANTECEDENTES_CIFRADO_CLAVE");
        if (claveBase64 == null || claveBase64.isBlank()) {
            throw new IllegalStateException(
                    "ANTECEDENTES_CIFRADO_CLAVE es obligatoria cuando smid.cifrado.relato=aes-gcm "
                            + "(clave AES-256 en Base64, 32 bytes).");
        }
        byte[] bytes;
        try {
            bytes = Base64.getDecoder().decode(claveBase64.trim());
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("ANTECEDENTES_CIFRADO_CLAVE no es Base64 valido.", e);
        }
        if (bytes.length != LONGITUD_CLAVE) {
            throw new IllegalStateException(
                    "ANTECEDENTES_CIFRADO_CLAVE debe decodificar a 32 bytes (AES-256); recibidos: " + bytes.length);
        }
        this.clave = new SecretKeySpec(bytes, "AES");
    }

    @Override
    public String cifrar(String textoPlano) {
        if (textoPlano == null) {
            return null;
        }
        try {
            byte[] iv = new byte[LONGITUD_IV];
            aleatorio.nextBytes(iv);
            Cipher cipher = Cipher.getInstance(TRANSFORMACION);
            cipher.init(Cipher.ENCRYPT_MODE, clave, new GCMParameterSpec(LONGITUD_TAG_BITS, iv));
            byte[] cifradoConTag = cipher.doFinal(textoPlano.getBytes(StandardCharsets.UTF_8));

            byte[] salida = new byte[iv.length + cifradoConTag.length];
            System.arraycopy(iv, 0, salida, 0, iv.length);
            System.arraycopy(cifradoConTag, 0, salida, iv.length, cifradoConTag.length);
            return Base64.getEncoder().encodeToString(salida);
        } catch (Exception e) {
            throw new IllegalStateException("Error al cifrar el campo reservado.", e);
        }
    }

    @Override
    public String descifrar(String almacenado) {
        if (almacenado == null) {
            return null;
        }
        try {
            byte[] datos = Base64.getDecoder().decode(almacenado);
            if (datos.length <= LONGITUD_IV) {
                throw new IllegalStateException("El dato cifrado es demasiado corto para contener IV y tag.");
            }
            byte[] iv = new byte[LONGITUD_IV];
            System.arraycopy(datos, 0, iv, 0, LONGITUD_IV);
            byte[] cifradoConTag = new byte[datos.length - LONGITUD_IV];
            System.arraycopy(datos, LONGITUD_IV, cifradoConTag, 0, cifradoConTag.length);

            Cipher cipher = Cipher.getInstance(TRANSFORMACION);
            cipher.init(Cipher.DECRYPT_MODE, clave, new GCMParameterSpec(LONGITUD_TAG_BITS, iv));
            byte[] plano = cipher.doFinal(cifradoConTag);
            return new String(plano, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new IllegalStateException("Error al descifrar el campo reservado.", e);
        }
    }
}
