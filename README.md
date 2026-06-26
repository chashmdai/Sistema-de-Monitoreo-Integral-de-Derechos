# SMID — Sistema de Monitoreo Integral de Derechos

**Defensoría de los Derechos de la Niñez** · ecosistema de microservicios (núcleo *SIGER*).

SMID es la plataforma que digitaliza el ciclo completo de la defensa de derechos de niñas, niños y
adolescentes (NNA): desde el **ingreso de un requerimiento**, su **admisibilidad y asignación**, la
apertura del **caso/expediente**, la **Ficha Interna Reservada (FIR)** de vulneraciones, los
**productos y tareas**, hasta los **catálogos** y el **registro de personas**. Sobre ese núcleo se
integran, mediante el patrón *strangler*, dos **motores de IA heredados** (ESNNA y SGS) y los
**frontends** web.

Todo vive detrás de un **API Gateway** único que valida un **JWT HS256** emitido por el servicio de
**Identidad**. Cada microservicio de negocio es autónomo (su propia base de datos) y el conjunto se
opera con una herramienta local **sin dependencias** (Python + PowerShell) con dashboard web.

---

## Tabla de contenidos

1. [Arquitectura](#1-arquitectura)
2. [Mapa de servicios](#2-mapa-de-servicios)
3. [Estructura del repositorio](#3-estructura-del-repositorio)
4. [Seguridad y autenticación](#4-seguridad-y-autenticación)
5. [Cómo levantar el ecosistema](#5-cómo-levantar-el-ecosistema)
6. [El gestor de microservicios (dashboard + CLI)](#6-el-gestor-de-microservicios-dashboard--cli)
7. [Frontends](#7-frontends)
8. [Convenciones transversales](#8-convenciones-transversales)
9. [Perfiles y variables de entorno](#9-perfiles-y-variables-de-entorno)
10. [Repositorios independientes](#10-repositorios-independientes)
11. [Documentación por componente](#11-documentación-por-componente)
12. [Troubleshooting operativo](#12-troubleshooting-operativo)

---

## 1. Arquitectura

```
                       ┌──────────────────────────────────────────────┐
   navegador  ──/api──►│  API Gateway (:8080)  valida JWT · enruta     │
   (frontends)         └───────────────┬──────────────────────────────┘
                                        │ StripPrefix=1 (núcleo) / 0 (legados)
        ┌───────────────────────────────┼───────────────────────────────────┐
        ▼                ▼               ▼                ▼                    ▼
   Identidad        Núcleo Fundacional (negocio)     Datos ref.        Sistemas IA (legado)
   smid-auth   catalogo·personas·requerimientos·    instituciones     esnna · sgs
   (:8081)     casos·vulneraciones·productos·
               antecedentes
                                        │
                 MySQL 8 (una BD por servicio)   ·   RabbitMQ (eventos de dominio)
```

- **Núcleo Fundacional**: arquitectura **hexagonal** (puertos y adaptadores), dominio puro, Flyway
  dueño del esquema (`ddl-auto=validate`), Java 21 + Spring Boot 3.5.
- **Cadena de eventos** (RabbitMQ, *exchange* `smid.eventos`):
  `requerimiento.asignado` → **casos** → `caso.abierto` → **vulneraciones/productos**.
- **Legados IA** (ESNNA, SGS): arquitectura por capas; integrados tras el Gateway sin reescribir,
  alineando solo el contrato externo (ruta + JWT HS256 compartido).

---

## 2. Mapa de servicios

Fuente única de verdad: [`scripts/services.json`](scripts/services.json).

| Tier | Servicio | Módulo | Puerto | Ruta Gateway | BD | Notas |
|---|---|---|---|---|---|---|
| Perímetro | **smid-api-gateway** | — | 8080 | `/api` | — | Frontera única; valida firma/claims del JWT |
| Identidad | **smid-auth** | 6.1 | 8081 | `/api/auth` | `db_auth` | Emite access/refresh token (HS256) |
| Núcleo | **catalogo-service** | 6.7 | 8087 | `/api/catalogo` | `db_catalogo` | Taxonomía de derechos (dato de referencia) |
| Núcleo | **personas-service** | 6.2 | 8088 | `/api/personas` | `db_personas` | Registro maestro de NNA/adultos/jurídicas |
| Núcleo | **requerimientos-service** | 6.3 | 8089 | `/api/requerimientos` | `db_requerimientos` | Ingreso, admisibilidad, asignación |
| Núcleo | **casos-service** | 6.4 | 8090 | `/api/casos` | `db_casos` | Expediente; consume `requerimiento.asignado` |
| Núcleo | **vulneraciones-service** | 6.5 | 8091 | `/api/vulneraciones` | `db_vulneraciones` | FIR; consume `caso.abierto` |
| Núcleo | **productos-service** | 6.6 | 8092 | `/api/productos` | `db_productos` | Productos y tareas del caso |
| Núcleo | **antecedentes-service** | 6.8 | 8094 | `/api/antecedentes` | `db_antecedentes` | Antecedentes y hallazgos *(opcional)* |
| Referencia | **instituciones-service** | 6.10 | 8093 | `/api/instituciones` | `db_instituciones` | Catálogo nacional de instituciones *(opcional)* |
| Legado SMID | **smid-service** | — | 8085 | `/api/smid` | `smid_db` | Core legado: requerimientos SMID, evaluaciones, oficios, legislativo y reportes *(autoStart)* |
| Legado IA | **esnna-service** | — | 8086 | `/api/esnna` | `db_esnna` | Motor IA de casos ESNNA *(autoStart)* |
| Legado IA | **sgs-service** | — | 8083 | `/api/sgs` | `db_sgs` | Gestión y seguimiento con IA *(autoStart)* |
| Frontend | **frontend-smid** | — | 3000 | (proxy `/api`) | — | SPA Vite/React *(autoStart)* |

Infraestructura: **MySQL 8** (`:3306`, una BD por servicio) y **RabbitMQ** (`:5672`, mgmt `:15672`).
El botón/CLI **Levantar todo** usa `autoStart=true`: levanta el stack operativo completo y deja fuera
solo los experimentales (`antecedentes-service`, `instituciones-service`) salvo que se pida
`-IncludeOptional`.

---

## 3. Estructura del repositorio

```
SIGER/
├── services/                 Microservicios backend (Java 21 / Spring Boot 3.5)
│   ├── smid-api-gateway/     Gateway (Spring Cloud Gateway, WebFlux)
│   ├── smid-auth/            Identidad (6.1)
│   ├── catalogo-service/     … resto del Núcleo (6.x)
│   ├── …
│   ├── smid-service/         Core legado SMID
│   ├── esnna-service/        Legado IA (motor ESNNA)  + DISENO.md
│   └── sgs-service/          Legado IA (gestión y seguimiento)
├── frontends/
│   ├── smid/                 SPA del ecosistema (Vite + React + Mantine)
│   └── siger/                Reservado para el frontend de SIGER (vacío)
├── scripts/                  Gestor de microservicios (fuente única + tooling)
│   ├── services.json         ★ Fuente única de verdad del ecosistema
│   ├── siger-services.ps1     Runner CLI (start/stop/status/logs/doctor)
│   ├── dashboard_server.py    Backend del panel (Python, sin dependencias)
│   ├── dashboard/             Frontend del panel (HTML/CSS/JS)
│   ├── e2e.ps1                Prueba end-to-end del ecosistema
│   └── demo-siger.ps1         Demo guiada
├── api-docs/                 Contratos OpenAPI exportados por servicio
├── assets/brand/             Identidad visual SMID
├── _runtime-logs/            Logs y estado de ejecución (no se versiona)
├── Abrir SIGER Dashboard.bat Lanzador del panel
└── Ejecutar Demo SIGER.bat   Lanzador de la demo
```

---

## 4. Seguridad y autenticación

- **`smid-auth`** emite un **JWT HS256** (con `kid`) y un **refresh token**. Login:
  `POST /api/auth/login` con `{ email, password }` → `{ accessToken, refreshToken, expiraEn, usuario }`.
- **Cada servicio revalida el token** (defensa en profundidad), no confía solo en el Gateway.
- El **secreto HMAC (`JWT_SECRET`) es compartido** por todo el ecosistema: debe coincidir byte a
  byte (el *Doctor* del dashboard lo verifica). Los legados (ESNNA/SGS) validan con la misma firma.
- Claims del Núcleo: `sub` (alt_key del usuario), `iss=smid-auth`, `aud` contiene `smid-servicios`,
  `roles[]`, `idSede`, `idUnidad`, `alcance` (`UNIDAD`|`SEDE`|`NACIONAL`).
- **Acceso territorial** registro a registro: fuera de alcance ⇒ 404 (no 403), para no revelar
  existencia de registros de otras jurisdicciones.

---

## 5. Cómo levantar el ecosistema

### 5.1 Requisitos

| Para | Necesitas |
|---|---|
| Backend | **JDK 21**, Maven (o los `mvnw` incluidos), **MySQL 8**, **RabbitMQ** |
| Frontend | **Node.js** (18+) |
| Gestor / dashboard | **Python 3** y **PowerShell 7** (`pwsh`) |
| ESNNA / SGS | Clave **OpenAI**; SGS además **MinIO** |

Cada servicio arranca con su base creada y **vacía** (Flyway crea el esquema). Los secretos se
proveen por `.env` (nunca versionado); ver `.env.example` de cada servicio.

Checklist para una máquina nueva:

1. Clonar este repo y abrir PowerShell en la raíz (`SIGER/`).
2. Confirmar JDK 21: `.\scripts\siger-services.ps1 doctor`.
3. Levantar MySQL y RabbitMQ antes de arrancar servicios.
4. Crear/copiar los `.env` desde `.env.example` en los servicios que se van a usar.
5. Verificar que todos los `.env` compartan el mismo `JWT_SECRET`.
6. Ejecutar `.\scripts\siger-services.ps1 start` o usar el dashboard.

Bases de datos esperadas en local: `db_auth`, `db_catalogo`, `db_personas`,
`db_requerimientos`, `db_casos`, `db_vulneraciones`, `db_productos`, `smid_db`,
`db_esnna`, `db_sgs`. Para opcionales experimentales: `db_antecedentes`,
`db_instituciones`.

### 5.2 Arranque rápido

```powershell
# 1) Panel de control (abre http://127.0.0.1:8765)
.\Abrir SIGER Dashboard.bat        # o:  python .\scripts\dashboard_server.py

# 2) Levantar el stack operativo por niveles, esperando health UP
#    Incluye base + SMID Core + ESNNA + SGS + Gateway + frontend
.\scripts\siger-services.ps1 start

# 3) Agregar opcionales experimentales/no afinados
.\scripts\siger-services.ps1 start -IncludeOptional
```

Credenciales semilla (perfil `local`/`dev`): `admin@defensorianinez.cl` / `Smid.Local.2026`.

Comprobación rápida después del arranque:

```powershell
.\scripts\siger-services.ps1 status
Invoke-RestMethod http://localhost:8080/actuator/health
```

Resultado esperado del stack operativo: `smid-auth`, `catalogo`, `personas`, `requerimientos`,
`casos`, `vulneraciones`, `productos`, `smid-service`, `esnna-service`, `sgs-service`, Gateway y
frontend en `UP`. Los experimentales pueden quedar `down` si no se levantaron explícitamente.

---

## 6. El gestor de microservicios (dashboard + CLI)

Herramienta local (solo `127.0.0.1`, sin dependencias) que lee `scripts/services.json` y permite
**levantar/apagar/reiniciar** por servicio o tier, ver **logs en vivo**, obtener un **Bearer** de
prueba y correr un **diagnóstico (Doctor)**. Soporta servicios Java (Maven) y **frontends `web`**
(npm). Detalle en [`scripts/dashboard/README.md`](scripts/dashboard/README.md).

```powershell
.\scripts\siger-services.ps1 status
.\scripts\siger-services.ps1 logs -Services smid-api-gateway -Follow
.\scripts\siger-services.ps1 doctor
.\scripts\e2e.ps1                 # prueba end-to-end (auth→catálogo→…→productos)
```

**Agregar un servicio** = agregar una entrada en `scripts/services.json` (Java: `kind` omitido;
frontend: `"kind":"web"` con `path`, `command`, `url`). Si debe entrar en «Levantar todo»,
marcar `"autoStart": true`; si queda en laboratorio, dejarlo opcional. El runner y el dashboard lo
toman solos.

Comandos útiles:

```powershell
.\scripts\siger-services.ps1 start                       # stack operativo
.\scripts\siger-services.ps1 start -IncludeOptional      # suma experimentales
.\scripts\siger-services.ps1 restart -Services sgs-service
.\scripts\siger-services.ps1 logs -Services smid-auth -Follow
.\scripts\siger-services.ps1 dashboard-stop
```

---

## 7. Frontends

- **`frontends/smid`** — SPA **Vite + React + TypeScript + Mantine + Tailwind**. En dev corre en
  `:3000` y proxya `/api` → Gateway `:8080`. Tiene vistas para el Núcleo (Requerimientos,
  evaluaciones, legislativo…) y para los legados (ESNNA, SGS). Login alineado al contrato de
  `smid-auth` (email + `accessToken`). Ver [`frontends/smid/README.md`](frontends/smid/README.md).
- **`frontends/siger`** — reservado (vacío) para el frontend propio de SIGER.

---

## 8. Convenciones transversales

- **Hexagonal en el Núcleo**: dominio puro, puertos/adaptadores, transacciones en el controlador.
  Los legados (ESNNA/SGS) son por capas (se documenta explícitamente, no se finge equivalencia).
- **Flyway dueño del esquema** (`ddl-auto=validate`). Excepción: ESNNA usa `update` (deuda).
- **Sobre de error unificado** en español: `{ status, error, codigo, mensaje, detalles, ruta, timestamp }`.
- **Tiempos en UTC**, IDs públicos como `alt_key` (UUID); la PK interna nunca cruza la API.
- **DT-2: cero secretos en archivos** — todo por variables de entorno.

---

## 9. Perfiles y variables de entorno

- `local` — desarrollo (trazas verbosas, SQL, datos semilla en auth).
- `prod` — producción endurecida (sin trazas verbosas, sin Swagger, actuator mínimo). Cada servicio
  Java trae `application-prod.yml`.
- Activación: `SPRING_PROFILES_ACTIVE=prod` (o `-Profile` en el runner).
- Variables: copiar `.env.example` → `.env` en cada servicio. El `JWT_SECRET` debe ser el mismo en
  todos.

---

## 10. Repositorios independientes

Este árbol es el **monorepo de integración**: reúne servicios, frontends, el gestor y la
documentación para operarlos como un solo ecosistema. Si una carpeta de servicio conserva su `.git`
local, trátalo como repositorio de trabajo independiente; para publicar el integrador en GitHub se
versiona el contenido desde el repo padre sin subir `.env`, `target/`, `node_modules/` ni logs.

Antes de empujar cambios:

```powershell
git status --short
git diff --check
.\scripts\siger-services.ps1 doctor
```

---

## 11. Documentación por componente

| Componente | Documento |
|---|---|
| Gestor / dashboard | [`scripts/dashboard/README.md`](scripts/dashboard/README.md) |
| Gateway | [`services/smid-api-gateway/README.md`](services/smid-api-gateway/README.md) |
| Identidad | [`services/smid-auth/README.md`](services/smid-auth/README.md) |
| Núcleo (6.x) | READMEs en `services/<servicio>/README.md` |
| SMID Core legado | [`services/smid-service/README.md`](services/smid-service/README.md) |
| ESNNA (legado IA) | [`services/esnna-service/README.md`](services/esnna-service/README.md) · [`DISENO.md`](services/esnna-service/DISENO.md) |
| SGS (legado IA) | [`services/sgs-service/README.md`](services/sgs-service/README.md) |
| Frontend SMID | [`frontends/smid/README.md`](frontends/smid/README.md) |
| Identidad visual | [`assets/brand/README.md`](assets/brand/README.md) |

---

## 12. Troubleshooting operativo

| Síntoma | Revisión rápida | Corrección típica |
|---|---|---|
| `doctor` marca MySQL o RabbitMQ caído | Puertos `3306`, `5672`, `15672` | Levantar servicios de infraestructura antes del stack |
| Login falla con 401 | `JWT_SECRET` distinto o auth sin semilla | Ejecutar Doctor y revisar `.env` de `smid-auth` |
| Gateway `UP` pero rutas `/api/*` fallan | Servicio destino caído o sin health | `.\scripts\siger-services.ps1 status` y logs del servicio |
| Dashboard no abre | Puerto `8765` ocupado o proceso colgado | `.\scripts\siger-services.ps1 dashboard-stop` y volver a abrir |
| Frontend sin respuesta | Puerto `3000` ocupado o Vite iniciando | Revisar logs de `frontend-smid`; el primer `npm install` tarda |
| `Levantar todo` parece lento | El runner espera `health UP` por nivel | Esperar la banda de progreso del dashboard o revisar logs |

Logs importantes:

- Estado de procesos: `_runtime-logs/siger-services.json`
- Logs por corrida: `_runtime-logs/<timestamp>/`
- Acciones del dashboard: `_runtime-logs/dashboard-actions/`
- E2E: `_runtime-logs/e2e/<runId>/`

---

<sub>Defensoría de los Derechos de la Niñez · SMID — Sistema de Monitoreo Integral de Derechos.</sub>
