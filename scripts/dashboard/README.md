# Panel de microservicios SMID/SIGER

Herramienta local para observar, controlar y diagnosticar el ecosistema SMID desde el
navegador. Corre solo en `127.0.0.1`, **sin dependencias** (solo Python 3 + PowerShell 7),
y usa `scripts/siger-services.ps1` para levantar/apagar/reiniciar con `mvn spring-boot:run`.

La **fuente única de verdad** es [`scripts/services.json`](../services.json): tanto el runner
PowerShell como el servidor Python la leen. **Agregar un servicio = agregar una entrada ahí**;
no se toca código.

Los microservicios viven bajo `services/<servicio>`; la ruta base se configura en
`scripts/services.json` (`paths.servicesRoot`).

## Levantar el panel

```powershell
python .\scripts\dashboard_server.py        # o:  .\scripts\siger-services.ps1 dashboard
```

Abre `http://127.0.0.1:8765/`. Opciones: `--no-open`, `--port 8770`.
Para apagarlo si quedó en background: `.\scripts\siger-services.ps1 dashboard-stop`.

## Qué ofrece la pantalla

- **Franja de infraestructura**: estado en vivo de **MySQL** (`:3306`) y **RabbitMQ**
  (`:5672`/`:15672`), la causa #1 de fallos al arrancar.
- **Tarjetas por tier** (Perímetro · Identidad · Núcleo · Referencia) con nombre legible,
  módulo (6.x), dominio, ruta del gateway, BD y eventos que emite/escucha cada servicio.
- **Diagnóstico ("Doctor")**: verifica Java/Maven/PowerShell, infra, presencia de `.env` y
  **coherencia del `JWT_SECRET`** entre todos los servicios (maneja alias como
  `JWT_SECRET` / `JWT_SECRET_ACTIVO`).
- **Control**: levantar/apagar/reiniciar por servicio o global. «Levantar todo» respeta el
  **orden de arranque por niveles** (infra → núcleo → gateway) con espera de `health UP`.
- **Visor de logs**: stdout/stderr, resaltado de ERROR/WARN/INFO, búsqueda con realce,
  **modo en vivo (SSE)**, wrap on/off.
- **Obtener token**: hace login con el usuario semilla y te copia el Bearer para probar
  las rutas `/api/*`.
- **Tema oscuro/claro** y preferencias persistidas (tema, auto-refresh, heaps, perfil).

> Los heaps y el perfil se aplican al próximo `start`/`restart`; no cambian un JVM ya corriendo.

## JDK 21 (importante)

El proyecto apunta a **Java 21**. Si tu `java` por defecto es otro (p. ej. 25), el runner
**no usa el del PATH**: resuelve un JDK 21 y lo inyecta como `JAVA_HOME` solo a los procesos
Maven que lanza (tu PATH global queda intacto). Orden de prioridad:

1. `-JavaHome "C:\ruta\al\jdk-21"` en `siger-services.ps1`.
2. Variable `SIGER_JAVA_HOME`.
3. Autodetección de `jdk-21*` en Program Files / LOCALAPPDATA\Programs.
4. Si no hay JDK 21, usa el `java` del PATH (el Doctor lo marca en amarillo).

El Doctor (web y CLI) muestra **qué Java se usará** en la fila «Java (arranque)».
Nota: bajo JDK 23+ `javac` ya no corre procesadores de anotaciones del classpath por defecto,
por eso cada servicio con Lombok declara `annotationProcessorPaths` en su `pom.xml`.

## Control por línea de comandos

```powershell
.\scripts\siger-services.ps1 start                       # todo, por niveles, esperando health
.\scripts\siger-services.ps1 start -Services casos-service
.\scripts\siger-services.ps1 start -Tier nucleo
.\scripts\siger-services.ps1 start -IncludeOptional      # incluye instituciones-service
.\scripts\siger-services.ps1 start -NoWait               # sin esperar health entre niveles
.\scripts\siger-services.ps1 stop                        # o -Services / -Tier
.\scripts\siger-services.ps1 restart
.\scripts\siger-services.ps1 status
.\scripts\siger-services.ps1 logs -Services smid-api-gateway -Follow
.\scripts\siger-services.ps1 doctor                      # diagnóstico en consola
```

## Prueba end-to-end

`scripts/e2e.ps1` recorre el ecosistema con un JWT real: auth → catálogo → personas →
requerimientos → (RabbitMQ) → casos → vulneraciones/FIR → productos, más verificaciones
negativas (401/404/409). Lee puertos y rutas de `services.json` y la contraseña de
`services/smid-auth/.env` (`SEED_PASSWORD`).

```powershell
.\scripts\e2e.ps1                 # vía Gateway (por defecto)
.\scripts\e2e.ps1 -Direct         # directo a cada servicio
.\scripts\e2e.ps1 -TimeoutSec 60  # más espera para materializaciones asíncronas
.\scripts\e2e.ps1 -RequireAsync   # trata materializaciones opcionales como fallo
.\scripts\e2e.ps1 -NoPreflight    # omite chequeos previos de health/infra/JWT
```

Antes del flujo, el script valida MySQL, RabbitMQ, health de servicios requeridos y coherencia
del `JWT_SECRET`. Cada corrida deja evidencia en `_runtime-logs/e2e/<runId>/report.json` y
`transcript.log`.

Devuelve exit code 0 si todo pasa. Nota: la FIR (vulneraciones) y la tarea-semilla
(productos) solo se materializan si esos servicios consumen eventos; en perfil `local`
corren en aislamiento (`consumo: none`), así que el script las marca como aviso, no fallo.
Para ejercitarlas, levanta vulneraciones/productos con `EVENTOS_CONSUMO=rabbitmq`.

## Agregar un servicio nuevo

Pega un bloque en `scripts/services.json` (campos: `name`, `module`, `displayName`,
`domain`, `tier`, `port`, `optional`, `startLevel`, `gatewayRoute`, `db`, `needs`, `events`).
El runner y el dashboard lo toman automáticamente. `instituciones-service` está como
`optional` hasta que tenga `.env`/`launch.json`.

## Frontends (servicios `web`)

Además de los microservicios Java, el catálogo admite **frontends** (Vite/npm). Se declaran con
`"kind": "web"` y viven fuera de `services/` (campo `path`, p. ej. `frontends/smid`):

```json
{
  "name": "frontend-smid", "kind": "web", "tier": "frontend",
  "port": 3000, "path": "frontends/smid", "command": "npm run dev",
  "url": "http://localhost:3000/", "optional": true, "startLevel": 6
}
```

Al levantarlo (tarjeta del panel o `siger-services.ps1 start -Services frontend-smid`), el runner
hace `npm install` la primera vez (si falta `node_modules`), lanza `npm run dev` y, cuando el
puerto responde, **abre el navegador** en la `url`. Su salud es «puerto abierto» (no usa
`/actuator/health`); en la ficha, el enlace de salud se vuelve **«Abrir ventana»**. Para no abrir
el navegador, usar `-NoOpen`.

## Archivos

- Fuente de verdad: `scripts/services.json`
- Estado de procesos: `_runtime-logs/siger-services.json`
- Logs por servicio: `_runtime-logs/<timestamp>/*.log`
- Logs de acciones del panel: `_runtime-logs/dashboard-actions/*.log`
- Backend: `scripts/dashboard_server.py` · Frontend: `scripts/dashboard/{index.html,styles.css,app.js}`

## Seguridad local

El panel puede ejecutar procesos en la máquina: mantenlo en `127.0.0.1`, no lo expongas a la red.
