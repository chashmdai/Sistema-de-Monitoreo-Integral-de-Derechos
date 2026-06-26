package cl.smid.casos.api;

import cl.smid.casos.api.dto.CasoDetalleDTO;
import cl.smid.casos.api.dto.CasoResumenDTO;
import cl.smid.casos.api.dto.PaginaDTO;
import cl.smid.casos.api.dto.TransicionDTO;
import cl.smid.casos.dominio.excepcion.SolicitudInvalidaException;
import cl.smid.casos.dominio.modelo.AccionCaso;
import cl.smid.casos.dominio.modelo.Caso;
import cl.smid.casos.dominio.modelo.CasoEnriquecido;
import cl.smid.casos.dominio.modelo.ContextoTerritorial;
import cl.smid.casos.dominio.modelo.DatosEnriquecimiento;
import cl.smid.casos.dominio.modelo.EstadoCaso;
import cl.smid.casos.dominio.modelo.Pagina;
import cl.smid.casos.dominio.puerto.entrada.ConsultarCasos;
import cl.smid.casos.dominio.puerto.entrada.TransicionarCaso;
import cl.smid.casos.dominio.puerto.entrada.TransicionarCaso.ComandoTransicion;
import cl.smid.casos.api.error.SobreError;
import cl.smid.casos.infraestructura.seguridad.ProveedorContexto;
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
import org.springframework.http.HttpHeaders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * API REST del expediente. Tras el Gateway, las rutas {@code /api/casos/**} llegan aquí como
 * {@code /casos/**} (StripPrefix=1). No existe endpoint de creación: los casos NACEN de eventos
 * ({@code requerimiento.asignado}); por eso solo se exponen consulta y transición.
 *
 * <p>La demarcación transaccional de escritura se aplica AQUÍ ({@code @Transactional} en la
 * transición), conforme a la arquitectura. El alcance territorial y la facultad por rol los resuelve
 * el dominio.</p>
 */
@RestController
@RequestMapping("/casos")
@Tag(name = "Casos", description = "Gestión del expediente institucional nacido desde requerimientos asignados.")
@SecurityRequirement(name = "bearerAuth")
public class CasoController {

    private static final String PREFIJO_BEARER = "Bearer ";

    private final ConsultarCasos consultarCasos;
    private final TransicionarCaso transicionarCaso;
    private final ProveedorContexto proveedorContexto;
    private final MapeadorRespuesta mapeador;

    public CasoController(ConsultarCasos consultarCasos, TransicionarCaso transicionarCaso,
                          ProveedorContexto proveedorContexto, MapeadorRespuesta mapeador) {
        this.consultarCasos = consultarCasos;
        this.transicionarCaso = transicionarCaso;
        this.proveedorContexto = proveedorContexto;
        this.mapeador = mapeador;
    }

    /** Detalle de un caso por su identificador opaco (acotado al alcance territorial). */
    @GetMapping("/{altKey}")
    @Operation(summary = "Obtiene el detalle de un caso",
            description = """
                    Retorna el expediente por identificador opaco dentro del alcance territorial del usuario.
                    Si el caso no existe o está fuera del alcance territorial, responde CAS-404.
                    No existe endpoint de creación manual: los casos nacen por eventos requerimiento.asignado.
                    """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Caso encontrado",
                    content = @Content(schema = @Schema(implementation = CasoDetalleDTO.class))),
            @ApiResponse(responseCode = "401", description = "AUTZ-003 - No autenticado",
                    content = @Content(schema = @Schema(implementation = SobreError.class))),
            @ApiResponse(responseCode = "403", description = "AUTZ-004 - Sin rol requerido",
                    content = @Content(schema = @Schema(implementation = SobreError.class))),
            @ApiResponse(responseCode = "404", description = "CAS-404 - No existe o fuera de alcance territorial",
                    content = @Content(schema = @Schema(implementation = SobreError.class))),
            @ApiResponse(responseCode = "500", description = "CAS-500 - Error interno",
                    content = @Content(schema = @Schema(implementation = SobreError.class)))
    })
    public CasoDetalleDTO obtener(@Parameter(description = "Identificador opaco del caso.",
                                      example = "7d6b3c3c-9d22-4b10-8d3d-1f3900c13f20")
                                  @PathVariable String altKey,
                                  @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false)
                                  String autorizacion) {
        ContextoTerritorial ctx = proveedorContexto.actual();
        CasoEnriquecido enriquecido = consultarCasos.detalle(altKey, ctx, extraerToken(autorizacion));
        return mapeador.aDetalle(enriquecido);
    }

    /** Listado paginado de casos dentro del alcance, con filtros opcionales por estado y unidad. */
    @GetMapping
    @Operation(summary = "Lista casos paginados",
            description = """
                    Lista casos dentro del alcance territorial del usuario autenticado.
                    Permite filtrar por estado y por unidad usando identificadores opacos.
                    Estados: ABIERTO, EN_INVESTIGACION, EN_SEGUIMIENTO, SUSPENDIDO, CERRADO, ARCHIVADO.
                    """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Página de casos",
                    content = @Content(schema = @Schema(implementation = PaginaDTO.class))),
            @ApiResponse(responseCode = "400", description = "CAS-001 - Solicitud inválida",
                    content = @Content(schema = @Schema(implementation = SobreError.class))),
            @ApiResponse(responseCode = "401", description = "AUTZ-003 - No autenticado",
                    content = @Content(schema = @Schema(implementation = SobreError.class))),
            @ApiResponse(responseCode = "500", description = "CAS-500 - Error interno",
                    content = @Content(schema = @Schema(implementation = SobreError.class)))
    })
    public PaginaDTO<CasoResumenDTO> listar(@Parameter(description = "Estado del caso.",
                                                    example = "ABIERTO",
                                                    schema = @Schema(allowableValues = {
                                                            "ABIERTO", "EN_INVESTIGACION", "EN_SEGUIMIENTO",
                                                            "SUSPENDIDO", "CERRADO", "ARCHIVADO"}))
                                            @RequestParam(required = false) String estado,
                                            @Parameter(description = "Identificador opaco de la unidad.",
                                                    example = "4f86e9a4-2924-41d7-bf27-6ef13b6f6b9a")
                                            @RequestParam(required = false) String unidad,
                                            @Parameter(description = "Número de página, base cero.", example = "0")
                                            @RequestParam(defaultValue = "0") int pagina,
                                            @Parameter(description = "Tamaño de página.", example = "20")
                                            @RequestParam(defaultValue = "20") int tamano) {
        ContextoTerritorial ctx = proveedorContexto.actual();
        EstadoCaso estadoFiltro = interpretarEstado(estado);
        Pagina<Caso> resultado = consultarCasos.listar(ctx, estadoFiltro, unidad, pagina, tamano);
        return mapeador.aPagina(resultado);
    }

    /** Aplica una transición de estado al caso. Demarcación transaccional en el controlador. */
    @PostMapping("/{altKey}/transiciones")
    @Transactional
    @Operation(summary = "Transiciona el estado de un caso",
            description = """
                    Aplica una acción válida según la máquina de estados del expediente.
                    Acciones: INICIAR_INVESTIGACION, DERIVAR_A_SEGUIMIENTO, SUSPENDER, REANUDAR,
                    CERRAR, REABRIR y ARCHIVAR. CERRAR, REABRIR y ARCHIVAR requieren rol de Coordinación.
                    Una transición no permitida para el estado actual responde CAS-409.
                    """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Caso transicionado",
                    content = @Content(schema = @Schema(implementation = CasoDetalleDTO.class))),
            @ApiResponse(responseCode = "400", description = "CAS-001 - Solicitud inválida",
                    content = @Content(schema = @Schema(implementation = SobreError.class))),
            @ApiResponse(responseCode = "401", description = "AUTZ-003 - No autenticado",
                    content = @Content(schema = @Schema(implementation = SobreError.class))),
            @ApiResponse(responseCode = "403", description = "AUTZ-004 - Sin rol de Coordinación",
                    content = @Content(schema = @Schema(implementation = SobreError.class))),
            @ApiResponse(responseCode = "404", description = "CAS-404 - No existe o fuera de alcance territorial",
                    content = @Content(schema = @Schema(implementation = SobreError.class))),
            @ApiResponse(responseCode = "409", description = "CAS-409 - Transición inválida",
                    content = @Content(schema = @Schema(implementation = SobreError.class))),
            @ApiResponse(responseCode = "422", description = "CAS-422 - Regla de negocio incumplida",
                    content = @Content(schema = @Schema(implementation = SobreError.class))),
            @ApiResponse(responseCode = "500", description = "CAS-500 - Error interno",
                    content = @Content(schema = @Schema(implementation = SobreError.class)))
    })
    public CasoDetalleDTO transicionar(@Parameter(description = "Identificador opaco del caso.",
                                               example = "7d6b3c3c-9d22-4b10-8d3d-1f3900c13f20")
                                       @PathVariable String altKey,
                                       @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                               required = true,
                                               content = @Content(schema = @Schema(implementation = TransicionDTO.class),
                                                       examples = @ExampleObject("""
                                                               {
                                                                 "accion": "INICIAR_INVESTIGACION",
                                                                 "observacion": "Inicio de investigación institucional."
                                                               }
                                                               """)))
                                       @Valid @RequestBody TransicionDTO solicitud) {
        ContextoTerritorial ctx = proveedorContexto.actual();
        AccionCaso accion = interpretarAccion(solicitud.accion());
        ComandoTransicion comando = new ComandoTransicion(altKey, accion, solicitud.observacion());
        Caso caso = transicionarCaso.transicionar(comando, ctx);
        // En escritura no se realiza enriquecimiento (sin llamadas salientes): esqueleto.
        return mapeador.aDetalle(new CasoEnriquecido(caso, DatosEnriquecimiento.noDisponible()));
    }

    // -------------------------------- Auxiliares --------------------------------

    private String extraerToken(String autorizacion) {
        if (autorizacion != null && autorizacion.startsWith(PREFIJO_BEARER)) {
            return autorizacion.substring(PREFIJO_BEARER.length()).trim();
        }
        return null;
    }

    private EstadoCaso interpretarEstado(String estado) {
        if (estado == null || estado.isBlank()) {
            return null;
        }
        try {
            return EstadoCaso.valueOf(estado.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new SolicitudInvalidaException("Estado de filtro no válido: " + estado);
        }
    }

    private AccionCaso interpretarAccion(String accion) {
        try {
            return AccionCaso.valueOf(accion.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new SolicitudInvalidaException("Acción no válida: " + accion);
        }
    }
}
