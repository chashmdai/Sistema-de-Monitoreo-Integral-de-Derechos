package cl.smid.esnna.service;

import cl.smid.esnna.domain.EstadoJob;

/**
 * Canal de progreso del pipeline hacia el job. El motor lo invoca desde sus
 * propios hilos; las implementaciones deben ser thread-safe (ProcesoJob usa
 * campos volatile).
 */
public interface ProgressListener {

    ProgressListener NOOP = new ProgressListener() {
        @Override
        public void onFase(EstadoJob fase) {
        }

        @Override
        public void onAvanceMap(int procesados, int total) {
        }
    };

    void onFase(EstadoJob fase);

    void onAvanceMap(int procesados, int total);
}