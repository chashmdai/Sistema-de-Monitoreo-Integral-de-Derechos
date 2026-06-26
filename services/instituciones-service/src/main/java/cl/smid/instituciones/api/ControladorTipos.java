package cl.smid.instituciones.api;

import cl.smid.instituciones.api.dto.ActivacionTipoRequest;
import cl.smid.instituciones.api.dto.CrearTipoRequest;
import cl.smid.instituciones.api.dto.EditarTipoRequest;
import cl.smid.instituciones.api.dto.PaginaDTO;
import cl.smid.instituciones.api.dto.TipoInstitucionDTO;
import cl.smid.instituciones.dominio.modelo.Ambito;
import cl.smid.instituciones.dominio.modelo.ContextoSesion;
import cl.smid.instituciones.dominio.modelo.FiltroTipos;
import cl.smid.instituciones.dominio.modelo.Paginado;
import cl.smid.instituciones.dominio.puerto.entrada.GestionTiposUseCase;
import cl.smid.instituciones.dominio.puerto.entrada.comando.DatosTipo;
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
 * API REST de tipos de institución. Las lecturas son públicas autenticadas; las
 * escrituras exigen rol administrador (lo resuelve el dominio) y abren el límite
 * transaccional (override #7).
 */
@RestController
@RequestMapping("/tipos")
public class ControladorTipos {

    private final GestionTiposUseCase gestionTipos;
    private final EnsambladorRespuestas ensamblador;
    private final ProveedorContexto proveedorContexto;

    public ControladorTipos(GestionTiposUseCase gestionTipos, EnsambladorRespuestas ensamblador,
                            ProveedorContexto proveedorContexto) {
        this.gestionTipos = gestionTipos;
        this.ensamblador = ensamblador;
        this.proveedorContexto = proveedorContexto;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<TipoInstitucionDTO> crear(@Valid @RequestBody CrearTipoRequest solicitud) {
        ContextoSesion contexto = proveedorContexto.contextoActual();
        DatosTipo datos = new DatosTipo(solicitud.nombre(), solicitud.ambito(), solicitud.descripcion());
        TipoInstitucionDTO dto = ensamblador.aDto(gestionTipos.crearTipo(contexto, datos));
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping("/{altKey}")
    public TipoInstitucionDTO obtener(@PathVariable String altKey) {
        return ensamblador.aDto(gestionTipos.obtenerTipo(altKey));
    }

    @PutMapping("/{altKey}")
    @Transactional
    public TipoInstitucionDTO editar(@PathVariable String altKey,
                                     @Valid @RequestBody EditarTipoRequest solicitud) {
        ContextoSesion contexto = proveedorContexto.contextoActual();
        DatosTipo datos = new DatosTipo(solicitud.nombre(), solicitud.ambito(), solicitud.descripcion());
        return ensamblador.aDto(gestionTipos.editarTipo(contexto, altKey, datos));
    }

    @PostMapping("/{altKey}/activacion")
    @Transactional
    public TipoInstitucionDTO cambiarVigencia(@PathVariable String altKey,
                                              @Valid @RequestBody ActivacionTipoRequest solicitud) {
        ContextoSesion contexto = proveedorContexto.contextoActual();
        return ensamblador.aDto(gestionTipos.cambiarVigenciaTipo(contexto, altKey, solicitud.vigente()));
    }

    @GetMapping
    public PaginaDTO<TipoInstitucionDTO> listar(
            @RequestParam(required = false) String ambito,
            @RequestParam(required = false) String texto,
            @RequestParam(required = false) Boolean vigente,
            @RequestParam(required = false) Integer pagina,
            @RequestParam(required = false) Integer tamano) {
        Ambito ambitoFiltro = ambito == null || ambito.isBlank() ? null : Ambito.desde(ambito);
        FiltroTipos filtro = new FiltroTipos(ambitoFiltro, texto, vigente);
        return ensamblador.paginaTipos(gestionTipos.listarTipos(filtro, Paginado.de(pagina, tamano)));
    }
}
