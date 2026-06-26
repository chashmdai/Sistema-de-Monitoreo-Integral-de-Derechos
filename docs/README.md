# Corpus documental SMID/SIGER

Esta carpeta conserva el material de referencia usado para entender el sistema legado SIGER,
justificar la arquitectura SMID y trazar la migracion funcional. Es parte del repositorio de
integracion porque explica decisiones de dominio, riesgos normativos y continuidad operacional.

> Nota: las leyes incluidas son copias de trabajo. Antes de tomar decisiones juridicas o de
> cumplimiento, verificar siempre la version vigente en la fuente oficial.

## Lectura recomendada

1. **Contexto y riesgo institucional**
   - [`Informe_Obsolescencia_SIGER_Arquitectura_SMID_DDN_v1.0.docx`](Informe_Obsolescencia_SIGER_Arquitectura_SMID_DDN_v1.0.docx)
   - [`Auditoría de Arquitectura y Usabilidad SIGER.docx`](Auditori%CC%81a%20de%20Arquitectura%20y%20Usabilidad%20SIGER.docx)
2. **Minutas directivas**
   - [`Minuta_1_Diagnostico_Normativo_SIGER_DDN.docx`](Minuta_1_Diagnostico_Normativo_SIGER_DDN.docx)
   - [`Minuta_2_Urgencia_Legal_Ley21719_DDN.docx`](Minuta_2_Urgencia_Legal_Ley21719_DDN.docx)
   - [`Minuta_3_Hoja_de_Ruta_SMID_Compromiso_DDN.docx`](Minuta_3_Hoja_de_Ruta_SMID_Compromiso_DDN.docx)
3. **Especificacion y operacion funcional del legado**
   - [`2019.07.22 Detalle de requerimientos - Gestión de requerimientos v3.0.docx`](2019.07.22%20Detalle%20de%20requerimientos%20-%20Gesti%C3%B3n%20de%20requerimientos%20v3.0.docx)
   - [`USR.01 - Creación de requerimiento v2.1.pdf`](USR.01%20-%20Creaci%C3%B3n%20de%20requerimiento%20v2.1.pdf)
   - [`USR.02 - Admisibilidad y derivación de requerimientos v2.0.pdf`](USR.02%20-%20Admisibilidad%20y%20derivaci%C3%B3n%20de%20requerimientos%20v2.0.pdf)
   - [`USR.03 - Gestión de requerimientos y casos v2.0.pdf`](USR.03%20-%20Gesti%C3%B3n%20de%20requerimientos%20y%20casos%20v2.0.pdf)
4. **Base normativa**
   - [`Ley-21067_29-ENE-2018.pdf`](Ley-21067_29-ENE-2018.pdf)
   - [`Ley-21430_15-MAR-2022 (1).pdf`](Ley-21430_15-MAR-2022%20%281%29.pdf)
   - [`Ley-21719_13-DIC-2024.pdf`](Ley-21719_13-DIC-2024.pdf)
   - [`LEY-18575_05-DIC-1986.pdf`](LEY-18575_05-DIC-1986.pdf)
5. **Evidencia tecnica complementaria**
   - [`ai_req-sin_datos_clienes.sql`](ai_req-sin_datos_clienes.sql)
   - [`PR658 - DEFENSORIA - Sistema de Gestión de Requerimientos v5.0.pdf`](PR658%20-%20DEFENSORIA%20-%20Sistema%20de%20Gesti%C3%B3n%20de%20Requerimientos%20v5.0.pdf)
   - [`gantt_smid.html`](gantt_smid.html)
   - [`presentacion siger.pdf`](presentacion%20siger.pdf)

## Como se conecta con el README principal

| Area | Documentos base | Decisiones que informan |
|---|---|---|
| Requerimientos y casos | Especificacion 2019, USR.01, USR.02, USR.03 | Separacion entre ingreso, admisibilidad, asignacion, caso/expediente, FIR, productos y tareas |
| Seguridad territorial | Ley 21.067, Ley 21.430, minutas 1 y 3 | JWT compartido, alcance por sede/unidad/nacional y 404 fuera de alcance |
| Proteccion de datos | Ley 21.719, informe de obsolescencia, minuta 2 | Minimizar datos, evitar secretos en repo, registrar accesos y separar responsabilidades |
| Migracion desde legado | Auditoria, informe de obsolescencia, SQL legado, PR658 | Patron strangler, gateway unico, servicios autonomos y conservacion del legado como referencia |
| Gestion del programa | Gantt, minutas directivas | Priorizacion del MVP fundacional, marcha blanca y corte limpio hacia operacion 2027 |

## Hallazgos sinteticos

- El legado SIGER cubria requerimientos, admisibilidad, casos, FIR, puntos focales, carga de
  trabajo, historia de eventos, documentos y reportes, pero lo hacia en una plataforma monolitica
  con alto acoplamiento.
- Los manuales USR fijan el flujo operativo que SMID debe preservar: crear requerimiento,
  derivar/admitir, asignar profesional, gestionar caso, emitir productos y cerrar/seguir.
- La nueva arquitectura SMID transforma esos pasos en servicios delimitados y verificables:
  identidad, personas, requerimientos, casos, vulneraciones, productos, catalogo y motores
  heredados detras del Gateway.
- Las minutas y el informe tecnico-juridico posicionan la modernizacion como continuidad
  institucional y reduccion de riesgo, no como reescritura cosmetica.
- El SQL legado se conserva como evidencia estructural y fuente para mapear entidades historicas;
  no debe usarse como runtime del nuevo ecosistema sin revision y depuracion.

## Cuidados

- No agregar `.env`, respaldos completos con datos personales, dumps productivos ni archivos de
  runtime en esta carpeta.
- Si se agregan nuevas versiones de leyes o informes, mantener el nombre con fecha/version y
  actualizar esta tabla.
- Si un documento contiene datos personales o secretos, crear una version expurgada antes de
  subirla al repositorio remoto.
