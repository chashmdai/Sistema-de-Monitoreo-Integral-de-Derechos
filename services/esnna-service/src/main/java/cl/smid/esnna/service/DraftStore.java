package cl.smid.esnna.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;

/**
 * Cache en memoria de borradores de análisis, con expiración. La clave es el hash
 * del lote de documentos (draftId), de modo que reprocesar el mismo lote devuelve
 * el resultado cacheado en vez de re-pagar gpt-5.5 en 'high'.
 *
 * In-memory: los borradores no sobreviven a un reinicio (aceptable para un
 * borrador). El caso definitivo se persiste vía /guardar.
 */
@Component
public class DraftStore {

    private final Cache<String, DraftAnalisis> cache;

    public DraftStore(@Value("${esnna.draft.ttl-minutos:120}") long ttlMinutos,
                      @Value("${esnna.draft.max-entradas:500}") long maxEntradas) {
        this.cache = Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofMinutes(ttlMinutos))
                .maximumSize(maxEntradas)
                .build();
    }

    public Optional<DraftAnalisis> get(String draftId) {
        return Optional.ofNullable(cache.getIfPresent(draftId));
    }

    public void put(String draftId, DraftAnalisis draft) {
        cache.put(draftId, draft);
    }

    public void invalidar(String draftId) {
        cache.invalidate(draftId);
    }
}
