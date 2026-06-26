package cl.smid.instituciones.api;

import cl.smid.instituciones.api.dto.ActivacionInstitucionRequest;
import cl.smid.instituciones.api.dto.CrearInstitucionRequest;
import cl.smid.instituciones.api.dto.EditarInstitucionRequest;
import cl.smid.instituciones.api.dto.InstitucionDTO;
import cl.smid.instituciones.api.dto.InstitucionResumenDTO;
import cl.smid.instituciones.api.dto.PaginaDTO;
import cl.smid.instituciones.dominio.modelo.Ambito;
import cl.smid.instituciones.dominio.modelo.ContextoSesion;
import cl.smid.instituciones.dominio.modelo.FiltroInstituciones;
import cl.smid.instituciones.dominio.modelo.Paginado;
import cl.smid.instituciones.dominio.modelo.Rut;
import cl.smid.instituciones.dominio.puerto.entrada.GestionInstitucionesUseCase;
import cl.smid.instituciones.dominio.puerto.entrada.comando.DatosInstitucion;
import cl.smid.instituciones.infraestructura.seguridad.ProveedorContexto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * API REST de instituciones. Soporta búsqueda por RUT vía {@code GET /instituciones?rut=}
 * y filtros por tipo, ámbito, texto, región y estado.
 */
@RestController
@RequestMapping("/instituciones")
public class ControladorInstituciones {

    private final GestionInstitucionesUseCase gestionInstituciones;
    private final EnsambladorRespuestas ensamblador;
    private final ProveedorContexto proveedorContexto;

    public ControladorInstituciones(GestionInstitucionesUseCase gestionInstituciones,
                                    EnsambladorRespuestas ensamblador,
                                    ProveedorContexto proveedorContexto) {
        this.gestionInstituciones = gestionInstituciones;
        this.ensamblador = ensamblador;
        this.proveedorContexto = proveedorContexto;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<InstitucionDTO> crear(@Valid @RequestBody CrearInstitucionRequest solicitud) {
        ContextoSesion contexto = proveedorContexto.contextoActual();
        InstitucionDTO dto = ensamblador.aDto(gestionInstituciones.crearInstitucion(contexto, aDatos(solicitud)));
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping("/{altKey}")
    public InstitucionDTO obtener(@PathVariable String altKey) {
        return ensamblador.aDto(gestionInstituciones.obtenerInstitucion(altKey));
    }

    @PutMapping("/{altKey}")
    @Transactional
    public InstitucionDTO editar(@PathVariable String altKey,
                                 @Valid @RequestBody EditarInstitucionRequest solicitud) {
        ContextoSesion contexto = proveedorContexto.contextoActual();
        return ensamblador.aDto(gestionInstituciones.editarInstitucion(contexto, altKey, aDatos(solicitud)));
    }

    @PostMapping("/{altKey}/activacion")
    @Transactional
    public InstitucionDTO cambiarActivacion(@PathVariable String altKey,
                                            @Valid @RequestBody ActivacionInstitucionRequest solicitud) {
        ContextoSesion contexto = proveedorContexto.contextoActual();
        return ensamblador.aDto(
                gestionInstituciones.cambiarActivacionInstitucion(contexto, altKey, solicitud.activa()));
    }

    @GetMapping
    public PaginaDTO<InstitucionResumenDTO> listar(
            @RequestParam(required = false) String tipoAlt,
            @RequestParam(required = false) String ambito,
            @RequestParam(required = false) String texto,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) Boolean activa,
            @RequestParam(required = false) String rut,
            @RequestParam(required = false) Integer pagina,
            @RequestParam(required = false) Integer tamano) {
        Ambito ambitoFiltro = ambito == null || ambito.isBlank() ? null : Ambito.desde(ambito);
        Rut rutFiltro = rut == null || rut.isBlank() ? null : Rut.desde(rut);
        FiltroInstituciones filtro = new FiltroInstituciones(
                tipoAlt, ambitoFiltro, texto, region, activa, rutFiltro);
        return ensamblador.paginaInstituciones(
                gestionInstituciones.listarInstituciones(filtro, Paginado.de(pagina, tamano)));
    }

    private DatosInstitucion aDatos(CrearInstitucionRequest s) {
        return new DatosInstitucion(s.codigo(), s.nombre(), s.tipoAlt(), s.rut(),
                s.regionCodigo(), s.comunaCodigo(), s.direccion(), s.telefono(), s.email(), s.sitioWeb());
    }

    private DatosInstitucion aDatos(EditarInstitucionRequest s) {
        return new DatosInstitucion(s.codigo(), s.nombre(), s.tipoAlt(), s.rut(),
                s.regionCodigo(), s.comunaCodigo(), s.direccion(), s.telefono(), s.email(), s.sitioWeb());
    }
}
