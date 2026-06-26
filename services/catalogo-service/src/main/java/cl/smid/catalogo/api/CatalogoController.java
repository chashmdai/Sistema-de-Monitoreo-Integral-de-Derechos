package cl.smid.catalogo.api;

import cl.smid.catalogo.api.dto.ActualizarDerechoRequest;
import cl.smid.catalogo.api.dto.CausaDTO;
import cl.smid.catalogo.api.dto.CrearCausaRequest;
import cl.smid.catalogo.api.dto.CrearDerechoRequest;
import cl.smid.catalogo.api.dto.DerechoDetalleDTO;
import cl.smid.catalogo.api.dto.DerechoArbolDTO;
import cl.smid.catalogo.api.dto.DerechoPlanoDTO;
import cl.smid.catalogo.api.error.ErrorResponse;
import cl.smid.catalogo.config.ContextoSesionActual;
import cl.smid.catalogo.dominio.puerto.entrada.ActorEvento;
import cl.smid.catalogo.dominio.puerto.entrada.ActualizarDerechoCmd;
import cl.smid.catalogo.dominio.puerto.entrada.CatalogoUseCase;
import cl.smid.catalogo.dominio.puerto.entrada.CrearCausaCmd;
import cl.smid.catalogo.dominio.puerto.entrada.CrearDerechoCmd;
import cl.smid.catalogo.dominio.modelo.Causa;
import cl.smid.catalogo.dominio.modelo.Derecho;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Controlador REST del servicio de Catálogo de Derechos.
 *
 * <h2>Ruta base</h2>
 * <p>Mapea en {@code /catalogo/...}. El Gateway expone el servicio bajo {@code /api/catalogo/**}
 * y, con su filtro {@code StripPrefix}, elimina únicamente el segmento {@code /api} antes de
 * reenviar; por eso los controladores se montan en {@code /catalogo} y no en {@code /}.</p>
 *
 * <h2>Seguridad</h2>
 * <p>Las lecturas (GET) requieren solo autenticación; las escrituras (POST/PUT/DELETE)
 * requieren rol de Administración. Esa autorización se aplica de forma centralizada en
 * {@code SeguridadConfig} (basada en URL), de modo que aquí no se repiten anotaciones de rol.</p>
 *
 * <h2>Transaccionalidad</h2>
 * <p>La demarcación vive en estos métodos para mantener puro el dominio: las lecturas usan
 * {@code @Transactional(readOnly = true)} y las escrituras {@code @Transactional}, de modo que
 * cada caso de uso de escritura (que puede tocar varias filas, p. ej. una baja en cascada o un
 * recálculo de niveles) se ejecuta de forma atómica.</p>
 */
@RestController
@RequestMapping("/catalogo")
@Tag(name = "Catalogo de Derechos", description = "Derechos vulnerados, causas y taxonomia institucional.")
@SecurityRequirement(name = "bearerAuth")
public class CatalogoController {

    private final CatalogoUseCase catalogo;
    private final MapeadorRespuesta mapeador;
    private final ContextoSesionActual sesion;

    public CatalogoController(CatalogoUseCase catalogo, MapeadorRespuesta mapeador,
                              ContextoSesionActual sesion) {
        this.catalogo = catalogo;
        this.mapeador = mapeador;
        this.sesion = sesion;
    }

    // ============================== Lecturas ==============================

    /**
     * Árbol taxonómico de derechos vigentes. Con {@code ?formato=plano} devuelve la lista
     * plana (cada nodo con el alt_key de su padre); en cualquier otro caso, el árbol anidado.
     */
    @GetMapping("/derechos")
    @Transactional(readOnly = true)
    @Operation(summary = "Listar derechos",
            description = "Lectura autenticada. Devuelve el arbol anidado de derechos vigentes o lista plana con formato=plano.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Arbol o lista plana de derechos",
                    content = @Content(array = @ArraySchema(schema = @Schema(oneOf = {DerechoArbolDTO.class, DerechoPlanoDTO.class})))),
            @ApiResponse(responseCode = "401", description = "AUTZ-003 - Token ausente o invalido",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "AUTZ-004 - Acceso denegado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "CAT-500 - Error interno",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public List<?> listarDerechos(@RequestParam(name = "formato", required = false) String formato) {
        if ("plano".equalsIgnoreCase(formato)) {
            return mapeador.aPlano(catalogo.obtenerPlano());
        }
        return mapeador.aArbol(catalogo.obtenerArbol());
    }

    /** Búsqueda por nombre o código (acento- e insensible a mayúsculas). */
    @GetMapping("/derechos/buscar")
    @Transactional(readOnly = true)
    @Operation(summary = "Buscar derechos",
            description = "Lectura autenticada. Busca por nombre o codigo, de forma insensible a tildes y mayusculas.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Derechos encontrados",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = DerechoPlanoDTO.class)))),
            @ApiResponse(responseCode = "401", description = "AUTZ-003 - Token ausente o invalido",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "CAT-500 - Error interno",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public List<?> buscarDerechos(
            @Parameter(description = "Texto de busqueda por nombre o codigo.", example = "educacion")
            @RequestParam(name = "q", required = false) String q) {
        return mapeador.aPlano(catalogo.buscar(q));
    }

    /** Detalle de un derecho con sus hijos directos vigentes. */
    @GetMapping("/derechos/{altKey}")
    @Transactional(readOnly = true)
    @Operation(summary = "Obtener detalle de derecho",
            description = "Lectura autenticada. Devuelve un derecho con sus hijos directos vigentes.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Derecho encontrado",
                    content = @Content(schema = @Schema(implementation = DerechoDetalleDTO.class))),
            @ApiResponse(responseCode = "401", description = "AUTZ-003 - Token ausente o invalido",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "CAT-001 - Derecho no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "CAT-500 - Error interno",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public DerechoDetalleDTO obtenerDerecho(
            @Parameter(description = "altKey publico del derecho.", example = "11111111-1111-4111-8111-000000000003")
            @PathVariable String altKey) {
        return mapeador.aDetalle(catalogo.obtenerDetalle(altKey));
    }

    /** Causas vigentes de un derecho. */
    @GetMapping("/derechos/{altKey}/causas")
    @Transactional(readOnly = true)
    @Operation(summary = "Listar causas de un derecho",
            description = "Lectura autenticada. Devuelve causas vigentes vinculadas a un derecho.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Causas vigentes",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = CausaDTO.class)))),
            @ApiResponse(responseCode = "401", description = "AUTZ-003 - Token ausente o invalido",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "CAT-001 - Derecho no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "CAT-500 - Error interno",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public List<CausaDTO> listarCausas(
            @Parameter(description = "altKey publico del derecho.", example = "11111111-1111-4111-8111-000000000003")
            @PathVariable String altKey) {
        return mapeador.aCausas(catalogo.obtenerCausas(altKey));
    }

    // ============================== Escrituras ==============================

    /** Crea un derecho (raíz o hijo). Responde 201 con el detalle y la cabecera {@code Location}. */
    @PostMapping("/derechos")
    @Transactional
    @Operation(summary = "Crear derecho",
            description = "Escritura administrativa. Crea un derecho raiz o hijo. Requiere rol de administracion.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Derecho creado",
                    content = @Content(schema = @Schema(implementation = DerechoDetalleDTO.class))),
            @ApiResponse(responseCode = "400", description = "CAT-005 - Validacion de DTO",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "AUTZ-003 - Token ausente o invalido",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "AUTZ-004 - Sin rol de administracion",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "CAT-002 - Codigo duplicado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "422", description = "CAT-004 - Arbol invalido",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "CAT-500 - Error interno",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<DerechoDetalleDTO> crearDerecho(@Valid @RequestBody CrearDerechoRequest req) {
        ActorEvento actor = sesion.comoActor();
        CrearDerechoCmd cmd = new CrearDerechoCmd(
                req.idPadreAltKey(), req.codigo(), req.nombre(), req.descripcion(), req.orden());

        Derecho creado = catalogo.crearDerecho(cmd, actor);
        DerechoDetalleDTO cuerpo = mapeador.aDetalle(catalogo.obtenerDetalle(creado.getAltKey()));

        URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{altKey}")
                .buildAndExpand(creado.getAltKey())
                .toUri();
        return ResponseEntity.created(ubicacion).body(cuerpo);
    }

    /** Actualiza un derecho existente. Responde 200 con el detalle actualizado. */
    @PutMapping("/derechos/{altKey}")
    @Transactional
    @Operation(summary = "Actualizar derecho",
            description = "Escritura administrativa. Actualiza nombre, descripcion, orden o padre. El codigo es inmutable: si cambia responde CAT-006.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Derecho actualizado",
                    content = @Content(schema = @Schema(implementation = DerechoDetalleDTO.class))),
            @ApiResponse(responseCode = "400", description = "CAT-005 - Validacion de DTO",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "AUTZ-003 - Token ausente o invalido",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "AUTZ-004 - Sin rol de administracion",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "CAT-001 - Derecho no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "CAT-006 - Codigo inmutable",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "422", description = "CAT-004 - Arbol invalido",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "CAT-500 - Error interno",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public DerechoDetalleDTO actualizarDerecho(
                                               @Parameter(description = "altKey publico del derecho.", example = "11111111-1111-4111-8111-000000000003")
                                               @PathVariable String altKey,
                                               @Valid @RequestBody ActualizarDerechoRequest req) {
        ActorEvento actor = sesion.comoActor();
        ActualizarDerechoCmd cmd = new ActualizarDerechoCmd(
                req.codigo(), req.nombre(), req.descripcion(), req.orden(), req.idPadreAltKey());

        catalogo.actualizarDerecho(altKey, cmd, actor);
        return mapeador.aDetalle(catalogo.obtenerDetalle(altKey));
    }

    /** Baja lógica de un derecho (en cascada sobre sus descendientes). Responde 204; es idempotente. */
    @DeleteMapping("/derechos/{altKey}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    @Operation(summary = "Dar de baja derecho",
            description = "Escritura administrativa. Ejecuta baja logica idempotente en cascada sobre descendientes.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Baja aplicada", content = @Content),
            @ApiResponse(responseCode = "401", description = "AUTZ-003 - Token ausente o invalido",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "AUTZ-004 - Sin rol de administracion",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "CAT-500 - Error interno",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public void darDeBajaDerecho(
            @Parameter(description = "altKey publico del derecho.", example = "11111111-1111-4111-8111-000000000003")
            @PathVariable String altKey) {
        catalogo.darDeBajaDerecho(altKey, sesion.comoActor());
    }

    /** Crea una causa para un derecho. Responde 201 con la causa creada. */
    @PostMapping("/derechos/{altKey}/causas")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    @Operation(summary = "Crear causa",
            description = "Escritura administrativa. Crea una causa vigente asociada al derecho indicado.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Causa creada",
                    content = @Content(schema = @Schema(implementation = CausaDTO.class))),
            @ApiResponse(responseCode = "400", description = "CAT-005 - Validacion de DTO",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "AUTZ-003 - Token ausente o invalido",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "AUTZ-004 - Sin rol de administracion",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "CAT-001 - Derecho no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "CAT-003 - Codigo de causa duplicado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "CAT-500 - Error interno",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public CausaDTO crearCausa(
                               @Parameter(description = "altKey publico del derecho.", example = "11111111-1111-4111-8111-000000000003")
                               @PathVariable String altKey,
                               @Valid @RequestBody CrearCausaRequest req) {
        Causa creada = catalogo.crearCausa(altKey, new CrearCausaCmd(req.codigo(), req.nombre()),
                sesion.comoActor());
        return mapeador.aCausa(creada);
    }
}
