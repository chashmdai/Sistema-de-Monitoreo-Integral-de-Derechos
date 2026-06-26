package cl.smid.requerimientos.api;

import cl.smid.requerimientos.api.dto.AgregarNnaRequest;
import cl.smid.requerimientos.api.dto.CrearRequerimientoRequest;
import cl.smid.requerimientos.api.dto.DecisionAdmisibilidadRequest;
import cl.smid.requerimientos.api.dto.DerechoVulneradoRequest;
import cl.smid.requerimientos.api.dto.EditarRequerimientoRequest;
import cl.smid.requerimientos.api.dto.PaginaDTO;
import cl.smid.requerimientos.api.dto.RequerimientoDTO;
import cl.smid.requerimientos.api.dto.ResumenRequerimientoDTO;
import cl.smid.requerimientos.api.error.ErrorResponse;
import cl.smid.requerimientos.dominio.modelo.EstadoRequerimiento;
import cl.smid.requerimientos.dominio.modelo.PaginaDominio;
import cl.smid.requerimientos.dominio.modelo.Requerimiento;
import cl.smid.requerimientos.dominio.puerto.entrada.GestionRequerimientosUseCase;
import cl.smid.requerimientos.dominio.puerto.entrada.GestionRequerimientosUseCase.ComandoAdmisibilidad;
import cl.smid.requerimientos.dominio.puerto.entrada.GestionRequerimientosUseCase.ComandoAgregarNna;
import cl.smid.requerimientos.dominio.puerto.entrada.GestionRequerimientosUseCase.ComandoCrear;
import cl.smid.requerimientos.dominio.puerto.entrada.GestionRequerimientosUseCase.ComandoEditar;
import cl.smid.requerimientos.dominio.puerto.entrada.GestionRequerimientosUseCase.ContextoUsuario;
import cl.smid.requerimientos.dominio.puerto.entrada.GestionRequerimientosUseCase.DerechoSolicitado;
import cl.smid.requerimientos.dominio.puerto.entrada.GestionRequerimientosUseCase.FiltroListado;
import cl.smid.requerimientos.infraestructura.seguridad.UsuarioAutenticado;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Adaptador de entrada REST de requerimientos (Núcleo 6.3). Tras el {@code StripPrefix=1} del
 * Gateway, cuelga de {@code /requerimientos}.
 *
 * <p>Responsabilidades de aplicación que aquí se concentran: <b>demarcación transaccional</b> (cada
 * operación de escritura abre su transacción; las lecturas son de solo lectura), traducción
 * DTO↔comando, extracción del {@link ContextoUsuario} desde el {@code SecurityContext} y la
 * autorización de Coordinación para la admisibilidad. La lógica de negocio vive en el dominio.</p>
 */
@RestController
@RequestMapping("/requerimientos")
@Tag(name = "Requerimientos", description = "Ingreso, edicion, envio, admisibilidad y asignacion de requerimientos.")
@SecurityRequirement(name = "bearerAuth")
public class RequerimientoController {

    private final GestionRequerimientosUseCase casoUso;
    private final MapeadorRespuesta mapeador;

    public RequerimientoController(GestionRequerimientosUseCase casoUso, MapeadorRespuesta mapeador) {
        this.casoUso = casoUso;
        this.mapeador = mapeador;
    }

    /** Crea un requerimiento en BORRADOR (USR.01). Autenticado. */
    @PostMapping
    @Transactional
    @Operation(summary = "Crear requerimiento",
            description = "Crea un requerimiento en estado BORRADOR. La sede y autoria se estampan desde el JWT.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Requerimiento creado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RequerimientoDTO.class))),
            @ApiResponse(responseCode = "400", description = "REQ-001 - Validacion de DTO o cuerpo ilegible",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "AUTZ-003 - Token ausente o invalido",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "AUTZ-004 - Sin permiso",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "422", description = "REQ-422 - Regla de negocio violada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "REQ-500 - Error interno",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<RequerimientoDTO> crear(@Valid @RequestBody CrearRequerimientoRequest peticion) {
        ContextoUsuario ctx = contexto();
        ComandoCrear cmd = new ComandoCrear(peticion.canal(), peticion.complejidad(), peticion.urgencia(),
                peticion.idUnidadDestinoAlt(), peticion.resumen(), peticion.idRequirenteAlt(),
                peticion.esBeta());
        Requerimiento creado = casoUso.crear(cmd, ctx);
        URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{altKey}").buildAndExpand(creado.altKey()).toUri();
        return ResponseEntity.created(ubicacion).body(mapeador.aDetalle(creado));
    }

    /** Edita un requerimiento mutable (partial-merge). Territorial. */
    @PutMapping("/{altKey}")
    @Transactional
    @Operation(summary = "Editar requerimiento",
            description = "Actualizacion partial-merge de requerimientos en estados mutables: BORRADOR, INGRESADO o EN_ADMISIBILIDAD.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Requerimiento editado",
                    content = @Content(schema = @Schema(implementation = RequerimientoDTO.class))),
            @ApiResponse(responseCode = "400", description = "REQ-001 - Solicitud invalida",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "AUTZ-003 - Token ausente o invalido",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "AUTZ-004 - Sin permiso",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "REQ-404 - Inexistente o fuera de alcance territorial",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "REQ-409 - Conflicto con estado actual",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "422", description = "REQ-422 - Regla de negocio violada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "REQ-500 - Error interno",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<RequerimientoDTO> editar(
                                                   @Parameter(description = "Identificador publico opaco del requerimiento.", example = "1c2d3e4f-5a6b-7c8d-9e0f-1a2b3c4d5e6f")
                                                   @PathVariable String altKey,
                                                   @Valid @RequestBody EditarRequerimientoRequest peticion) {
        ComandoEditar cmd = new ComandoEditar(peticion.canal(), peticion.complejidad(), peticion.urgencia(),
                peticion.idUnidadDestinoAlt(), peticion.resumen(), peticion.idRequirenteAlt());
        Requerimiento editado = casoUso.editar(altKey, cmd, contexto());
        return ResponseEntity.ok(mapeador.aDetalle(editado));
    }

    /** Envía el requerimiento: BORRADOR -> INGRESADO (valida mínimos). Territorial. */
    @PostMapping("/{altKey}/enviar")
    @Transactional
    @Operation(summary = "Enviar requerimiento",
            description = "Transiciona BORRADOR a INGRESADO. Valida minimos: canal, sede y al menos un NNA; si faltan responde REQ-422.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Requerimiento ingresado",
                    content = @Content(schema = @Schema(implementation = RequerimientoDTO.class))),
            @ApiResponse(responseCode = "401", description = "AUTZ-003 - Token ausente o invalido",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "AUTZ-004 - Sin permiso",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "REQ-404 - Inexistente o fuera de alcance territorial",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "REQ-409 - Transicion invalida",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "422", description = "REQ-422 - Minimos de ingreso incompletos",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "REQ-500 - Error interno",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<RequerimientoDTO> enviar(
            @Parameter(description = "Identificador publico opaco del requerimiento.", example = "1c2d3e4f-5a6b-7c8d-9e0f-1a2b3c4d5e6f")
            @PathVariable String altKey) {
        Requerimiento ingresado = casoUso.enviar(altKey, contexto());
        return ResponseEntity.ok(mapeador.aDetalle(ingresado));
    }

    /** Agrega un NNA afectado con sus derechos (USR.01). Territorial. */
    @PostMapping("/{altKey}/nna")
    @Transactional
    @Operation(summary = "Agregar NNA afectado",
            description = "Agrega un NNA afectado y sus derechos vulnerados a un requerimiento mutable.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "NNA agregado",
                    content = @Content(schema = @Schema(implementation = RequerimientoDTO.class))),
            @ApiResponse(responseCode = "400", description = "REQ-001 - Solicitud invalida",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "AUTZ-003 - Token ausente o invalido",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "AUTZ-004 - Sin permiso",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "REQ-404 - Inexistente o fuera de alcance territorial",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "REQ-409 - Conflicto con estado actual",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "422", description = "REQ-422 - Regla de negocio violada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "REQ-500 - Error interno",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<RequerimientoDTO> agregarNna(
                                                       @Parameter(description = "Identificador publico opaco del requerimiento.", example = "1c2d3e4f-5a6b-7c8d-9e0f-1a2b3c4d5e6f")
                                                       @PathVariable String altKey,
                                                       @Valid @RequestBody AgregarNnaRequest peticion) {
        List<DerechoSolicitado> derechos = (peticion.derechos() == null ? List.<DerechoVulneradoRequest>of()
                : peticion.derechos()).stream()
                .map(d -> new DerechoSolicitado(d.idDerechoAlt(), d.idCausaAlt()))
                .toList();
        ComandoAgregarNna cmd = new ComandoAgregarNna(peticion.idPersonaAlt(), derechos);
        Requerimiento actualizado = casoUso.agregarNna(altKey, cmd, contexto());
        return ResponseEntity.ok(mapeador.aDetalle(actualizado));
    }

    /**
     * Registra la decisión de admisibilidad (USR.02). Exige rol de Coordinación: si falta, Spring
     * lanza {@code AccessDeniedException} y se responde {@code AUTZ-004} (403). Territorial.
     */
    @PostMapping("/{altKey}/admisibilidad")
    @PreAuthorize("@autorizacionRoles.tieneRolCoordinacion(authentication)")
    @Transactional
    @Operation(summary = "Decidir admisibilidad",
            description = "Ejecuta INADMISIBLE, RESPUESTA_INMEDIATA o ASIGNACION. RESPUESTA_INMEDIATA registra la decision pero no envia comunicacion saliente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Decision registrada",
                    content = @Content(schema = @Schema(implementation = RequerimientoDTO.class))),
            @ApiResponse(responseCode = "400", description = "REQ-001 - Solicitud invalida",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "AUTZ-003 - Token ausente o invalido",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "AUTZ-004 - Falta rol de Coordinacion",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "REQ-404 - Inexistente o fuera de alcance territorial",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "REQ-409 - Transicion invalida",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "422", description = "REQ-422 - Profesional/unidad invalida u otra regla",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "REQ-500 - Error interno",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<RequerimientoDTO> decidirAdmisibilidad(
            @Parameter(description = "Identificador publico opaco del requerimiento.", example = "1c2d3e4f-5a6b-7c8d-9e0f-1a2b3c4d5e6f")
            @PathVariable String altKey,
            @Valid @RequestBody DecisionAdmisibilidadRequest peticion) {
        ComandoAdmisibilidad cmd = new ComandoAdmisibilidad(peticion.accion(),
                peticion.escaladoADefensora(), peticion.idProfesionalAsignadoAlt(), peticion.observacion());
        Requerimiento decidido = casoUso.decidirAdmisibilidad(altKey, cmd, contexto());
        return ResponseEntity.ok(mapeador.aDetalle(decidido));
    }

    /** Obtiene el detalle. Fuera de alcance => 404. Territorial. */
    @GetMapping("/{altKey}")
    @Transactional(readOnly = true)
    @Operation(summary = "Obtener detalle",
            description = "Obtiene el detalle completo de un requerimiento visible. Fuera de alcance territorial responde REQ-404.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Detalle de requerimiento",
                    content = @Content(schema = @Schema(implementation = RequerimientoDTO.class))),
            @ApiResponse(responseCode = "401", description = "AUTZ-003 - Token ausente o invalido",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "AUTZ-004 - Sin permiso",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "REQ-404 - Inexistente o fuera de alcance territorial",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "REQ-500 - Error interno",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<RequerimientoDTO> obtener(
            @Parameter(description = "Identificador publico opaco del requerimiento.", example = "1c2d3e4f-5a6b-7c8d-9e0f-1a2b3c4d5e6f")
            @PathVariable String altKey) {
        Requerimiento req = casoUso.obtener(altKey, contexto());
        return ResponseEntity.ok(mapeador.aDetalle(req));
    }

    /** Lista requerimientos visibles con filtros opcionales. Territorial. */
    @GetMapping
    @Transactional(readOnly = true)
    @Operation(summary = "Listar requerimientos",
            description = "Listado paginado de requerimientos visibles dentro del alcance territorial, con filtros opcionales por estado y unidad.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pagina de requerimientos",
                    content = @Content(schema = @Schema(implementation = PaginaDTO.class))),
            @ApiResponse(responseCode = "400", description = "REQ-001 - Parametros invalidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "AUTZ-003 - Token ausente o invalido",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "AUTZ-004 - Sin permiso",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "REQ-500 - Error interno",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<PaginaDTO<ResumenRequerimientoDTO>> listar(
            @Parameter(description = "Estado del requerimiento.", schema = @Schema(allowableValues = {"BORRADOR", "INGRESADO", "EN_ADMISIBILIDAD", "INADMISIBLE", "RESPONDIDO", "ASIGNADO"}))
            @RequestParam(required = false) EstadoRequerimiento estado,
            @Parameter(description = "altKey de unidad destino.", example = "5f1d2c8e-1d4a-4a1e-9b2c-0e7a1b2c3d4e")
            @RequestParam(name = "unidad", required = false) String idUnidadDestinoAlt,
            @Parameter(description = "Pagina base 0.", example = "0")
            @RequestParam(defaultValue = "0") int pagina,
            @Parameter(description = "Tamano de pagina.", example = "20")
            @RequestParam(defaultValue = "20") int tamano) {
        FiltroListado filtro = new FiltroListado(estado, idUnidadDestinoAlt, pagina, tamano);
        PaginaDominio<Requerimiento> resultado = casoUso.listar(filtro, contexto());
        return ResponseEntity.ok(mapeador.aPaginaResumen(resultado));
    }

    /**
     * Construye el contexto del usuario a partir del principal autenticado del SecurityContext.
     *
     * @return contexto de usuario para el caso de uso
     */
    private ContextoUsuario contexto() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UsuarioAutenticado u = (UsuarioAutenticado) auth.getPrincipal();
        return new ContextoUsuario(u.usuarioAlt(), u.idSedeAlt(), u.idUnidadAlt(), u.alcance(), u.roles());
    }
}
