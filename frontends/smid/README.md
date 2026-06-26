# frontend-smid · SMID — Aplicación web

Frontend del ecosistema **SMID** — *Sistema de Monitoreo Integral de Derechos* de la **Defensoría
de los Derechos de la Niñez**. SPA que consume el **API Gateway** (`:8080`) y ofrece la interfaz de
los módulos del Núcleo (Requerimientos, Casos, etc.) y de los sistemas IA heredados (ESNNA, SGS).

> Es uno de los dos frontends del monorepo: `frontends/smid` (este) y `frontends/siger` (reservado,
> aún vacío). El gestor de microservicios lo levanta como servicio `kind: "web"` (ver
> [`scripts/dashboard/README.md`](../../scripts/dashboard/README.md)).

---

## 1. Stack tecnológico

| Componente | Detalle |
|---|---|
| Build / dev server | **Vite** (puerto `3000`, `strictPort`) |
| Lenguaje | TypeScript + React 18 |
| UI | **Mantine** (`@mantine/*`) + **Tailwind CSS** + `@tabler/icons-react` / `lucide-react` |
| Datos / estado servidor | **TanStack Query** (`@tanstack/react-query`) + **axios** |
| Ruteo | `react-router-dom` |
| Validación | `zod` |
| Lint | ESLint (`eslint.config.js`) |

---

## 2. Cómo se conecta al backend

En desarrollo, Vite **proxya `/api` → `http://localhost:8080`** (el API Gateway), sin reescribir la
ruta (el Gateway ya espera el prefijo `/api`). Así, el navegador habla con `localhost:3000` y las
llamadas `/api/**` llegan al Gateway, que valida el JWT y enruta a cada servicio.

```
navegador (:3000)  ──/api/**──►  Vite proxy  ──►  API Gateway (:8080)  ──►  microservicios
```

La configuración está en [`vite.config.ts`](vite.config.ts). El CORS del Gateway ya admite el
origen `http://localhost:3000`.

---

## 3. Estructura

```
src/
├── api          Clientes HTTP (axios) hacia el Gateway
├── routes       Definición de rutas y guardas de autenticación
├── pages        Vistas por dominio:
│   ├── smid     Requerimientos, evaluaciones, trámite legislativo, ...
│   ├── esnna    Casos del motor ESNNA
│   └── sgs      Oficios y seguimiento de SGS
├── layouts      Estructuras de página (navegación, shell)
├── components   Componentes reutilizables (incl. components/smid)
├── context      Contextos de React (sesión/usuario, etc.)
├── hooks        Hooks de datos y UI
├── types        Tipos TypeScript compartidos
└── theme.ts     Tema de Mantine
```

---

## 4. Ejecución

### 4.1 Desde el gestor de microservicios (recomendado)

Levantar el frontend como un servicio más del ecosistema —instala dependencias la primera vez,
lanza el dev server y **abre el navegador**:

```powershell
.\scripts\siger-services.ps1 start -Services frontend-smid
```

…o desde el **dashboard** (tier «Frontends» → *Frontend SMID* → Start). Requiere que el **Gateway
(`:8080`)** esté arriba para que las llamadas `/api` respondan.

### 4.2 Directo con npm

```bash
npm install        # solo la primera vez
npm run dev        # Vite en http://localhost:3000
npm run build      # build de producción (tsc -b && vite build) → dist/
npm run preview    # sirve el build
npm run lint
```

---

## 5. Notas

- **`node_modules` no se versiona** (se reinstala con `npm install`); por eso el primer arranque
  tarda unos minutos.
- En producción, el `build` (`dist/`) se sirve como estáticos detrás del mismo dominio que el
  Gateway, o se ajusta el destino del proxy/variable de entorno según el despliegue.
