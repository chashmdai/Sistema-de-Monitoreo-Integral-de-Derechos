package cl.smid.instituciones.api;

import cl.smid.instituciones.api.dto.ActivacionPuntoFocalRequest;
import cl.smid.instituciones.api.dto.CrearPuntoFocalRequest;
import cl.smid.instituciones.api.dto.EditarPuntoFocalRequest;
import cl.smid.instituciones.api.dto.PuntoFocalDTO;
import cl.smid.instituciones.dominio.modelo.ContextoSesion;
import cl.smid.instituciones.dominio.puerto.entrada.GestionPuntosFocalesUseCase;
import cl.smid.instituciones.dominio.puerto.entrada.comando.DatosPuntoFocal;
import cl.smid.instituciones.infraestructura.seguridad.ProveedorContexto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * API REST de puntos focales de una institución. La creación se hace bajo el recurso de
 * la institución; la edición y el cambio de estado operan por el alt_key del propio
 * punto focal. La lectura se obtiene a través del detalle de la institución.
 */
@RestController
@RequestMapping("/instituciones")
public class ControladorPuntosFocales {

    private final GestionPuntosFocalesUseCase gestionPuntosFocales;
    private final EnsambladorRespuestas ensamblador;
    private final ProveedorContexto proveedorContexto;

    public ControladorPuntosFocales(GestionPuntosFocalesUseCase gestionPuntosFocales,
                                    EnsambladorRespuestas ensamblador,
                                    ProveedorContexto proveedorContexto) {
        this.gestionPuntosFocales = gestionPuntosFocales;
        this.ensamblador = ensamblador;
        this.proveedorContexto = proveedorContexto;
    }

    @PostMapping("/{institucionAlt}/puntos-focales")
    @Transactional
    public ResponseEntity<PuntoFocalDTO> crear(@PathVariable String institucionAlt,
                                               @Valid @RequestBody CrearPuntoFocalRequest solicitud) {
        ContextoSesion contexto = proveedorContexto.contextoActual();
        DatosPuntoFocal datos = new DatosPuntoFocal(solicitud.nombre(), solicitud.cargo(),
                solicitud.email(), solicitud.telefono(), solicitud.principal());
        PuntoFocalDTO dto = ensamblador.aDto(
                gestionPuntosFocales.crearPuntoFocal(contexto, institucionAlt, datos));
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PutMapping("/puntos-focales/{altKey}")
    @Transactional
    public PuntoFocalDTO editar(@PathVariable String altKey,
                                @Valid @RequestBody EditarPuntoFocalRequest solicitud) {
        ContextoSesion contexto = proveedorContexto.contextoActual();
        DatosPuntoFocal datos = new DatosPuntoFocal(solicitud.nombre(), solicitud.cargo(),
                solicitud.email(), solicitud.telefono(), solicitud.principal());
        return ensamblador.aDto(gestionPuntosFocales.editarPuntoFocal(contexto, altKey, datos));
    }

    @PostMapping("/puntos-focales/{altKey}/activacion")
    @Transactional
    public PuntoFocalDTO cambiarActivacion(@PathVariable String altKey,
                                           @Valid @RequestBody ActivacionPuntoFocalRequest solicitud) {
        ContextoSesion contexto = proveedorContexto.contextoActual();
        return ensamblador.aDto(
                gestionPuntosFocales.cambiarActivacionPuntoFocal(contexto, altKey, solicitud.activo()));
    }
}
