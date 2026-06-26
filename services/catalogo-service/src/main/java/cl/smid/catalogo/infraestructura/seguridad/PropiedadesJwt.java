package cl.smid.catalogo.infraestructura.seguridad;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Propiedades del JWT del ecosistema (prefijo {@code smid.jwt}), alineadas con el contrato
 * 2.4 del Núcleo y con el servicio de autenticación.
 *
 * <p>El servicio valida los tokens de forma <b>independiente</b> (defensa en profundidad,
 * DT-3): aunque el Gateway ya haya validado, cada servicio vuelve a verificar firma, emisor,
 * audiencia y expiración.</p>
 *
 * <h2>Rotación de claves</h2>
 * <p>Para no invalidar tokens en vuelo durante una rotación, se admiten dos claves: la
 * <b>activa</b> (obligatoria) y la <b>previa</b> (opcional). El validador selecciona la clave
 * de verificación según el {@code kid} de la cabecera del token, de modo que ambas conviven
 * durante la ventana de rotación. Modelarlo como dos pares {@code kid/secreto} (en lugar de un
 * mapa con clave dinámica) lo hace directo de configurar por variables de entorno.</p>
 *
 * <p><b>Formato del secreto:</b> se interpreta como bytes UTF-8 crudos para HS256 (mínimo 32
 * bytes). El mismo {@code kid} y secreto deben estar configurados en el emisor (auth) para que
 * las firmas validen.</p>
 */
@ConfigurationProperties(prefix = "smid.jwt")
@Getter
@Setter
public class PropiedadesJwt {

    /** Identificador (kid) de la clave activa. */
    private String kidActivo;

    /** Secreto de la clave activa (bytes UTF-8, ≥ 32). */
    private String secretoActivo;

    /** Identificador (kid) de la clave previa, durante una rotación (opcional). */
    private String kidPrevio;

    /** Secreto de la clave previa (opcional, solo en rotación). */
    private String secretoPrevio;

    /** Emisor esperado del token (claim {@code iss}); en el ecosistema, {@code smid-auth}. */
    private String issuer;

    /** Audiencia que debe contener el token (claim {@code aud}); en el ecosistema, {@code smid-servicios}. */
    private String audience;

    /** {@code true} si hay una clave previa configurada (kid y secreto no vacíos). */
    public boolean tieneClavePrevia() {
        return kidPrevio != null && !kidPrevio.isBlank()
                && secretoPrevio != null && !secretoPrevio.isBlank();
    }
}
