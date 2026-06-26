#!/usr/bin/env python3
"""Dashboard local para el ecosistema SMID/SIGER.

Solo libreria estandar: clona y corre `python scripts/dashboard_server.py`.
La fuente unica de verdad es services.json (servicios, infra, jwt, auth).
"""
from __future__ import annotations

import argparse
import ctypes
import glob
import json
import os
import platform
import re
import shutil
import socket
import subprocess
import sys
import threading
import time
import urllib.error
import urllib.parse
import urllib.request
import uuid
from concurrent.futures import ThreadPoolExecutor, as_completed
from http import HTTPStatus
from http.server import SimpleHTTPRequestHandler, ThreadingHTTPServer
from pathlib import Path
from typing import Any


SCRIPT_DIR = Path(__file__).resolve().parent
WORKSPACE = SCRIPT_DIR.parent
STATE_FILE = WORKSPACE / "_runtime-logs" / "siger-services.json"
ACTION_LOG_DIR = WORKSPACE / "_runtime-logs" / "dashboard-actions"
STATIC_ROOT = SCRIPT_DIR / "dashboard"
RUNNER_SCRIPT = SCRIPT_DIR / "siger-services.ps1"
CATALOG_FILE = SCRIPT_DIR / "services.json"
DEFAULT_SERVICES_ROOT = "services"

ACTION_LOCK = threading.Lock()
ACTIONS: dict[str, dict[str, Any]] = {}
HEAP_RE = re.compile(r"^\d+[mMgG]$")

ERROR_RE = re.compile(r"\b(error|exception|fatal|severe)\b", re.IGNORECASE)
WARN_RE = re.compile(r"\bwarn(ing)?\b", re.IGNORECASE)
INFO_RE = re.compile(r"\binfo\b", re.IGNORECASE)

# Claves sensibles que, si aparecen con valor literal en application-local.yml,
# pisan al .env (en Spring el perfil-yml gana sobre el placeholder ${VAR}).
LOCAL_OVERRIDE_RE = re.compile(
    r"^\s*(username|password|secreto-activo|secreto-previo|secreto|secret|clave|cifrado|consumo|roles-[a-z-]+)\s*:\s*(\S.*?)\s*$",
    re.IGNORECASE,
)


# ---------------------------------------------------------------------------
# Catalogo (services.json)
# ---------------------------------------------------------------------------

_CATALOG_CACHE: dict[str, Any] = {}
_CATALOG_MTIME: float = 0.0


def load_catalog() -> dict[str, Any]:
    """Lee services.json con recarga automatica si cambia en disco."""
    global _CATALOG_CACHE, _CATALOG_MTIME
    try:
        mtime = CATALOG_FILE.stat().st_mtime
    except OSError:
        return _CATALOG_CACHE or {"services": [], "infra": [], "tiers": {}, "jwt": {}, "auth": {}}

    if not _CATALOG_CACHE or mtime != _CATALOG_MTIME:
        _CATALOG_CACHE = json.loads(CATALOG_FILE.read_text(encoding="utf-8"))
        _CATALOG_MTIME = mtime
    return _CATALOG_CACHE


def services() -> list[dict[str, Any]]:
    return load_catalog().get("services", [])


def workspace_path(value: str | None, default: str) -> Path:
    candidate = Path(value or default)
    if candidate.is_absolute():
        return candidate
    return WORKSPACE / candidate


def services_root(catalog: dict[str, Any] | None = None) -> Path:
    catalog = catalog or load_catalog()
    paths = catalog.get("paths", {})
    root_value = paths.get("servicesRoot") if isinstance(paths, dict) else None
    return workspace_path(root_value, DEFAULT_SERVICES_ROOT)


def service_dir(service_name: str, catalog: dict[str, Any] | None = None) -> Path:
    return services_root(catalog) / service_name


def service_names() -> set[str]:
    return {s["name"] for s in services()}


def service_auto_start(spec: dict[str, Any]) -> bool:
    return bool(spec.get("autoStart", not bool(spec.get("optional"))))


# ---------------------------------------------------------------------------
# Estado de procesos
# ---------------------------------------------------------------------------

def read_state() -> list[dict[str, Any]]:
    if not STATE_FILE.exists():
        return []
    try:
        value = json.loads(STATE_FILE.read_text(encoding="utf-8-sig"))
    except (json.JSONDecodeError, OSError):
        return []
    if isinstance(value, list):
        return [item for item in value if isinstance(item, dict)]
    if isinstance(value, dict):
        return [value]
    return []


def get_state_by_service() -> dict[str, dict[str, Any]]:
    result: dict[str, dict[str, Any]] = {}
    for item in read_state():
        name = item.get("service")
        if isinstance(name, str):
            result[name] = item
    return result


def is_windows() -> bool:
    return platform.system().lower() == "windows"


def windows_process_info(pid: int) -> dict[str, Any]:
    access = 0x1000 | 0x0010
    kernel32 = ctypes.windll.kernel32
    psapi = ctypes.windll.psapi
    handle = kernel32.OpenProcess(access, False, pid)
    if not handle:
        return {"alive": False, "memory_mb": None}

    class ProcessMemoryCounters(ctypes.Structure):
        _fields_ = [
            ("cb", ctypes.c_ulong),
            ("PageFaultCount", ctypes.c_ulong),
            ("PeakWorkingSetSize", ctypes.c_size_t),
            ("WorkingSetSize", ctypes.c_size_t),
            ("QuotaPeakPagedPoolUsage", ctypes.c_size_t),
            ("QuotaPagedPoolUsage", ctypes.c_size_t),
            ("QuotaPeakNonPagedPoolUsage", ctypes.c_size_t),
            ("QuotaNonPagedPoolUsage", ctypes.c_size_t),
            ("PagefileUsage", ctypes.c_size_t),
            ("PeakPagefileUsage", ctypes.c_size_t),
        ]

    counters = ProcessMemoryCounters()
    counters.cb = ctypes.sizeof(counters)
    memory_mb = None
    try:
        if psapi.GetProcessMemoryInfo(handle, ctypes.byref(counters), counters.cb):
            memory_mb = round(counters.WorkingSetSize / 1024 / 1024, 1)
    finally:
        kernel32.CloseHandle(handle)
    return {"alive": True, "memory_mb": memory_mb}


def process_info(pid: Any) -> dict[str, Any]:
    try:
        int_pid = int(pid)
    except (TypeError, ValueError):
        return {"alive": False, "memory_mb": None}
    if int_pid <= 0:
        return {"alive": False, "memory_mb": None}
    if is_windows():
        return windows_process_info(int_pid)
    try:
        os.kill(int_pid, 0)
    except OSError:
        return {"alive": False, "memory_mb": None}
    return {"alive": True, "memory_mb": None}


def port_open(host: str, port: int, timeout: float = 0.18) -> bool:
    try:
        with socket.create_connection((host, port), timeout=timeout):
            return True
    except OSError:
        return False


def read_health(url: str, has_port: bool) -> dict[str, Any]:
    if not has_port:
        return {"health": "NO_RESPONSE", "detail": "Puerto cerrado"}
    try:
        with urllib.request.urlopen(url, timeout=3.0) as response:
            raw = response.read(32768).decode("utf-8", errors="replace")
            try:
                payload = json.loads(raw)
            except json.JSONDecodeError:
                return {"health": f"HTTP_{response.status}", "detail": ""}
            status = payload.get("status") if isinstance(payload, dict) else None
            return {"health": str(status or f"HTTP_{response.status}"), "detail": ""}
    except urllib.error.HTTPError as error:
        return {"health": f"HTTP_{error.code}", "detail": error.reason or ""}
    except Exception as error:
        return {"health": "NO_HEALTH", "detail": str(error)[:180]}


def classify(process_alive: bool, has_port: bool, health: str) -> str:
    if health == "UP":
        return "up" if process_alive else "untracked"
    if process_alive and has_port:
        return "warning"
    if process_alive:
        return "starting"
    if has_port:
        return "untracked"
    return "down"


# ---------------------------------------------------------------------------
# .env y coherencia JWT
# ---------------------------------------------------------------------------

def parse_dotenv(path: Path) -> dict[str, str]:
    vars: dict[str, str] = {}
    if not path.exists():
        return vars
    for line in path.read_text(encoding="utf-8", errors="replace").splitlines():
        line = line.strip()
        if not line or line.startswith("#"):
            continue
        if line.startswith("export "):
            line = line[7:].strip()
        if "=" not in line:
            continue
        key, _, value = line.partition("=")
        key = key.strip()
        value = value.strip()
        if len(value) >= 2 and value[0] == value[-1] and value[0] in "\"'":
            value = value[1:-1]
        vars[key] = value
    return vars


def env_active_value(vars: dict[str, str], names: list[str]) -> str | None:
    for name in names:
        if vars.get(name):
            return vars[name]
    return None


def find_java21() -> str | None:
    """Resuelve un JDK 21 (igual que el .ps1): SIGER_JAVA_HOME o autodeteccion de jdk-21*."""
    explicit = os.environ.get("SIGER_JAVA_HOME")
    if explicit and (Path(explicit) / "bin" / "java.exe").exists():
        return explicit
    patterns: list[str] = []
    for base in (os.environ.get("ProgramFiles"), os.environ.get("ProgramFiles(x86)"),
                 os.path.join(os.environ.get("LOCALAPPDATA", ""), "Programs")):
        if base:
            patterns.append(os.path.join(base, "jdk-21*"))
            patterns.append(os.path.join(base, "*", "jdk-21*"))
    hits = [d for pat in patterns for d in glob.glob(pat) if (Path(d) / "bin" / "java.exe").exists()]
    return sorted(hits, reverse=True)[0] if hits else None


def scan_local_overrides(service_name: str) -> list[tuple[str, str]]:
    """Devuelve [(clave, severidad)] de valores literales en application-local.yml
    que pisan el .env (los que usan ${VAR} no se reportan)."""
    path = service_dir(service_name) / "src" / "main" / "resources" / "application-local.yml"
    findings: list[tuple[str, str]] = []
    if not path.exists():
        return findings
    for line in path.read_text(encoding="utf-8", errors="replace").splitlines():
        m = LOCAL_OVERRIDE_RE.match(line)
        if not m:
            continue
        key, value = m.group(1).lower(), m.group(2).strip()
        if value.startswith("${") or value.startswith("#"):
            continue
        severity = "fail" if ("secret" in key or "secreto" in key or "clave" in key) else "warn"
        findings.append((key, severity))
    return findings


# ---------------------------------------------------------------------------
# Logs
# ---------------------------------------------------------------------------

def tail_lines(path: Path, limit: int = 220) -> list[str]:
    if not path.exists() or not path.is_file():
        return ["No hay log disponible todavia."]
    size = path.stat().st_size
    read_size = min(size, 256 * 1024)
    with path.open("rb") as handle:
        handle.seek(max(0, size - read_size))
        data = handle.read()
    return data.decode("utf-8", errors="replace").splitlines()[-limit:]


def read_tail_text(path_value: Any, max_bytes: int = 256 * 1024) -> str:
    if not path_value:
        return ""
    path = Path(str(path_value))
    if not path.exists() or not path.is_file():
        return ""
    try:
        size = path.stat().st_size
        with path.open("rb") as handle:
            handle.seek(max(0, size - max_bytes))
            return handle.read().decode("utf-8", errors="replace")
    except OSError:
        return ""


def log_stats_for_state(state: dict[str, Any] | None) -> dict[str, int]:
    empty = {"lines": 0, "errors": 0, "warnings": 0, "infos": 0}
    if not state:
        return empty
    text = "\n".join(
        part for part in (read_tail_text(state.get("stdout")), read_tail_text(state.get("stderr"))) if part
    )
    if not text:
        return empty
    lines = text.splitlines()
    errors = warnings = infos = 0
    for line in lines:
        if ERROR_RE.search(line):
            errors += 1
        elif WARN_RE.search(line):
            warnings += 1
        elif INFO_RE.search(line):
            infos += 1
    return {"lines": len(lines), "errors": errors, "warnings": warnings, "infos": infos}


# ---------------------------------------------------------------------------
# Payloads
# ---------------------------------------------------------------------------

def inspect_service(spec: dict[str, Any], state: dict[str, Any] | None) -> dict[str, Any]:
    port = int(spec["port"])
    pid = state.get("pid") if state else None
    proc = process_info(pid)
    has_port = port_open("127.0.0.1", port)
    kind = spec.get("kind", "java")
    optional = bool(spec.get("optional"))
    auto_start = service_auto_start(spec)

    if kind == "web":
        # Un frontend 'web' (Vite) no tiene /actuator/health: su salud es el puerto.
        url = spec.get("url") or f"http://localhost:{port}/"
        health_url = url
        health = {"health": "UP" if has_port else "", "detail": ""}
        swagger_url = None
        directory = str(WORKSPACE / spec["path"]) if spec.get("path") else str(service_dir(spec["name"]))
    else:
        health_url = f"http://localhost:{port}/actuator/health"
        health = read_health(health_url, has_port)
        swagger_url = f"http://localhost:{port}/swagger-ui/index.html"
        directory = str(service_dir(spec["name"]))
    overall = classify(proc["alive"], has_port, health["health"])

    return {
        "name": spec["name"],
        "module": spec.get("module", ""),
        "displayName": spec.get("displayName", spec["name"]),
        "domain": spec.get("domain", ""),
        "tier": spec.get("tier", "nucleo"),
        "kind": kind,
        "url": spec.get("url", ""),
        "port": port,
        "optional": optional,
        "autoStart": auto_start,
        "startLevel": spec.get("startLevel"),
        "gatewayRoute": spec.get("gatewayRoute", ""),
        "db": spec.get("db"),
        "needs": spec.get("needs", []),
        "events": spec.get("events", {"consumes": [], "publishes": []}),
        "pid": pid,
        "process": "running" if proc["alive"] else "stopped",
        "memoryMb": proc["memory_mb"],
        "health": health["health"],
        "detail": health["detail"],
        "portOpen": has_port,
        "overall": overall,
        "startedAt": state.get("startedAt") if state else None,
        "healthUrl": health_url,
        "swaggerUrl": swagger_url,
        "stdout": state.get("stdout") if state else None,
        "stderr": state.get("stderr") if state else None,
        "logStats": log_stats_for_state(state),
        "directory": directory,
    }


def status_payload() -> dict[str, Any]:
    catalog = load_catalog()
    specs = catalog.get("services", [])
    by_service = get_state_by_service()
    items: list[dict[str, Any]] = []

    with ThreadPoolExecutor(max_workers=min(12, max(1, len(specs)))) as pool:
        futures = {pool.submit(inspect_service, spec, by_service.get(spec["name"])): spec["name"] for spec in specs}
        for future in as_completed(futures):
            items.append(future.result())

    items.sort(key=lambda i: (i["tier"], i["port"]))
    memory_mb = round(sum(i["memoryMb"] or 0 for i in items), 1)
    counts = {
        "total": len(items),
        "up": sum(1 for i in items if i["overall"] == "up"),
        "watch": sum(1 for i in items if i["overall"] in {"warning", "starting", "untracked"}),
        "down": sum(1 for i in items if i["overall"] == "down"),
        "tracked": sum(1 for i in items if i["pid"]),
        "memoryMb": memory_mb,
    }
    return {
        "generatedAt": time.strftime("%Y-%m-%dT%H:%M:%S%z"),
        "workspace": str(WORKSPACE),
        "stateFile": str(STATE_FILE),
        "tiers": catalog.get("tiers", {}),
        "services": items,
        "counts": counts,
    }


def infra_payload() -> dict[str, Any]:
    catalog = load_catalog()
    rows = []
    for infra in catalog.get("infra", []):
        host = infra.get("host", "127.0.0.1")
        is_up = port_open(host, int(infra["port"]), timeout=0.4)
        mgmt = infra.get("mgmtPort")
        rows.append({
            "key": infra["key"],
            "name": infra["name"],
            "host": host,
            "port": infra["port"],
            "mgmtPort": mgmt,
            "mgmtUp": port_open(host, int(mgmt), timeout=0.4) if mgmt else None,
            "up": is_up,
            "requiredBy": infra.get("requiredBy", ""),
        })
    return {"infra": rows}


def doctor_payload() -> dict[str, Any]:
    catalog = load_catalog()
    checks: list[dict[str, str]] = []

    def add(name: str, status: str, detail: str) -> None:
        checks.append({"check": name, "status": status, "detail": detail})

    # Toolchain — Java que realmente usaran los servicios (JAVA_HOME inyectado por el runner)
    java_home = find_java21()
    java_exe = str(Path(java_home) / "bin" / "java.exe") if java_home else shutil.which("java")
    if java_exe:
        try:
            out = subprocess.run([java_exe, "-version"], capture_output=True, text=True, timeout=5)
            text = out.stderr or out.stdout
            ver = text.splitlines()[0].strip() if text else "java"
        except Exception:
            ver = "java"
        is21 = '"21' in ver
        origin = f"JAVA_HOME={java_home}" if java_home else "java del PATH"
        add("Java (arranque)", "ok" if is21 else "warn", f"{ver} | {origin}")
    else:
        add("Java (arranque)", "fail", "No se encontro java ni un JDK 21 (se requiere JDK 21).")
    mvn = shutil.which("mvn")
    add("Maven", "ok" if mvn else "warn", mvn or "mvn no esta en PATH; los servicios con mvnw.cmd igual funcionan.")
    add("PowerShell 7", "ok" if (shutil.which("pwsh") or shutil.which("powershell")) else "fail",
        "Necesario para start/stop desde el dashboard.")

    # Infra
    for infra in catalog.get("infra", []):
        up = port_open(infra.get("host", "127.0.0.1"), int(infra["port"]), timeout=0.4)
        add(infra["name"], "ok" if up else "fail",
            f"Puerto {infra['port']} {'abierto' if up else 'cerrado'}. {infra.get('requiredBy', '')}")

    # .env + coherencia JWT
    jwt_cfg = catalog.get("jwt", {})
    secrets: dict[str, str] = {}
    for spec in catalog.get("services", []):
        # Los frontends 'web' no usan .env del ecosistema; se omiten del chequeo.
        if spec.get("kind") == "web":
            continue
        env_path = service_dir(spec["name"], catalog) / ".env"
        if not env_path.exists():
            example = (service_dir(spec["name"], catalog) / ".env.example").exists()
            optional = bool(spec.get("optional"))
            auto_start = service_auto_start(spec)
            msg = "falta .env para arranque normal" if auto_start else "opcional, sin .env"
            if example:
                msg += " (hay .env.example)"
            add(f"env: {spec['name']}", "fail" if auto_start else "warn", msg)
            continue
        vars = parse_dotenv(env_path)
        secret = env_active_value(vars, jwt_cfg.get("secretVars", ["JWT_SECRET"]))
        if secret:
            secrets[spec["name"]] = secret
        add(f"env: {spec['name']}", "ok", str(env_path))

    distinct = set(secrets.values())
    if not secrets:
        add("JWT coherente", "warn", "No se pudo leer ningun JWT_SECRET de los .env.")
    elif len(distinct) == 1:
        add("JWT coherente", "ok", f"{len(secrets)} servicios comparten el mismo JWT_SECRET.")
    else:
        add("JWT coherente", "fail",
            f"Hay {len(distinct)} secretos distintos entre: {', '.join(sorted(secrets))}. El Gateway rechazara tokens.")

    # Valores hardcodeados en application-local.yml que pisan el .env
    for spec in catalog.get("services", []):
        overrides = scan_local_overrides(spec["name"])
        if not overrides:
            continue
        keys = ", ".join(sorted({k for k, _ in overrides}))
        sev = "fail" if any(s == "fail" for _, s in overrides) else "warn"
        add(f"local yml: {spec['name']}", sev,
            f"application-local.yml hardcodea {keys} y pisa el .env. Parametrizar a ${{VAR}} para coherencia.")

    summary = {
        "ok": sum(1 for c in checks if c["status"] == "ok"),
        "warn": sum(1 for c in checks if c["status"] == "warn"),
        "fail": sum(1 for c in checks if c["status"] == "fail"),
    }
    return {"generatedAt": time.strftime("%Y-%m-%dT%H:%M:%S%z"), "checks": checks, "summary": summary}


def log_payload(service: str, log_type: str) -> dict[str, Any]:
    state = get_state_by_service().get(service)
    if not state:
        return {"service": service, "type": log_type, "path": None, "ok": False,
                "lines": ["Sin proceso registrado para este servicio."]}
    key = "stderr" if log_type == "err" else "stdout"
    path_value = state.get(key)
    if not path_value:
        return {"service": service, "type": log_type, "path": None, "ok": False,
                "lines": ["No hay ruta de log registrada."]}
    path = Path(str(path_value))
    return {"service": service, "type": log_type, "path": str(path), "ok": path.exists(), "lines": tail_lines(path)}


# ---------------------------------------------------------------------------
# Acciones (start/stop/restart via siger-services.ps1)
# ---------------------------------------------------------------------------

def now_iso() -> str:
    return time.strftime("%Y-%m-%dT%H:%M:%S%z")


def powershell_executable() -> str:
    exe = shutil.which("pwsh") or shutil.which("powershell")
    if not exe:
        raise RuntimeError("No encontre pwsh/powershell en PATH.")
    return exe


def validate_heap(value: Any, default: str) -> str:
    text = str(value or default).strip()
    if not HEAP_RE.match(text):
        raise ValueError(f"Heap invalido: {text}. Usa valores como 384m, 512m o 1g.")
    return text.lower()


def normalize_list(value: Any, valid: set[str], label: str) -> list[str]:
    if value in (None, "", []):
        return []
    items = [value] if isinstance(value, str) else [str(v) for v in value if str(v).strip()]
    unknown = [i for i in items if i not in valid]
    if unknown:
        raise ValueError(f"{label} desconocido: {', '.join(unknown)}")
    return items


def action_command(command: str, svc_list: list[str], tiers: list[str], options: dict[str, Any]) -> list[str]:
    args = [powershell_executable(), "-NoProfile", "-ExecutionPolicy", "Bypass",
            "-File", str(RUNNER_SCRIPT), command]
    if svc_list:
        args.append("-Services")
        args.extend(svc_list)
    elif tiers:
        args.append("-Tier")
        args.extend(tiers)

    if command in {"start", "restart"}:
        args.extend(["-AppXmx", options["appXmx"], "-MavenXmx", options["mavenXmx"]])
        args.extend(["-Profile", options["profile"]])
        if options.get("includeOptional") and not svc_list:
            args.append("-IncludeOptional")
        if options.get("useExampleEnv"):
            args.append("-UseExampleEnv")
        if options.get("noWait"):
            args.append("-NoWait")
    return args


def run_subprocess(args: list[str], log_path: Path) -> int:
    startupinfo = None
    creationflags = 0
    if is_windows():
        startupinfo = subprocess.STARTUPINFO()
        startupinfo.dwFlags |= subprocess.STARTF_USESHOWWINDOW
        creationflags = subprocess.CREATE_NO_WINDOW
    with log_path.open("a", encoding="utf-8", errors="replace") as log:
        log.write(f"\n$ {' '.join(args)}\n")
        log.flush()
        process = subprocess.Popen(args, cwd=str(WORKSPACE), stdout=log, stderr=subprocess.STDOUT,
                                   text=True, startupinfo=startupinfo, creationflags=creationflags)
        return process.wait()


def create_action(payload: dict[str, Any]) -> dict[str, Any]:
    action = str(payload.get("action", "")).strip().lower()
    if action not in {"start", "stop", "restart"}:
        raise ValueError("action debe ser start, stop o restart.")

    valid_services = service_names()
    valid_tiers = set(load_catalog().get("tiers", {}).keys())
    svc_list = normalize_list(payload.get("services") or payload.get("service"), valid_services, "Servicio")
    tiers = normalize_list(payload.get("tiers") or payload.get("tier"), valid_tiers, "Tier")

    options = {
        "appXmx": validate_heap(payload.get("appXmx"), "512m"),
        "mavenXmx": validate_heap(payload.get("mavenXmx"), "384m"),
        "profile": str(payload.get("profile") or "local").strip() or "local",
        "includeOptional": bool(payload.get("includeOptional")),
        "useExampleEnv": bool(payload.get("useExampleEnv")),
        "noWait": bool(payload.get("noWait")),
    }

    action_id = uuid.uuid4().hex[:12]
    ACTION_LOG_DIR.mkdir(parents=True, exist_ok=True)
    log_path = ACTION_LOG_DIR / f"{action_id}-{action}.log"

    record = {
        "id": action_id, "action": action, "services": svc_list, "tiers": tiers, "options": options,
        "status": "queued", "createdAt": now_iso(), "startedAt": None, "finishedAt": None,
        "exitCode": None, "logPath": str(log_path), "error": None,
    }
    with ACTION_LOCK:
        ACTIONS[action_id] = record
    threading.Thread(target=run_action, args=(action_id,), daemon=True).start()
    return record


def run_action(action_id: str) -> None:
    with ACTION_LOCK:
        record = ACTIONS[action_id]
        record["status"] = "running"
        record["startedAt"] = now_iso()

    action = record["action"]
    svc_list = record["services"]
    tiers = record["tiers"]
    options = record["options"]
    log_path = Path(record["logPath"])
    exit_code = 0
    error = None
    try:
        if action == "restart":
            stop_code = run_subprocess(action_command("stop", svc_list, tiers, options), log_path)
            time.sleep(1)
            start_code = run_subprocess(action_command("start", svc_list, tiers, options), log_path)
            exit_code = stop_code or start_code
        else:
            exit_code = run_subprocess(action_command(action, svc_list, tiers, options), log_path)
    except Exception as exc:
        exit_code = 1
        error = str(exc)
        with log_path.open("a", encoding="utf-8", errors="replace") as log:
            log.write(f"\nERROR: {error}\n")

    with ACTION_LOCK:
        record = ACTIONS[action_id]
        record["status"] = "succeeded" if exit_code == 0 else "failed"
        record["finishedAt"] = now_iso()
        record["exitCode"] = exit_code
        record["error"] = error


def action_payload(action_id: str | None = None) -> dict[str, Any]:
    with ACTION_LOCK:
        values = list(ACTIONS.values())
    if action_id:
        values = [v for v in values if v["id"] == action_id]
    values.sort(key=lambda v: v["createdAt"], reverse=True)
    result = []
    for item in values[:20]:
        clone = dict(item)
        log_path = Path(item["logPath"])
        clone["logTail"] = tail_lines(log_path, 80) if log_path.exists() else []
        result.append(clone)
    return {"actions": result}


# ---------------------------------------------------------------------------
# Helper de token (login con usuario semilla)
# ---------------------------------------------------------------------------

def token_payload(password: str | None) -> dict[str, Any]:
    catalog = load_catalog()
    auth = catalog.get("auth", {})
    email = auth.get("email", "")
    pwd = password or auth.get("defaultPassword", "")
    body = json.dumps({"email": email, "password": pwd}).encode("utf-8")

    gateway = next((s for s in catalog.get("services", []) if s["name"] == "smid-api-gateway"), None)
    auth_svc = next((s for s in catalog.get("services", []) if s["name"] == auth.get("service")), None)
    targets = []
    if gateway:
        targets.append(f"http://localhost:{gateway['port']}{auth.get('loginPath', '/api/auth/login')}")
    if auth_svc:
        targets.append(f"http://localhost:{auth_svc['port']}{auth.get('directLoginPath', '/auth/login')}")

    last_error = "No hay objetivo de login configurado."
    for url in targets:
        try:
            req = urllib.request.Request(url, data=body, headers={"Content-Type": "application/json"}, method="POST")
            with urllib.request.urlopen(req, timeout=4) as response:
                data = json.loads(response.read().decode("utf-8", errors="replace"))
                return {"ok": True, "via": url, "email": email,
                        "accessToken": data.get("accessToken"), "usuario": data.get("usuario")}
        except urllib.error.HTTPError as error:
            detail = error.read().decode("utf-8", errors="replace")[:300]
            last_error = f"{error.code} en {url}: {detail}"
        except Exception as error:
            last_error = f"{type(error).__name__} en {url}: {error}"
    return {"ok": False, "error": last_error}


# ---------------------------------------------------------------------------
# HTTP server
# ---------------------------------------------------------------------------

# ---------------------------------------------------------------------------
# Flujo E2E visual (server-side, sin CORS) — transmitido por SSE
# ---------------------------------------------------------------------------

def gw_base() -> str:
    cat = load_catalog()
    gw = next((s for s in cat.get("services", []) if s["name"] == "smid-api-gateway"), None)
    return f"http://localhost:{gw['port'] if gw else 8080}"


def gw_call(method: str, path: str, token: str | None = None, body: Any = None, timeout: float = 15.0):
    data = json.dumps(body).encode("utf-8") if body is not None else None
    req = urllib.request.Request(gw_base() + path, data=data, method=method)
    req.add_header("Accept", "application/json")
    if body is not None:
        req.add_header("Content-Type", "application/json")
    if token:
        req.add_header("Authorization", "Bearer " + token)
    try:
        with urllib.request.urlopen(req, timeout=timeout) as r:
            raw = r.read().decode("utf-8", errors="replace")
            return r.status, (json.loads(raw) if raw else None)
    except urllib.error.HTTPError as e:
        return e.code, e.read().decode("utf-8", errors="replace")[:200]
    except Exception as e:
        return 0, str(e)


def seed_password() -> str | None:
    return parse_dotenv(service_dir("smid-auth") / ".env").get("SEED_PASSWORD")


def iter_e2e(password: str | None = None):
    import random
    cat = load_catalog()
    auth = cat.get("auth", {})
    email = auth.get("email", "")
    pwd = password or seed_password() or auth.get("defaultPassword", "")
    suf = random.randint(1000, 9999)
    summary: list[dict[str, str]] = []

    def ev(stage, status, label, value="", detail=""):
        return {"type": "step", "stage": stage, "status": status, "label": label, "value": value, "detail": detail}

    yield ev("auth", "running", "Login en auth…")
    s, lg = gw_call("POST", "/api/auth/login", body={"email": email, "password": pwd})
    if s != 200 or not isinstance(lg, dict):
        yield ev("auth", "fail", "Login falló", detail=str(lg)[:140])
        yield {"type": "done", "ok": False, "summary": summary}
        return
    tok, u = lg["accessToken"], lg["usuario"]
    unidad, ualt = u["unidad"]["altKey"], u["altKey"]
    yield ev("auth", "done", "Autenticado", f'{u["nombres"]} {u["apellidos"]}', f'alcance {u["alcance"]} · {", ".join(u["roles"])}')

    yield ev("catalogo", "running", "Leyendo catálogo…")
    s, der = gw_call("GET", "/api/catalogo/derechos?formato=plano", tok)
    if s != 200 or not der:
        yield ev("catalogo", "fail", "Sin derechos", detail=str(der)[:140])
        yield {"type": "done", "ok": False, "summary": summary}
        return
    derAlt, causaAlt, codigo = der[0]["altKey"], None, der[0].get("codigo", "")
    for d in der:
        _, cs = gw_call("GET", f"/api/catalogo/derechos/{d['altKey']}/causas", tok)
        if isinstance(cs, list) and cs:
            derAlt, causaAlt, codigo = d["altKey"], cs[0]["altKey"], d.get("codigo", "")
            break
    yield ev("catalogo", "done", "Derecho elegido", codigo, "con causa" if causaAlt else "sin causa")

    yield ev("personas", "running", "Creando personas…")
    _, rq = gw_call("POST", "/api/personas", tok, {"tipo": "ADULTO", "nombres": f"Requirente {suf}", "apellidoPaterno": "Pérez"})
    _, nn = gw_call("POST", "/api/personas", tok, {"tipo": "NNA", "nombres": f"Camila {suf}", "apellidoPaterno": "Reyes", "fechaNacimiento": "2015-03-10"})
    yield ev("personas", "done", "Requirente + NNA", f'{rq.get("altKey","")[:8]}… / {nn.get("altKey","")[:8]}…', "ADULTO + NNA")
    summary += [{"k": "Requirente", "v": rq.get("altKey", "")}, {"k": "NNA", "v": nn.get("altKey", "")}]

    yield ev("requerimientos", "running", "Creando requerimiento…")
    _, r = gw_call("POST", "/api/requerimientos", tok, {"canal": "WEB", "complejidad": "MEDIANA", "urgencia": "AMARILLO", "idUnidadDestinoAlt": unidad, "resumen": "Demo E2E dashboard", "idRequirenteAlt": rq["altKey"], "esBeta": None})
    rAlt, fichaReq = r["altKey"], r.get("requiereFichaReservada")
    gw_call("POST", f"/api/requerimientos/{rAlt}/nna", tok, {"idPersonaAlt": nn["altKey"], "derechos": [{"idDerechoAlt": derAlt, "idCausaAlt": causaAlt}]})
    gw_call("POST", f"/api/requerimientos/{rAlt}/enviar", tok)
    _, ra = gw_call("POST", f"/api/requerimientos/{rAlt}/admisibilidad", tok, {"accion": "ASIGNACION", "idProfesionalAsignadoAlt": ualt, "observacion": "Demo"})
    yield ev("requerimientos", "done", "Requerimiento asignado", r.get("folio", ""), f'estado {ra.get("estado") if isinstance(ra, dict) else ra}')
    summary.append({"k": "Requerimiento", "v": r.get("folio", "")})

    yield ev("casos", "running", "Esperando materialización (RabbitMQ)…")
    casoAlt, caso = None, None
    for _ in range(24):
        time.sleep(1.5)
        _, pg = gw_call("GET", "/api/casos?pagina=0&tamano=100", tok)
        if isinstance(pg, dict):
            for it in pg.get("contenido", []):
                _, dt = gw_call("GET", f"/api/casos/{it['altKey']}", tok)
                if isinstance(dt, dict) and dt.get("idRequerimientoOrigen") == rAlt:
                    casoAlt, caso = dt["altKey"], dt
                    break
        if casoAlt:
            break
    if not casoAlt:
        yield ev("casos", "fail", "Caso no materializado", "", "¿RabbitMQ/consumo?")
        yield {"type": "done", "ok": False, "summary": summary}
        return
    gw_call("POST", f"/api/casos/{casoAlt}/transiciones", tok, {"accion": "INICIAR_INVESTIGACION", "observacion": "Demo"})
    yield ev("casos", "done", "Expediente abierto", caso.get("numeroExpediente", ""), "EN_INVESTIGACION")
    summary.append({"k": "Caso", "v": caso.get("numeroExpediente", "")})

    if fichaReq:
        yield ev("vulneraciones", "running", "Esperando FIR (evento)…")
        ficha = None
        for _ in range(24):
            time.sleep(1.5)
            _, pg = gw_call("GET", "/api/vulneraciones/fichas?pagina=0&tamano=100", tok)
            if isinstance(pg, dict):
                hits = [x for x in pg.get("contenido", []) if x.get("idCasoAlt") == casoAlt]
                if hits:
                    ficha = hits[0]
                    break
        if not ficha:
            yield ev("vulneraciones", "warn", "FIR no materializó", "", "aislamiento/consumo")
        else:
            fa = ficha["altKey"]
            gw_call("POST", f"/api/vulneraciones/fichas/{fa}/vulneraciones", tok, {"idDerechoAlt": derAlt, "idCausaAlt": causaAlt, "idNnaAlt": nn["altKey"], "gravedad": "GRAVE", "relato": "Relato reservado (demo, cifrado AES-GCM)", "fechaHecho": "2027-04-20"})
            gw_call("POST", f"/api/vulneraciones/fichas/{fa}/antecedentes", tok, {"tipo": "ESCOLAR", "descripcion": "Antecedente (demo)", "fecha": "2027-03-15", "fuente": "Escuela"})
            gw_call("POST", f"/api/vulneraciones/fichas/{fa}/transiciones", tok, {"accion": "CERRAR", "observacion": "Demo"})
            yield ev("vulneraciones", "done", "FIR cifrada y cerrada", ficha.get("numeroFicha", ""), "vulneración + antecedente")
            summary.append({"k": "FIR", "v": ficha.get("numeroFicha", "")})
    else:
        yield ev("vulneraciones", "warn", "Sin FIR", "", "no requiere ficha")

    yield ev("productos", "running", "Producto + tarea…")
    _, prod = gw_call("POST", "/api/productos/productos", tok, {"idCaso": casoAlt, "tipo": "INFORME", "titulo": f"Informe demo {suf}", "descripcion": "Demo"})
    pAlt = prod["altKey"]
    gw_call("POST", f"/api/productos/productos/{pAlt}/transiciones", tok, {"accion": "ENVIAR_REVISION", "observacion": "Demo"})
    _, pe = gw_call("POST", f"/api/productos/productos/{pAlt}/transiciones", tok, {"accion": "EMITIR", "observacion": "Demo"})
    numProd = pe.get("numeroProducto", "") if isinstance(pe, dict) else ""
    _, tarea = gw_call("POST", "/api/productos/tareas", tok, {"idCaso": casoAlt, "titulo": f"Tarea demo {suf}", "descripcion": "Demo", "responsableAlt": ualt, "prioridad": "ALTA"})
    tAlt = tarea["altKey"]
    gw_call("POST", f"/api/productos/tareas/{tAlt}/transiciones", tok, {"accion": "TOMAR", "observacion": "Demo"})
    gw_call("POST", f"/api/productos/tareas/{tAlt}/transiciones", tok, {"accion": "COMPLETAR", "observacion": "Demo"})
    yield ev("productos", "done", "Producto emitido + tarea", numProd, "tarea COMPLETADA")
    summary.append({"k": "Producto", "v": numProd})

    gw_call("POST", f"/api/casos/{casoAlt}/transiciones", tok, {"accion": "CERRAR", "observacion": "Demo"})
    c401, _ = gw_call("GET", f"/api/casos/{casoAlt}")
    c404, _ = gw_call("GET", "/api/casos/00000000-0000-0000-0000-000000000000", tok)
    c409, _ = gw_call("POST", f"/api/casos/{casoAlt}/transiciones", tok, {"accion": "DERIVAR_A_SEGUIMIENTO", "observacion": "x"})
    okneg = c401 == 401 and c404 == 404 and c409 == 409
    yield ev("checks", "done" if okneg else "warn", "Verificaciones negativas", f"{c401}/{c404}/{c409}", "401/404/409 esperados")

    yield {"type": "done", "ok": True, "summary": summary}


def open_browser(url: str) -> None:
    if is_windows():
        subprocess.Popen(["cmd", "/c", "start", "", url], shell=False)
        return
    opener = "open" if sys.platform == "darwin" else "xdg-open"
    subprocess.Popen([opener, url])


class DashboardHandler(SimpleHTTPRequestHandler):
    server_version = "SigerDashboard/2.0"

    def translate_path(self, path: str) -> str:
        parsed = urllib.parse.urlparse(path)
        clean = parsed.path.lstrip("/") or "index.html"
        return str(STATIC_ROOT / clean)

    def end_headers(self) -> None:
        self.send_header("Cache-Control", "no-store")
        super().end_headers()

    def send_json(self, payload: dict[str, Any], status: HTTPStatus = HTTPStatus.OK) -> None:
        body = json.dumps(payload, ensure_ascii=False).encode("utf-8")
        self.send_response(status)
        self.send_header("Content-Type", "application/json; charset=utf-8")
        self.send_header("Content-Length", str(len(body)))
        self.end_headers()
        self.wfile.write(body)

    def read_json_body(self) -> dict[str, Any]:
        length = int(self.headers.get("Content-Length", "0") or "0")
        if length <= 0:
            return {}
        raw = self.rfile.read(length).decode("utf-8", errors="replace")
        value = json.loads(raw)
        if not isinstance(value, dict):
            raise ValueError("El body JSON debe ser un objeto.")
        return value

    def stream_log(self, service: str, log_type: str) -> None:
        state = get_state_by_service().get(service)
        key = "stderr" if log_type == "err" else "stdout"
        path_value = state.get(key) if state else None
        if not path_value or not Path(str(path_value)).exists():
            self.send_json({"error": "Sin log para transmitir."}, HTTPStatus.NOT_FOUND)
            return
        path = Path(str(path_value))
        self.send_response(HTTPStatus.OK)
        self.send_header("Content-Type", "text/event-stream; charset=utf-8")
        self.send_header("Cache-Control", "no-store")
        self.send_header("Connection", "keep-alive")
        self.end_headers()
        try:
            with path.open("r", encoding="utf-8", errors="replace") as handle:
                # arranca cerca del final
                handle.seek(0, os.SEEK_END)
                size = handle.tell()
                handle.seek(max(0, size - 8192))
                handle.readline()  # descarta linea parcial
                for line in handle:
                    self._sse_send(line.rstrip("\n"))
                idle = 0
                while True:
                    line = handle.readline()
                    if line:
                        self._sse_send(line.rstrip("\n"))
                        idle = 0
                    else:
                        time.sleep(0.8)
                        idle += 1
                        if idle % 18 == 0:  # heartbeat ~15s
                            self.wfile.write(b": ping\n\n")
                            self.wfile.flush()
        except (BrokenPipeError, ConnectionResetError, OSError):
            return

    def _sse_send(self, text: str) -> None:
        self.wfile.write(f"data: {text}\n\n".encode("utf-8"))
        self.wfile.flush()

    def run_e2e_sse(self, password: str | None) -> None:
        self.send_response(HTTPStatus.OK)
        self.send_header("Content-Type", "text/event-stream; charset=utf-8")
        self.send_header("Cache-Control", "no-store")
        self.end_headers()
        try:
            for event in iter_e2e(password):
                self.wfile.write(("data: " + json.dumps(event, ensure_ascii=False) + "\n\n").encode("utf-8"))
                self.wfile.flush()
        except (BrokenPipeError, ConnectionResetError, OSError):
            return
        except Exception as error:
            try:
                self.wfile.write(("data: " + json.dumps({"type": "done", "ok": False, "error": str(error)}) + "\n\n").encode("utf-8"))
                self.wfile.flush()
            except OSError:
                pass

    def do_GET(self) -> None:
        parsed = urllib.parse.urlparse(self.path)
        route = parsed.path

        if route == "/api/status":
            self.send_json(status_payload())
            return
        if route == "/api/infra":
            self.send_json(infra_payload())
            return
        if route == "/api/doctor":
            self.send_json(doctor_payload())
            return
        if route == "/api/log":
            query = urllib.parse.parse_qs(parsed.query)
            self.send_json(log_payload(query.get("service", [""])[0], query.get("type", ["out"])[0]))
            return
        if route == "/api/log/stream":
            query = urllib.parse.parse_qs(parsed.query)
            self.stream_log(query.get("service", [""])[0], query.get("type", ["out"])[0])
            return
        if route == "/api/e2e/stream":
            query = urllib.parse.parse_qs(parsed.query)
            self.run_e2e_sse(query.get("password", [None])[0])
            return
        if route == "/api/actions":
            query = urllib.parse.parse_qs(parsed.query)
            self.send_json(action_payload(query.get("id", [None])[0]))
            return
        if route == "/":
            self.path = "/index.html"
        return super().do_GET()

    def do_POST(self) -> None:
        parsed = urllib.parse.urlparse(self.path)
        try:
            if parsed.path == "/api/action":
                record = create_action(self.read_json_body())
                self.send_json({"action": record}, HTTPStatus.ACCEPTED)
                return
            if parsed.path == "/api/token":
                body = self.read_json_body()
                self.send_json(token_payload(body.get("password")))
                return
            self.send_json({"error": "not found"}, HTTPStatus.NOT_FOUND)
        except Exception as error:
            self.send_json({"error": str(error)}, HTTPStatus.BAD_REQUEST)

    def log_message(self, format: str, *args: Any) -> None:
        if self.path.startswith(("/api/status", "/api/infra", "/api/log/stream")):
            return
        super().log_message(format, *args)


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser(description="Dashboard local para microservicios SMID/SIGER.")
    parser.add_argument("--host", default="127.0.0.1")
    parser.add_argument("--port", type=int, default=8765)
    parser.add_argument("--no-open", action="store_true")
    return parser.parse_args()


def main() -> None:
    args = parse_args()
    STATIC_ROOT.mkdir(parents=True, exist_ok=True)
    if not CATALOG_FILE.exists():
        print(f"AVISO: no existe {CATALOG_FILE}; el dashboard no tendra servicios.")
    server = ThreadingHTTPServer((args.host, args.port), DashboardHandler)
    url = f"http://{args.host}:{args.port}/"
    print(f"Dashboard SMID: {url}")
    print("Ctrl+C para detener.")
    if not args.no_open:
        open_browser(url)
    try:
        server.serve_forever()
    except KeyboardInterrupt:
        pass
    finally:
        server.server_close()


if __name__ == "__main__":
    main()
