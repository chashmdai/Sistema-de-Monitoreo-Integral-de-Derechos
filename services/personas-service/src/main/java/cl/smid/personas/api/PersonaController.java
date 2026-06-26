package cl.smid.personas.api;

import cl.smid.personas.api.dto.ActualizarPersonaRequest;
import cl.smid.personas.api.dto.BuscarDuplicadosRequest;
import cl.smid.personas.api.dto.CrearPersonaRequest;
import cl.smid.personas.api.dto.DuplicadosResponse;
import cl.smid.personas.api.dto.PaginaResponse;
import cl.smid.personas.api.dto.PersonaResponse;
import cl.smid.personas.api.dto.PersonaResumenResponse;
import cl.smid.personas.api.error.ErrorResponse;
import cl.smid.personas.dominio.excepcion.SolicitudInvalidaException;
import cl.smid.personas.dominio.modelo.ContextoTerritorial;
import cl.smid.personas.dominio.modelo.CriterioDuplicados;
import cl.smid.personas.dominio.modelo.DatosPersona;
import cl.smid.personas.dominio.modelo.Pagina;
import cl.smid.personas.dominio.modelo.Persona;
import cl.smid.personas.dominio.modelo.ResultadoDuplicados;
import cl.smid.personas.dominio.puerto.entrada.GestionPersonasUseCase;
import cl.smid.personas.infraestructura.seguridad.UsuarioAutenticado;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/**
 * Controlador REST del servicio de Personas (Núcleo 5.4). Es el borde HTTP: traduce DTOs,
 * construye el {@link ContextoTerritorial} a partir del principal autenticado y delega en el
 * caso de uso. Aquí vive la <b>demarcación transaccional</b> (el dominio es puro): escritura en
 * {@code POST}/{@code PUT}; lectura de sólo lectura en las consultas.
 *
 * <p>Las rutas cuelgan de {@code /personas} sin el prefijo {@code /api}, que el Gateway elimina
 * ({@code StripPrefix}) antes de enrutar.</p>
 */
@RestController
@RequestMapping("/personas")
@Tag(name = "Personas", description = "Registro maestro de NNA, adultos, personas juridicas y testigos.")
@SecurityRequirement(name = "bearerAuth")
public class PersonaController {

    /** Tamaño de página por defecto y máximo permitido para las búsquedas por nombre. */
    private static final int TAMANO_PAGINA_DEFECTO = 20;
    private static final int TAMANO_PAGINA_MAXIMO = 100;

    private final GestionPersonasUseCase gestionPersonas;

    public PersonaController(GestionPersonasUseCase gestionPersonas) {
        this.gestionPersonas = gestionPersonas;
    }

    // ----------------------------------------------------------------------
    //  Búsqueda unificada por RUT o por nombre (territorial)
    // ----------------------------------------------------------------------

    /**
     * Busca personas dentro del alcance del solicitante. Exactamente uno de los parámetros debe
     * venir informado:
     * <ul>
     *   <li>{@code rut}: búsqueda exacta; devuelve una página de a lo sumo un elemento. Un RUT
     *       malformado produce {@code PER-002} (422), por consistencia "fail fast".</li>
     *   <li>{@code q}: búsqueda parcial por nombre, paginada.</li>
     * </ul>
     * Si no se informa ninguno, se responde {@code PER-001} (400).
     */
    @GetMapping
    @Transactional(readOnly = true)
    @Operation(
            summary = "Buscar personas por RUT o nombre",
            description = "Busqueda territorial. Debe informarse exactamente un criterio: rut o q. Un RUT invalido responde PER-002."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pagina de resultados",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PaginaResponse.class))),
            @ApiResponse(responseCode = "400", description = "PER-001 - Solicitud invalida",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "AUTZ-003 - Token ausente o invalido",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "AUTZ-004 - Autenticado sin permiso",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "422", description = "PER-002 - RUT invalido",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "PER-500 - Error interno",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<PaginaResponse<PersonaResumenResponse>> buscar(
            @Parameter(description = "RUT exacto. Excluyente con q.", example = "12345678-5")
            @RequestParam(value = "rut", required = false) String rut,
            @Parameter(description = "Texto de busqueda parcial por nombre. Excluyente con rut.", example = "Camila Reyes")
            @RequestParam(value = "q", required = false) String q,
            @Parameter(description = "Pagina base 0.", example = "0")
            @RequestParam(value = "pagina", defaultValue = "0") int pagina,
            @Parameter(description = "Tamano de pagina, maximo 100.", example = "20")
            @RequestParam(value = "tamano", defaultValue = "20") int tamano,
            @AuthenticationPrincipal UsuarioAutenticado usuario) {

        ContextoTerritorial ctx = usuario.aContextoTerritorial();

        Pagina<Persona> resultado;
        if (rut != null && !rut.isBlank()) {
            resultado = gestionPersonas.buscarPorRut(rut, ctx);
        } else if (q != null && !q.isBlank()) {
            resultado = gestionPersonas.buscarPorNombre(q, ctx, normalizarPagina(pagina), normalizarTamano(tamano));
        } else {
            throw new SolicitudInvalidaException(
                    "Debe indicar un criterio de búsqueda: 'rut' o 'q'.");
        }

        return ResponseEntity.ok(MapeadorRespuesta.aPaginaResumen(resultado));
    }

    // ----------------------------------------------------------------------
    //  Obtención por identificador público (territorial)
    // ----------------------------------------------------------------------

    /**
     * Obtiene el detalle de una persona por su {@code altKey}. Si está fuera del alcance del
     * solicitante se responde {@code PER-404} (404), idéntico a "no existe", para no revelar
     * registros de otras jurisdicciones.
     */
    @GetMapping("/{altKey}")
    @Transactional(readOnly = true)
    @Operation(
            summary = "Obtener detalle de persona",
            description = "Obtiene una persona por altKey dentro del alcance territorial. Fuera de alcance se responde PER-404."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Persona encontrada",
                    content = @Content(schema = @Schema(implementation = PersonaResponse.class))),
            @ApiResponse(responseCode = "401", description = "AUTZ-003 - Token ausente o invalido",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "AUTZ-004 - Autenticado sin permiso",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "PER-404 - Persona inexistente o fuera de alcance territorial",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "PER-500 - Error interno",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<PersonaResponse> obtener(
            @Parameter(description = "Identificador publico opaco de la persona.", example = "a9b8c7d6-1111-2222-3333-444455556666")
            @PathVariable String altKey,
            @AuthenticationPrincipal UsuarioAutenticado usuario) {

        Persona persona = gestionPersonas.obtener(altKey, usuario.aContextoTerritorial());
        return ResponseEntity.ok(MapeadorRespuesta.aDetalle(persona));
    }

    // ----------------------------------------------------------------------
    //  Alta
    // ----------------------------------------------------------------------

    /**
     * Da de alta una persona. La sede, la unidad y la autoría se estampan desde el token (no se
     * aceptan del cliente). Devuelve 201 con la cabecera {@code Location} apuntando al recurso.
     */
    @PostMapping
    @Transactional
    @Operation(
            summary = "Crear persona",
            description = "Crea una persona dentro de la sede/unidad del token. Un NNA puede registrarse sin RUT. RUT invalido responde PER-002 y RUT duplicado PER-003."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Persona creada",
                    content = @Content(schema = @Schema(implementation = PersonaResponse.class))),
            @ApiResponse(responseCode = "400", description = "PER-001 - Solicitud invalida",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "AUTZ-003 - Token ausente o invalido",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "AUTZ-004 - Autenticado sin permiso",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "PER-003 - RUT duplicado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = """
                                    {"status":409,"error":"Conflict","codigo":"PER-003","mensaje":"Ya existe una persona vigente con ese RUT.","ruta":"/personas","timestamp":"2026-01-15T12:00:00Z"}
                                    """))),
            @ApiResponse(responseCode = "422", description = "PER-002 - RUT invalido",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "PER-500 - Error interno",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<PersonaResponse> crear(
            @Valid @RequestBody CrearPersonaRequest peticion,
            @AuthenticationPrincipal UsuarioAutenticado usuario,
            UriComponentsBuilder uriBuilder) {

        DatosPersona datos = MapeadorPeticion.aDatos(peticion);
        Persona creada = gestionPersonas.crear(datos, usuario.aContextoTerritorial());

        URI ubicacion = uriBuilder.path("/personas/{altKey}")
                .buildAndExpand(creada.altKey())
                .toUri();

        return ResponseEntity.created(ubicacion).body(MapeadorRespuesta.aDetalle(creada));
    }

    // ----------------------------------------------------------------------
    //  Actualización parcial
    // ----------------------------------------------------------------------

    /**
     * Actualiza parcialmente una persona visible para el solicitante (partial-merge). Fuera de
     * alcance ⇒ {@code PER-404}.
     */
    @PutMapping("/{altKey}")
    @Transactional
    @Operation(
            summary = "Actualizar parcialmente persona",
            description = "Actualizacion partial-merge. Los campos nulos no se modifican; rut vacio limpia el RUT. Fuera de alcance territorial responde PER-404."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Persona actualizada",
                    content = @Content(schema = @Schema(implementation = PersonaResponse.class))),
            @ApiResponse(responseCode = "400", description = "PER-001 - Solicitud invalida",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "AUTZ-003 - Token ausente o invalido",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "AUTZ-004 - Autenticado sin permiso",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "PER-404 - Persona inexistente o fuera de alcance territorial",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "PER-003 - RUT duplicado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "422", description = "PER-002 - RUT invalido",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "PER-500 - Error interno",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<PersonaResponse> actualizar(
            @Parameter(description = "Identificador publico opaco de la persona.", example = "a9b8c7d6-1111-2222-3333-444455556666")
            @PathVariable String altKey,
            @Valid @RequestBody ActualizarPersonaRequest peticion,
            @AuthenticationPrincipal UsuarioAutenticado usuario) {

        DatosPersona datos = MapeadorPeticion.aDatos(peticion);
        Persona actualizada = gestionPersonas.actualizar(altKey, datos, usuario.aContextoTerritorial());
        return ResponseEntity.ok(MapeadorRespuesta.aDetalle(actualizada));
    }

    // ----------------------------------------------------------------------
    //  Prevalidación de duplicados (cross-territorial por diseño)
    // ----------------------------------------------------------------------

    /**
     * Ejecuta la prevalidación de duplicados (USR.01). Es una consulta cross-territorial: no
     * aplica filtro de alcance y sólo devuelve datos mínimos de las coincidencias.
     */
    @PostMapping("/buscar-duplicados")
    @Transactional(readOnly = true)
    @Operation(
            summary = "Prevalidar duplicados",
            description = "Consulta cross-territorial de solo lectura. Informa coincidencias exactas por RUT y probables por similitud; no fusiona registros."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Resultado de deduplicacion",
                    content = @Content(schema = @Schema(implementation = DuplicadosResponse.class))),
            @ApiResponse(responseCode = "400", description = "PER-001 - Solicitud invalida",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "AUTZ-003 - Token ausente o invalido",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "AUTZ-004 - Autenticado sin permiso",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "422", description = "PER-002 - RUT invalido",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "PER-500 - Error interno",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<DuplicadosResponse> buscarDuplicados(
            @Valid @RequestBody BuscarDuplicadosRequest peticion) {

        CriterioDuplicados criterio = MapeadorPeticion.aCriterio(peticion);
        ResultadoDuplicados resultado = gestionPersonas.buscarDuplicados(criterio);
        return ResponseEntity.ok(MapeadorRespuesta.aDuplicados(resultado));
    }

    // ----------------------------- Apoyo -----------------------------

    /** Garantiza un número de página no negativo. */
    private static int normalizarPagina(int pagina) {
        return Math.max(pagina, 0);
    }

    /** Acota el tamaño de página al rango [1, máximo]; valores fuera de rango se ajustan. */
    private static int normalizarTamano(int tamano) {
        if (tamano <= 0) {
            return TAMANO_PAGINA_DEFECTO;
        }
        return Math.min(tamano, TAMANO_PAGINA_MAXIMO);
    }
}
