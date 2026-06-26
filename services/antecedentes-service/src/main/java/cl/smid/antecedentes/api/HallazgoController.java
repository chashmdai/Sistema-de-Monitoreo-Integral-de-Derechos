package cl.smid.antecedentes.api;

import cl.smid.antecedentes.api.dto.CambiarEstadoHallazgoRequest;
import cl.smid.antecedentes.api.dto.CrearHallazgoRequest;
import cl.smid.antecedentes.api.dto.EditarHallazgoRequest;
import cl.smid.antecedentes.api.dto.HallazgoResponse;
import cl.smid.antecedentes.api.dto.PaginaResponse;
import cl.smid.antecedentes.dominio.modelo.ContextoSesion;
import cl.smid.antecedentes.dominio.modelo.EstadoHallazgo;
import cl.smid.antecedentes.dominio.modelo.FiltroHallazgos;
import cl.smid.antecedentes.dominio.modelo.Hallazgo;
import cl.smid.antecedentes.dominio.modelo.Pagina;
import cl.smid.antecedentes.dominio.modelo.Temporalidad;
import cl.smid.antecedentes.dominio.puerto.entrada.GestionHallazgosUseCase;
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
 * API de hallazgos (lectura nacional autenticada; escritura por rol gestion/revisor). Tras
 * {@code StripPrefix=1}, el servicio expone {@code /antecedentes/hallazgos/**}.
 */
@RestController
@RequestMapping("/antecedentes/hallazgos")
public class HallazgoController {

    private final GestionHallazgosUseCase gestionHallazgos;
    private final ProveedorContexto proveedorContexto;
    private final MapeadorRespuesta mapeador;

    public HallazgoController(GestionHallazgosUseCase gestionHallazgos, ProveedorContexto proveedorContexto,
                              MapeadorRespuesta mapeador) {
        this.gestionHallazgos = gestionHallazgos;
        this.proveedorContexto = proveedorContexto;
        this.mapeador = mapeador;
    }

    @GetMapping
    @Transactional(readOnly = true)
    public PaginaResponse<HallazgoResponse> listar(
            @RequestParam(value = "estado", required = false) EstadoHallazgo estado,
            @RequestParam(value = "temporalidad", required = false) Temporalidad temporalidad,
            @RequestParam(value = "texto", required = false) String texto,
            @RequestParam(value = "pagina", defaultValue = "0") int pagina,
            @RequestParam(value = "tamano", defaultValue = "20") int tamano) {
        ContextoSesion contexto = proveedorContexto.contextoActual();
        FiltroHallazgos filtro = new FiltroHallazgos(estado, temporalidad, texto, pagina, tamano);
        Pagina<Hallazgo> resultado = gestionHallazgos.listar(filtro, contexto);
        return PaginaResponse.de(resultado, mapeador::aHallazgo);
    }

    @GetMapping("/{alt}")
    @Transactional(readOnly = true)
    public HallazgoResponse obtener(@PathVariable("alt") String alt) {
        ContextoSesion contexto = proveedorContexto.contextoActual();
        return mapeador.aHallazgo(gestionHallazgos.obtener(alt, contexto));
    }

    @PostMapping
    @Transactional
    public ResponseEntity<HallazgoResponse> crear(@Valid @RequestBody CrearHallazgoRequest request) {
        ContextoSesion contexto = proveedorContexto.contextoActual();
        Hallazgo hallazgo = gestionHallazgos.crear(request.aComando(), contexto);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapeador.aHallazgo(hallazgo));
    }

    @PutMapping("/{alt}")
    @Transactional
    public HallazgoResponse editar(@PathVariable("alt") String alt,
                                   @Valid @RequestBody EditarHallazgoRequest request) {
        ContextoSesion contexto = proveedorContexto.contextoActual();
        return mapeador.aHallazgo(gestionHallazgos.editar(alt, request.aComando(), contexto));
    }

    @PostMapping("/{alt}/estado")
    @Transactional
    public HallazgoResponse cambiarEstado(@PathVariable("alt") String alt,
                                          @Valid @RequestBody CambiarEstadoHallazgoRequest request) {
        ContextoSesion contexto = proveedorContexto.contextoActual();
        return mapeador.aHallazgo(gestionHallazgos.cambiarEstado(alt, request.aComando(), contexto));
    }
}
