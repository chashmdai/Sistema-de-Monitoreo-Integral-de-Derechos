package cl.smid.antecedentes.api;

import cl.smid.antecedentes.api.dto.CrearReferenciaRequest;
import cl.smid.antecedentes.api.dto.EditarReferenciaRequest;
import cl.smid.antecedentes.api.dto.PaginaResponse;
import cl.smid.antecedentes.api.dto.ReferenciaResponse;
import cl.smid.antecedentes.api.dto.VigenciaReferenciaRequest;
import cl.smid.antecedentes.dominio.modelo.ContextoSesion;
import cl.smid.antecedentes.dominio.modelo.Pagina;
import cl.smid.antecedentes.dominio.modelo.Referencia;
import cl.smid.antecedentes.dominio.modelo.TipoReferencia;
import cl.smid.antecedentes.dominio.puerto.entrada.GestionReferenciasUseCase;
import cl.smid.antecedentes.infraestructura.seguridad.ProveedorContexto;
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
 * API CRUD de las tres tablas de referencia locales. Las rutas {@code /antecedentes/categorias},
 * {@code /antecedentes/procesos} y {@code /antecedentes/instrumentos} se resuelven con un
 * {@code @PathVariable} restringido por expresion regular (evita colisiones con
 * {@code /fichas} y {@code /hallazgos}). Lectura nacional autenticada; escritura por rol admin.
 */
@RestController
@RequestMapping("/antecedentes")
public class ReferenciaController {

    private final GestionReferenciasUseCase gestionReferencias;
    private final ProveedorContexto proveedorContexto;
    private final MapeadorRespuesta mapeador;

    public ReferenciaController(GestionReferenciasUseCase gestionReferencias, ProveedorContexto proveedorContexto,
                                MapeadorRespuesta mapeador) {
        this.gestionReferencias = gestionReferencias;
        this.proveedorContexto = proveedorContexto;
        this.mapeador = mapeador;
    }

    @PostMapping("/{recurso:categorias|procesos|instrumentos}")
    @Transactional
    public ResponseEntity<ReferenciaResponse> crear(@PathVariable("recurso") String recurso,
                                                    @Valid @RequestBody CrearReferenciaRequest request) {
        ContextoSesion contexto = proveedorContexto.contextoActual();
        Referencia referencia = gestionReferencias.crear(tipo(recurso), request.aComando(), contexto);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapeador.aReferencia(referencia));
    }

    @GetMapping("/{recurso:categorias|procesos|instrumentos}/{alt}")
    @Transactional(readOnly = true)
    public ReferenciaResponse obtener(@PathVariable("recurso") String recurso, @PathVariable("alt") String alt) {
        ContextoSesion contexto = proveedorContexto.contextoActual();
        return mapeador.aReferencia(gestionReferencias.obtener(tipo(recurso), alt, contexto));
    }

    @PutMapping("/{recurso:categorias|procesos|instrumentos}/{alt}")
    @Transactional
    public ReferenciaResponse editar(@PathVariable("recurso") String recurso, @PathVariable("alt") String alt,
                                     @Valid @RequestBody EditarReferenciaRequest request) {
        ContextoSesion contexto = proveedorContexto.contextoActual();
        return mapeador.aReferencia(gestionReferencias.editar(tipo(recurso), alt, request.aComando(), contexto));
    }

    @PostMapping("/{recurso:categorias|procesos|instrumentos}/{alt}/activacion")
    @Transactional
    public ReferenciaResponse cambiarVigencia(@PathVariable("recurso") String recurso,
                                              @PathVariable("alt") String alt,
                                              @Valid @RequestBody VigenciaReferenciaRequest request) {
        ContextoSesion contexto = proveedorContexto.contextoActual();
        return mapeador.aReferencia(
                gestionReferencias.cambiarVigencia(tipo(recurso), alt, request.aComando(), contexto));
    }

    @GetMapping("/{recurso:categorias|procesos|instrumentos}")
    @Transactional(readOnly = true)
    public PaginaResponse<ReferenciaResponse> listar(
            @PathVariable("recurso") String recurso,
            @RequestParam(value = "texto", required = false) String texto,
            @RequestParam(value = "vigente", required = false) Boolean vigente,
            @RequestParam(value = "pagina", defaultValue = "0") int pagina,
            @RequestParam(value = "tamano", defaultValue = "20") int tamano) {
        ContextoSesion contexto = proveedorContexto.contextoActual();
        Pagina<Referencia> resultado =
                gestionReferencias.listar(tipo(recurso), texto, vigente, pagina, tamano, contexto);
        return PaginaResponse.de(resultado, mapeador::aReferencia);
    }

    private static TipoReferencia tipo(String recurso) {
        return switch (recurso) {
            case "categorias" -> TipoReferencia.CATEGORIA;
            case "procesos" -> TipoReferencia.PROCESO;
            case "instrumentos" -> TipoReferencia.INSTRUMENTO;
            default -> throw new IllegalArgumentException("Recurso de referencia no soportado: " + recurso);
        };
    }
}
