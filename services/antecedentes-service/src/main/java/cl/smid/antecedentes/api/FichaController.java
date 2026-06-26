package cl.smid.antecedentes.api;

import cl.smid.antecedentes.api.dto.AccionRevisionRequest;
import cl.smid.antecedentes.api.dto.AgregarDocumentoRequest;
import cl.smid.antecedentes.api.dto.CrearFichaRequest;
import cl.smid.antecedentes.api.dto.EditarFichaRequest;
import cl.smid.antecedentes.api.dto.FichaDetalleResponse;
import cl.smid.antecedentes.api.dto.FichaResumenResponse;
import cl.smid.antecedentes.api.dto.PaginaResponse;
import cl.smid.antecedentes.dominio.modelo.AccionRevision;
import cl.smid.antecedentes.dominio.modelo.Calificacion;
import cl.smid.antecedentes.dominio.modelo.ContextoSesion;
import cl.smid.antecedentes.dominio.modelo.EstadoFicha;
import cl.smid.antecedentes.dominio.modelo.FichaAntecedente;
import cl.smid.antecedentes.dominio.modelo.FiltroFichas;
import cl.smid.antecedentes.dominio.modelo.Pagina;
import cl.smid.antecedentes.dominio.modelo.ResumenFicha;
import cl.smid.antecedentes.dominio.puerto.entrada.GestionFichasUseCase;
import cl.smid.antecedentes.infraestructura.seguridad.ProveedorContexto;
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
import org.springframework.web.bind.annotation.RestController;

/**
 * API de fichas de antecedentes (territoriales). La demarcacion transaccional vive aqui
 * (override #6): mutaciones con {@code @Transactional}, lecturas con
 * {@code @Transactional(readOnly = true)} (necesario para resolver el relato y las colecciones
 * perezosas en el detalle). Tras {@code StripPrefix=1} en el Gateway, el servicio expone
 * {@code /antecedentes/fichas/**}.
 */
@RestController
@RequestMapping("/antecedentes/fichas")
public class FichaController {

    private final GestionFichasUseCase gestionFichas;
    private final ProveedorContexto proveedorContexto;
    private final MapeadorRespuesta mapeador;

    public FichaController(GestionFichasUseCase gestionFichas, ProveedorContexto proveedorContexto,
                           MapeadorRespuesta mapeador) {
        this.gestionFichas = gestionFichas;
        this.proveedorContexto = proveedorContexto;
        this.mapeador = mapeador;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<FichaDetalleResponse> crear(@Valid @RequestBody CrearFichaRequest request) {
        ContextoSesion contexto = proveedorContexto.contextoActual();
        FichaAntecedente ficha = gestionFichas.crear(request.aComando(), contexto);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapeador.aDetalle(ficha));
    }

    @GetMapping("/{alt}")
    @Transactional(readOnly = true)
    public FichaDetalleResponse obtener(@PathVariable("alt") String alt) {
        ContextoSesion contexto = proveedorContexto.contextoActual();
        return mapeador.aDetalle(gestionFichas.obtener(alt, contexto));
    }

    @PutMapping("/{alt}")
    @Transactional
    public FichaDetalleResponse editar(@PathVariable("alt") String alt,
                                       @Valid @RequestBody EditarFichaRequest request) {
        ContextoSesion contexto = proveedorContexto.contextoActual();
        return mapeador.aDetalle(gestionFichas.editar(alt, request.aComando(), contexto));
    }

    @DeleteMapping("/{alt}")
    @Transactional
    public ResponseEntity<Void> eliminar(@PathVariable("alt") String alt) {
        ContextoSesion contexto = proveedorContexto.contextoActual();
        gestionFichas.eliminar(alt, contexto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{alt}/enviar-revision")
    @Transactional
    public FichaDetalleResponse enviarRevision(@PathVariable("alt") String alt,
                                               @RequestBody(required = false) AccionRevisionRequest request) {
        return aplicar(alt, AccionRevision.ENVIAR_REVISION, request);
    }

    @PostMapping("/{alt}/devolver")
    @Transactional
    public FichaDetalleResponse devolver(@PathVariable("alt") String alt,
                                         @RequestBody(required = false) AccionRevisionRequest request) {
        return aplicar(alt, AccionRevision.DEVOLVER, request);
    }

    @PostMapping("/{alt}/aprobar")
    @Transactional
    public FichaDetalleResponse aprobar(@PathVariable("alt") String alt,
                                        @RequestBody(required = false) AccionRevisionRequest request) {
        return aplicar(alt, AccionRevision.APROBAR, request);
    }

    @PostMapping("/{alt}/rechazar")
    @Transactional
    public FichaDetalleResponse rechazar(@PathVariable("alt") String alt,
                                         @RequestBody(required = false) AccionRevisionRequest request) {
        return aplicar(alt, AccionRevision.RECHAZAR, request);
    }

    @PostMapping("/{alt}/documentos")
    @Transactional
    public FichaDetalleResponse agregarDocumento(@PathVariable("alt") String alt,
                                                 @Valid @RequestBody AgregarDocumentoRequest request) {
        ContextoSesion contexto = proveedorContexto.contextoActual();
        return mapeador.aDetalle(gestionFichas.agregarDocumento(alt, request.aComando(), contexto));
    }

    @GetMapping
    @Transactional(readOnly = true)
    public PaginaResponse<FichaResumenResponse> listar(
            @RequestParam(value = "estado", required = false) EstadoFicha estado,
            @RequestParam(value = "calificacion", required = false) Calificacion calificacion,
            @RequestParam(value = "casoAlt", required = false) String casoAlt,
            @RequestParam(value = "procesoId", required = false) String procesoId,
            @RequestParam(value = "texto", required = false) String texto,
            @RequestParam(value = "pagina", defaultValue = "0") int pagina,
            @RequestParam(value = "tamano", defaultValue = "20") int tamano) {
        ContextoSesion contexto = proveedorContexto.contextoActual();
        FiltroFichas filtro = new FiltroFichas(estado, calificacion, casoAlt, procesoId, texto, pagina, tamano);
        Pagina<ResumenFicha> resultado = gestionFichas.listar(filtro, contexto);
        return PaginaResponse.de(resultado, mapeador::aResumen);
    }

    private FichaDetalleResponse aplicar(String alt, AccionRevision accion, AccionRevisionRequest request) {
        ContextoSesion contexto = proveedorContexto.contextoActual();
        String observacion = (request == null) ? null : request.observacion();
        GestionFichasUseCase.ComandoAccionRevision comando =
                new GestionFichasUseCase.ComandoAccionRevision(accion, observacion);
        return mapeador.aDetalle(gestionFichas.transicionar(alt, comando, contexto));
    }
}
