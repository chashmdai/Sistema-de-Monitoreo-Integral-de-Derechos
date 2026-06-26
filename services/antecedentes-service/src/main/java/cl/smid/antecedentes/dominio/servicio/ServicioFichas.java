package cl.smid.antecedentes.dominio.servicio;

import cl.smid.antecedentes.dominio.excepcion.AccesoDenegadoException;
import cl.smid.antecedentes.dominio.excepcion.CodigoError;
import cl.smid.antecedentes.dominio.excepcion.RecursoNoEncontradoException;
import cl.smid.antecedentes.dominio.excepcion.ReglaNegocioException;
import cl.smid.antecedentes.dominio.modelo.AccionRevision;
import cl.smid.antecedentes.dominio.modelo.ContextoSesion;
import cl.smid.antecedentes.dominio.modelo.CriterioTerritorial;
import cl.smid.antecedentes.dominio.modelo.DocumentoAsociado;
import cl.smid.antecedentes.dominio.modelo.EstadoFicha;
import cl.smid.antecedentes.dominio.modelo.EventoDominio;
import cl.smid.antecedentes.dominio.modelo.FichaAntecedente;
import cl.smid.antecedentes.dominio.modelo.FiltroFichas;
import cl.smid.antecedentes.dominio.modelo.Folio;
import cl.smid.antecedentes.dominio.modelo.Hallazgo;
import cl.smid.antecedentes.dominio.modelo.Pagina;
import cl.smid.antecedentes.dominio.modelo.PercepcionHallazgo;
import cl.smid.antecedentes.dominio.modelo.PoliticaRoles;
import cl.smid.antecedentes.dominio.modelo.ResumenFicha;
import cl.smid.antecedentes.dominio.modelo.TipoReferencia;
import cl.smid.antecedentes.dominio.puerto.entrada.GestionFichasUseCase;
import cl.smid.antecedentes.dominio.puerto.salida.GeneradorIdentificadores;
import cl.smid.antecedentes.dominio.puerto.salida.PublicadorEventos;
import cl.smid.antecedentes.dominio.puerto.salida.RelojDominio;
import cl.smid.antecedentes.dominio.puerto.salida.RepositorioFichas;
import cl.smid.antecedentes.dominio.puerto.salida.RepositorioHallazgos;
import cl.smid.antecedentes.dominio.puerto.salida.RepositorioReferencias;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Orquestador del caso de uso {@link GestionFichasUseCase}. POJO de dominio sin anotaciones de
 * Spring; se cablea en {@code config.CableadoDominio}. La demarcacion transaccional vive en el
 * controlador (override #6): este servicio no abre transacciones.
 */
public class ServicioFichas implements GestionFichasUseCase {

    private final RepositorioFichas repositorioFichas;
    private final RepositorioHallazgos repositorioHallazgos;
    private final RepositorioReferencias repositorioReferencias;
    private final MaquinaEstadosFicha maquinaEstados;
    private final EvaluadorAlcance evaluadorAlcance;
    private final GeneradorFolio generadorFolio;
    private final RelojDominio reloj;
    private final GeneradorIdentificadores generadorId;
    private final PublicadorEventos publicador;
    private final PoliticaRoles politicaRoles;

    public ServicioFichas(RepositorioFichas repositorioFichas, RepositorioHallazgos repositorioHallazgos,
                          RepositorioReferencias repositorioReferencias, MaquinaEstadosFicha maquinaEstados,
                          EvaluadorAlcance evaluadorAlcance, GeneradorFolio generadorFolio, RelojDominio reloj,
                          GeneradorIdentificadores generadorId, PublicadorEventos publicador,
                          PoliticaRoles politicaRoles) {
        this.repositorioFichas = repositorioFichas;
        this.repositorioHallazgos = repositorioHallazgos;
        this.repositorioReferencias = repositorioReferencias;
        this.maquinaEstados = maquinaEstados;
        this.evaluadorAlcance = evaluadorAlcance;
        this.generadorFolio = generadorFolio;
        this.reloj = reloj;
        this.generadorId = generadorId;
        this.publicador = publicador;
        this.politicaRoles = politicaRoles;
    }

    @Override
    public FichaAntecedente crear(ComandoCrear comando, ContextoSesion contexto) {
        Instant ahora = reloj.ahora();
        exigirContextoTerritorial(contexto);
        validarReferencias(comando.procesoId(), comando.categoriaPrincipalId(), comando.categoriasSecundariasIds());

        String fichaAltKey = generadorId.nuevoAltKey();
        ResolucionHallazgo resolucion = resolverHallazgo(comando.percepcionHallazgo(), comando.hallazgoAlt(),
                comando.propuestaHallazgo(), null, fichaAltKey, ahora);

        Folio folio = generadorFolio.siguienteFicha();
        FichaAntecedente ficha = FichaAntecedente.crear(
                fichaAltKey, folio.valor(), contexto.idUnidad(), contexto.idSede(), contexto.usuarioAlt(),
                comando.jefaturaAlt(), comando.procesoId(), comando.casoAlt(), comando.categoriaPrincipalId(),
                comando.categoriasSecundariasIds(), comando.derechosCdn(), comando.descripcion(), comando.relato(),
                comando.calificacion(), comando.criterios(), comando.percepcionHallazgo(),
                resolucion.hallazgoAlt(), ahora);

        FichaAntecedente persistida = repositorioFichas.guardar(ficha);
        publicarCreacion(persistida, resolucion, ahora);
        return persistida;
    }

    @Override
    public FichaAntecedente obtener(String altKey, ContextoSesion contexto) {
        FichaAntecedente ficha = cargarVisible(altKey, contexto);
        return ficha;
    }

    @Override
    public FichaAntecedente editar(String altKey, ComandoEditar comando, ContextoSesion contexto) {
        Instant ahora = reloj.ahora();
        FichaAntecedente ficha = cargarEditable(altKey, contexto);
        validarReferencias(comando.procesoId(), comando.categoriaPrincipalId(), comando.categoriasSecundariasIds());

        ResolucionHallazgo resolucion = resolverHallazgo(comando.percepcionHallazgo(), comando.hallazgoAlt(),
                comando.propuestaHallazgo(), ficha.hallazgoAlt(), ficha.altKey(), ahora);

        ficha.editar(comando.jefaturaAlt(), comando.procesoId(), comando.casoAlt(), comando.categoriaPrincipalId(),
                comando.categoriasSecundariasIds(), comando.derechosCdn(), comando.descripcion(), comando.relato(),
                comando.calificacion(), comando.criterios(), comando.percepcionHallazgo(), resolucion.hallazgoAlt(),
                ahora);

        FichaAntecedente persistida = repositorioFichas.guardar(ficha);
        // En edicion solo se emiten eventos si nacio una propuesta de hallazgo nueva.
        if (resolucion.nuevoHallazgo() != null) {
            publicarPropuestaHallazgo(persistida, resolucion.nuevoHallazgo(), ahora);
        }
        return persistida;
    }

    @Override
    public void eliminar(String altKey, ContextoSesion contexto) {
        FichaAntecedente ficha = cargarEditable(altKey, contexto);
        if (!ficha.estado().esMutable()) {
            throw new ReglaNegocioException(CodigoError.ANT_422,
                    "Solo una ficha en BORRADOR puede eliminarse.");
        }
        repositorioFichas.eliminarPorAltKey(ficha.altKey());
    }

    @Override
    public FichaAntecedente transicionar(String altKey, ComandoAccionRevision comando, ContextoSesion contexto) {
        Instant ahora = reloj.ahora();
        AccionRevision accion = comando.accion();
        if (accion == null) {
            throw new ReglaNegocioException(CodigoError.ANT_422, "La accion de revision es obligatoria.");
        }
        FichaAntecedente ficha = autorizarTransicion(altKey, accion, contexto);

        EstadoFicha destino = maquinaEstados.siguiente(ficha.estado(), accion);
        ficha.aplicarTransicion(accion, destino, contexto.usuarioAlt(), comando.observacion(), ahora);

        FichaAntecedente persistida = repositorioFichas.guardar(ficha);
        publicador.publicar(EventoDominio.de(tipoEvento(accion), persistida.altKey(), ahora));
        return persistida;
    }

    @Override
    public Pagina<ResumenFicha> listar(FiltroFichas filtro, ContextoSesion contexto) {
        return repositorioFichas.buscar(filtro, CriterioTerritorial.desde(contexto));
    }

    @Override
    public FichaAntecedente agregarDocumento(String altKey, ComandoAgregarDocumento comando,
                                             ContextoSesion contexto) {
        Instant ahora = reloj.ahora();
        FichaAntecedente ficha = cargarEditable(altKey, contexto);
        if (comando.nombre() == null || comando.nombre().isBlank()) {
            throw new ReglaNegocioException(CodigoError.ANT_422, "El nombre del documento es obligatorio.");
        }
        DocumentoAsociado documento = new DocumentoAsociado(generadorId.nuevoAltKey(), comando.nombre(),
                normalizar(comando.referenciaExterna()));
        ficha.agregarDocumento(documento, ahora);
        return repositorioFichas.guardar(ficha);
    }

    // ---------------------------------------------------------------------------------------
    // Autorizacion / carga
    // ---------------------------------------------------------------------------------------

    private FichaAntecedente cargarVisible(String altKey, ContextoSesion contexto) {
        FichaAntecedente ficha = repositorioFichas.buscarPorAltKey(altKey)
                .orElseThrow(() -> noEncontrada(altKey));
        if (!evaluadorAlcance.puedeVer(contexto, ficha)) {
            throw noEncontrada(altKey);
        }
        return ficha;
    }

    /**
     * Carga una ficha para edicion/eliminacion: exige pertenecer a la misma unidad. La
     * denegacion territorial (no es de su unidad) se expresa como 404 (override #5).
     */
    private FichaAntecedente cargarEditable(String altKey, ContextoSesion contexto) {
        FichaAntecedente ficha = repositorioFichas.buscarPorAltKey(altKey)
                .orElseThrow(() -> noEncontrada(altKey));
        if (!evaluadorAlcance.perteneceMismaUnidad(contexto, ficha)) {
            throw noEncontrada(altKey);
        }
        return ficha;
    }

    private FichaAntecedente autorizarTransicion(String altKey, AccionRevision accion, ContextoSesion contexto) {
        if (accion == AccionRevision.ENVIAR_REVISION) {
            // Autor o miembro de la unidad (seccion 7 de la especificacion).
            return cargarEditable(altKey, contexto);
        }
        // Devolver / aprobar / rechazar: visibilidad territorial (404) y rol revisor (403).
        FichaAntecedente ficha = cargarVisible(altKey, contexto);
        if (!contexto.tieneAlgunRol(politicaRoles.rolesRevision())) {
            throw new AccesoDenegadoException(
                    "La accion de revision exige un rol revisor.");
        }
        return ficha;
    }

    private void exigirContextoTerritorial(ContextoSesion contexto) {
        if (contexto.idUnidad() == null || contexto.idUnidad().isBlank()
                || contexto.idSede() == null || contexto.idSede().isBlank()) {
            throw new ReglaNegocioException(CodigoError.ANT_422,
                    "El contexto del solicitante no tiene unidad/sede para crear una ficha territorial.");
        }
    }

    private void validarReferencias(String procesoId, String categoriaPrincipalId,
                                    List<String> categoriasSecundariasIds) {
        if (!repositorioReferencias.existeVigentePorAltKey(TipoReferencia.PROCESO, procesoId)) {
            throw new ReglaNegocioException(CodigoError.ANT_422,
                    "El proceso indicado no existe o no esta vigente.");
        }
        if (!repositorioReferencias.existeVigentePorAltKey(TipoReferencia.CATEGORIA, categoriaPrincipalId)) {
            throw new ReglaNegocioException(CodigoError.ANT_422,
                    "La categoria principal no existe o no esta vigente.");
        }
        if (categoriasSecundariasIds != null) {
            for (String id : categoriasSecundariasIds) {
                if (id != null && !id.isBlank()
                        && !repositorioReferencias.existeVigentePorAltKey(TipoReferencia.CATEGORIA, id)) {
                    throw new ReglaNegocioException(CodigoError.ANT_422,
                            "Una categoria secundaria no existe o no esta vigente.");
                }
            }
        }
    }

    // ---------------------------------------------------------------------------------------
    // Resolucion del vinculo de hallazgo (coherencia de percepcion)
    // ---------------------------------------------------------------------------------------

    private ResolucionHallazgo resolverHallazgo(PercepcionHallazgo percepcion, String hallazgoAltComando,
                                                GestionFichasUseCase.DatosPropuestaHallazgo propuesta,
                                                String hallazgoAltActual, String fichaAltKey, Instant ahora) {
        if (percepcion == null) {
            throw new ReglaNegocioException(CodigoError.ANT_422, "La percepcion de hallazgo es obligatoria.");
        }
        return switch (percepcion) {
            case NO_ES_HALLAZGO -> {
                if (presente(hallazgoAltComando) || propuesta != null) {
                    throw new ReglaNegocioException(CodigoError.ANT_422,
                            "NO_ES_HALLAZGO no admite hallazgo ni instrumento.");
                }
                yield new ResolucionHallazgo(null, null);
            }
            case ANTECEDENTE_DE_HALLAZGO -> {
                if (propuesta != null) {
                    throw new ReglaNegocioException(CodigoError.ANT_422,
                            "ANTECEDENTE_DE_HALLAZGO no debe incluir una propuesta de hallazgo.");
                }
                if (!presente(hallazgoAltComando)) {
                    throw new ReglaNegocioException(CodigoError.ANT_422,
                            "ANTECEDENTE_DE_HALLAZGO exige el hallazgo existente.");
                }
                if (!repositorioHallazgos.existePorAltKey(hallazgoAltComando.trim())) {
                    throw new ReglaNegocioException(CodigoError.ANT_422,
                            "El hallazgo referenciado no existe.");
                }
                yield new ResolucionHallazgo(hallazgoAltComando.trim(), null);
            }
            case SE_PROPONE_HALLAZGO -> {
                if (presente(hallazgoAltComando)) {
                    throw new ReglaNegocioException(CodigoError.ANT_422,
                            "SE_PROPONE_HALLAZGO no referencia un hallazgo existente; envie la propuesta.");
                }
                if (presente(hallazgoAltActual)) {
                    // La ficha ya tiene un hallazgo propuesto: se conserva y se rechaza re-proponer.
                    if (propuesta != null) {
                        throw new ReglaNegocioException(CodigoError.ANT_422,
                                "La ficha ya tiene un hallazgo propuesto; no se puede proponer otro.");
                    }
                    yield new ResolucionHallazgo(hallazgoAltActual, null);
                }
                if (propuesta == null) {
                    throw new ReglaNegocioException(CodigoError.ANT_422,
                            "SE_PROPONE_HALLAZGO exige los datos de la propuesta (instrumento incluido).");
                }
                if (!presente(propuesta.instrumentoId())
                        || !repositorioReferencias.existeVigentePorAltKey(
                                TipoReferencia.INSTRUMENTO, propuesta.instrumentoId())) {
                    throw new ReglaNegocioException(CodigoError.ANT_422,
                            "El instrumento de la propuesta no existe o no esta vigente.");
                }
                String hallazgoAltKey = generadorId.nuevoAltKey();
                Folio folio = generadorFolio.siguienteHallazgo();
                Hallazgo hallazgo = Hallazgo.proponer(hallazgoAltKey, folio.valor(), propuesta.titulo(),
                        propuesta.descripcion(), propuesta.instrumentoId(), propuesta.temporalidad(),
                        propuesta.unidadesInvolucradas(), propuesta.institucionesExternas(), fichaAltKey, ahora);
                Hallazgo persistido = repositorioHallazgos.guardar(hallazgo);
                yield new ResolucionHallazgo(persistido.altKey(), persistido);
            }
        };
    }

    // ---------------------------------------------------------------------------------------
    // Eventos
    // ---------------------------------------------------------------------------------------

    private void publicarCreacion(FichaAntecedente ficha, ResolucionHallazgo resolucion, Instant ahora) {
        Map<String, Object> metadatos = new LinkedHashMap<>();
        metadatos.put("unidadAlt", ficha.unidadAlt());
        metadatos.put("calificacion", ficha.calificacion().name());
        if (ficha.casoAlt() != null) {
            metadatos.put("casoAlt", ficha.casoAlt());
        }
        publicador.publicar(new EventoDominio("ficha.creada", ficha.altKey(), ahora, metadatos));
        if (resolucion.nuevoHallazgo() != null) {
            publicarPropuestaHallazgo(ficha, resolucion.nuevoHallazgo(), ahora);
        }
    }

    private void publicarPropuestaHallazgo(FichaAntecedente ficha, Hallazgo hallazgo, Instant ahora) {
        publicador.publicar(new EventoDominio("ficha.propuesta_hallazgo", ficha.altKey(), ahora,
                Map.of("hallazgoAlt", hallazgo.altKey())));
        publicador.publicar(new EventoDominio("hallazgo.creado", hallazgo.altKey(), ahora,
                Map.of("temporalidad", hallazgo.temporalidad().name())));
    }

    private static String tipoEvento(AccionRevision accion) {
        return switch (accion) {
            case ENVIAR_REVISION -> "ficha.enviada_revision";
            case DEVOLVER -> "ficha.devuelta";
            case APROBAR -> "ficha.aprobada";
            case RECHAZAR -> "ficha.rechazada";
        };
    }

    // ---------------------------------------------------------------------------------------
    // Utilidades
    // ---------------------------------------------------------------------------------------

    private static boolean presente(String valor) {
        return valor != null && !valor.isBlank();
    }

    private static String normalizar(String valor) {
        if (valor == null) {
            return null;
        }
        String limpio = valor.trim();
        return limpio.isEmpty() ? null : limpio;
    }

    private static RecursoNoEncontradoException noEncontrada(String altKey) {
        return new RecursoNoEncontradoException("No existe una ficha accesible con identificador " + altKey + ".");
    }

    /**
     * Resultado de resolver el vinculo de hallazgo: el alt_key a fijar en la ficha y, si
     * corresponde, el hallazgo recien propuesto (no nulo solo cuando se creo uno).
     */
    private record ResolucionHallazgo(String hallazgoAlt, Hallazgo nuevoHallazgo) {
    }
}
