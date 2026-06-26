'use strict';

const $ = (id) => document.getElementById(id);
const LS = {
  get: (k, d) => { try { const v = localStorage.getItem('smid.' + k); return v === null ? d : JSON.parse(v); } catch { return d; } },
  set: (k, v) => { try { localStorage.setItem('smid.' + k, JSON.stringify(v)); } catch {} },
};

const state = {
  services: [],
  tiers: {},
  selected: LS.get('selected', null),
  logType: 'out',
  filterMode: 'all',
  auto: LS.get('auto', true),
  live: false,
  wrap: LS.get('wrap', true),
  busy: false,
  timer: null,
  actionTimer: null,
  actionPollTimer: null,
  currentActionId: null,
  eventSource: null,
};

const LABELS = { up: 'UP', warning: 'REVISAR', starting: 'INICIANDO', untracked: 'HUÉRFANO', down: 'DOWN' };
const ACTION_LABELS = { start: 'Levantar', stop: 'Apagar', restart: 'Reiniciar' };
const ACTION_STATUS = { queued: 'en cola', running: 'ejecutando', succeeded: 'completado', failed: 'falló' };

/* ---------- utilidades ---------- */
function escapeHtml(s) {
  return String(s).replace(/[&<>"']/g, (c) => ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#039;' }[c]));
}
function toast(msg) {
  const t = $('toast');
  t.textContent = msg; t.hidden = false;
  clearTimeout(toast._t);
  toast._t = setTimeout(() => { t.hidden = true; }, 2000);
}
async function copy(text) {
  if (!text) return;
  try { await navigator.clipboard.writeText(text); toast('Copiado'); } catch { toast('No se pudo copiar'); }
}
function fmtMem(v) { return v == null ? '-' : `${v} MB`; }
function selectedService() { return state.services.find((s) => s.name === state.selected) || null; }

function heapOptions() {
  return {
    appXmx: $('app-xmx').value.trim() || '512m',
    mavenXmx: $('maven-xmx').value.trim() || '384m',
    profile: $('profile').value.trim() || 'local',
    includeOptional: $('include-optional').checked,
    useExampleEnv: $('use-example-env').checked,
  };
}

/* ---------- infra ---------- */
async function refreshInfra() {
  try {
    const r = await fetch('/api/infra', { cache: 'no-store' });
    const data = await r.json();
    $('infra-row').innerHTML = (data.infra || []).map((i) => {
      const mgmt = i.mgmtPort ? ` · mgmt ${i.mgmtUp ? 'on' : 'off'}` : '';
      return `<div class="infra-chip" title="${escapeHtml(i.requiredBy)}">
        <span class="dot ${i.up ? 'on' : 'off'}"></span>
        <div><b>${escapeHtml(i.name)}</b><br><small>:${i.port}${mgmt}</small></div>
      </div>`;
    }).join('');
  } catch { /* silencioso */ }
}

/* ---------- estado / cards ---------- */
function matches(svc) {
  const q = $('filter').value.trim().toLowerCase();
  const text = !q || svc.name.toLowerCase().includes(q) || String(svc.port).includes(q) || (svc.module || '').includes(q);
  if (!text) return false;
  if (state.filterMode === 'up') return svc.overall === 'up';
  if (state.filterMode === 'down') return svc.overall === 'down';
  return true;
}

function cardHtml(svc) {
  const stats = svc.logStats || {};
  const errChip = stats.errors ? `<span class="chip err">E ${stats.errors}</span>` : '';
  const warnChip = stats.warnings ? `<span class="chip warn">W ${stats.warnings}</span>` : '';
  const autoChip = svc.optional && svc.autoStart ? '<span class="chip">auto</span>' : '';
  return `<article class="card ${svc.overall} ${svc.name === state.selected ? 'selected' : ''}" data-svc="${svc.name}">
    <div class="card-top">
      <span class="dot"></span>
      <span class="card-name" title="${svc.name}">${escapeHtml(svc.displayName)}</span>
      ${svc.module ? `<span class="card-mod">${svc.module}</span>` : ''}
    </div>
    <div class="card-domain">${escapeHtml(svc.domain || '')}</div>
    <div class="card-meta">
      <span class="chip">:${svc.port}</span>
      <span class="chip">${escapeHtml(svc.health)}</span>
      <span class="chip">${fmtMem(svc.memoryMb)}</span>
      ${autoChip}${errChip}${warnChip}
    </div>
  </article>`;
}

function renderTiers() {
  const visible = state.services.filter(matches);
  const tierKeys = Object.keys(state.tiers).length
    ? Object.keys(state.tiers).sort((a, b) => (state.tiers[a].order || 9) - (state.tiers[b].order || 9))
    : [...new Set(state.services.map((s) => s.tier))];

  let html = '';
  for (const key of tierKeys) {
    const group = visible.filter((s) => s.tier === key);
    if (!group.length) continue;
    const meta = state.tiers[key] || { label: key, description: '' };
    html += `<div class="tier">
      <div class="tier-head"><h3>${escapeHtml(meta.label || key)}</h3><small>${escapeHtml(meta.description || '')}</small></div>
      <div class="cards">${group.map(cardHtml).join('')}</div>
    </div>`;
  }
  $('tiers').innerHTML = html || '<div class="empty">Sin servicios para mostrar.</div>';

  for (const card of $('tiers').querySelectorAll('.card')) {
    card.addEventListener('click', () => selectService(card.dataset.svc));
  }
}

function selectService(name) {
  state.selected = name;
  LS.set('selected', name);
  stopLive();
  renderTiers();
  renderDetail();
  loadLog();
}

function renderMetrics(counts) {
  $('metric-up').textContent = counts.up ?? 0;
  $('metric-watch').textContent = counts.watch ?? 0;
  $('metric-down').textContent = counts.down ?? 0;
  $('metric-memory').textContent = counts.memoryMb ?? 0;
}

function renderDetail() {
  const svc = selectedService();
  if (!svc) {
    $('detail-title').textContent = 'Selecciona un servicio';
    $('detail-sub').textContent = 'Estado, enlaces y logs en vivo';
    $('detail-badge').className = 'badge down';
    $('detail-badge').textContent = '—';
    for (const id of ['fact-module', 'fact-route', 'fact-db', 'fact-port', 'fact-pid', 'fact-memory']) $(id).textContent = '-';
    $('fact-events').textContent = '';
    $('health-link').removeAttribute('href');
    $('health-link').textContent = 'Health';
    $('swagger-link').removeAttribute('href');
    $('swagger-link').hidden = false;
    $('swagger-link').textContent = 'Swagger';
    return;
  }
  $('detail-title').textContent = svc.displayName;
  $('detail-sub').textContent = svc.detail || svc.domain || '';
  $('detail-badge').className = `badge ${svc.overall}`;
  $('detail-badge').textContent = LABELS[svc.overall] || svc.overall;
  $('fact-module').textContent = svc.module || '—';
  $('fact-route').textContent = svc.gatewayRoute || '—';
  $('fact-db').textContent = svc.db || '—';
  $('fact-port').textContent = svc.port;
  $('fact-pid').textContent = svc.pid || '-';
  $('fact-memory').textContent = fmtMem(svc.memoryMb);
  const isWeb = svc.kind === 'web';
  // Para un frontend 'web', el enlace de salud abre la ventana del front.
  $('health-link').href = svc.healthUrl || svc.url || '';
  $('health-link').textContent = isWeb ? 'Abrir ventana' : 'Health';
  if (svc.swaggerUrl) {
    $('swagger-link').href = svc.swaggerUrl;
    $('swagger-link').hidden = false;
    $('swagger-link').textContent = 'Swagger';
  } else {
    $('swagger-link').removeAttribute('href');
    $('swagger-link').hidden = true;
  }

  const ev = svc.events || {};
  const parts = [];
  if (ev.consumes && ev.consumes.length) parts.push(`<b>escucha</b> ${ev.consumes.map(escapeHtml).join(', ')}`);
  if (ev.publishes && ev.publishes.length) parts.push(`<b>emite</b> ${ev.publishes.map(escapeHtml).join(', ')}`);
  if ((svc.needs || []).length) parts.push(`<b>requiere</b> ${svc.needs.map(escapeHtml).join(', ')}`);
  $('fact-events').innerHTML = parts.join(' · ');
}

/* ---------- logs ---------- */
function decorateLog(text) {
  const q = $('log-filter').value.trim();
  const lines = text.split('\n').map((line) => {
    let cls = '';
    if (/\b(error|exception|fatal|severe)\b/i.test(line)) cls = 'l-err';
    else if (/\bwarn(ing)?\b/i.test(line)) cls = 'l-warn';
    else if (/\binfo\b/i.test(line)) cls = 'l-info';
    let html = escapeHtml(line);
    if (q) {
      const re = new RegExp('(' + q.replace(/[.*+?^${}()|[\]\\]/g, '\\$&') + ')', 'gi');
      html = html.replace(re, '<mark>$1</mark>');
    }
    return cls ? `<span class="${cls}">${html}</span>` : html;
  });
  return lines.join('\n');
}

async function loadLog(showLoading = true) {
  const svc = selectedService();
  if (!svc) return;
  if (state.live) return; // el stream ya alimenta el visor
  if (showLoading) $('log-output').textContent = 'Cargando…';
  try {
    const url = `/api/log?service=${encodeURIComponent(svc.name)}&type=${encodeURIComponent(state.logType)}`;
    const r = await fetch(url, { cache: 'no-store' });
    const data = await r.json();
    $('log-path').textContent = data.path || 'Sin archivo de log';
    const text = (data.lines || []).join('\n') || 'Sin líneas.';
    $('log-output').innerHTML = decorateLog(text);
    $('log-output').scrollTop = $('log-output').scrollHeight;
  } catch (e) {
    $('log-output').textContent = String(e);
  }
}

function startLive() {
  const svc = selectedService();
  if (!svc) { toast('Selecciona un servicio'); return; }
  stopLive();
  state.live = true;
  $('live-btn').setAttribute('aria-pressed', 'true');
  $('log-output').innerHTML = '';
  const url = `/api/log/stream?service=${encodeURIComponent(svc.name)}&type=${encodeURIComponent(state.logType)}`;
  const es = new EventSource(url);
  state.eventSource = es;
  es.onmessage = (ev) => {
    const atBottom = $('log-output').scrollTop + $('log-output').clientHeight >= $('log-output').scrollHeight - 40;
    $('log-output').insertAdjacentHTML('beforeend', decorateLog(ev.data) + '\n');
    if (atBottom) $('log-output').scrollTop = $('log-output').scrollHeight;
  };
  es.onerror = () => { toast('Stream interrumpido'); stopLive(); };
}

function stopLive() {
  if (state.eventSource) { state.eventSource.close(); state.eventSource = null; }
  state.live = false;
  $('live-btn').setAttribute('aria-pressed', 'false');
}

function setLogType(type) {
  state.logType = type;
  $('tab-out').classList.toggle('active', type === 'out');
  $('tab-err').classList.toggle('active', type === 'err');
  if (state.live) startLive(); else loadLog();
}

/* ---------- refresh ---------- */
async function refresh() {
  $('refresh-btn').disabled = true;
  try {
    const r = await fetch('/api/status', { cache: 'no-store' });
    const data = await r.json();
    state.services = data.services || [];
    state.tiers = data.tiers || {};
    renderMetrics(data.counts || {});
    $('last-update').textContent = `Actualizado ${new Date().toLocaleTimeString()} · ${state.services.length} servicios`;
    if (!state.selected && state.services.length) state.selected = state.services[0].name;
    renderTiers();
    renderDetail();
    if (!state.live && state.selected) loadLog(false);
  } catch (e) {
    $('last-update').textContent = `Error: ${e.message}`;
  } finally {
    $('refresh-btn').disabled = false;
  }
}

/* ---------- acciones ---------- */
function setButtons(enabled) {
  for (const id of ['start-all', 'stop-all', 'restart-all', 'svc-start', 'svc-stop', 'svc-restart']) $(id).disabled = !enabled;
}

function actionScope(action) {
  if (!action) return 'ecosistema';
  if (action.services && action.services.length) return action.services.join(', ');
  if (action.tiers && action.tiers.length) return `tier ${action.tiers.join(', ')}`;
  return action.options?.includeOptional ? 'ecosistema completo + experimentales' : 'ecosistema operativo';
}

function renderActionStatus(action) {
  const el = $('action-status');
  if (!el || !action) return;
  const status = action.status || 'queued';
  const label = ACTION_LABELS[action.action] || action.action;
  const tail = (action.logTail || []).slice(-8).map(escapeHtml).join('\n');
  el.hidden = false;
  el.className = `action-status ${escapeHtml(status)}`;
  el.innerHTML = `
    <div class="action-main">
      <span class="dot"></span>
      <div>
        <strong>${escapeHtml(label)} · ${escapeHtml(actionScope(action))}</strong>
        <span>${escapeHtml(ACTION_STATUS[status] || status)}${action.exitCode !== null && action.exitCode !== undefined ? ` · exit ${action.exitCode}` : ''}</span>
      </div>
    </div>
    ${tail ? `<pre>${tail}</pre>` : ''}`;
}

function clearActionPoll() {
  if (state.actionPollTimer) clearInterval(state.actionPollTimer);
  state.actionPollTimer = null;
  state.currentActionId = null;
}

function finishAction(action) {
  clearActionPoll();
  state.busy = false;
  setButtons(true);
  refresh();
  toast(action.status === 'succeeded' ? 'Acción completada' : 'Acción con errores; revisa la banda de progreso');
}

function watchAction(action) {
  state.currentActionId = action.id;
  state.busy = true;
  setButtons(false);
  renderActionStatus(action);

  let polling = false;
  const tick = async () => {
    if (polling || !state.currentActionId) return;
    polling = true;
    try {
      const r = await fetch(`/api/actions?id=${encodeURIComponent(state.currentActionId)}`, { cache: 'no-store' });
      const data = await r.json();
      const latest = (data.actions || [])[0];
      if (latest) {
        renderActionStatus(latest);
        await refresh();
        if (latest.status === 'succeeded' || latest.status === 'failed') finishAction(latest);
      }
    } catch (e) {
      toast(`No pude leer la acción: ${e.message}`);
    } finally {
      polling = false;
    }
  };

  state.actionPollTimer = setInterval(tick, 2500);
  tick();
}

async function runAction(action, services = []) {
  if (state.busy) return;
  state.busy = true; setButtons(false);
  try {
    const body = { action, services, ...heapOptions() };
    const r = await fetch('/api/action', { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(body) });
    const data = await r.json();
    if (!r.ok) throw new Error(data.error || 'No se pudo ejecutar la acción.');
    toast(`Acción «${action}» enviada${services.length ? ` (${services.join(', ')})` : ''}`);
    watchAction(data.action);
  } catch (e) {
    toast(e.message);
    state.busy = false; setButtons(true);
  }
}
function selectedList() { const s = selectedService(); return s ? [s.name] : []; }

/* ---------- doctor ---------- */
async function openDoctor() {
  $('doctor-modal').hidden = false;
  $('doctor-body').textContent = 'Ejecutando…';
  try {
    const r = await fetch('/api/doctor', { cache: 'no-store' });
    const data = await r.json();
    const icon = { ok: '●', warn: '▲', fail: '✕' };
    const sum = data.summary || {};
    const rows = (data.checks || []).map((c) => `
      <div class="doctor-row ${c.status}">
        <span class="ic">${icon[c.status] || '·'}</span>
        <div><div class="name">${escapeHtml(c.check)}</div><div class="detail">${escapeHtml(c.detail)}</div></div>
      </div>`).join('');
    $('doctor-body').innerHTML = `
      <div class="doctor-summary">
        <span class="chip">${sum.ok || 0} ok</span>
        <span class="chip warn">${sum.warn || 0} avisos</span>
        <span class="chip err">${sum.fail || 0} fallos</span>
      </div>${rows}`;
  } catch (e) {
    $('doctor-body').textContent = String(e);
  }
}

/* ---------- token ---------- */
async function getToken() {
  $('token-result').textContent = 'Iniciando sesión…';
  try {
    const r = await fetch('/api/token', { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ password: $('token-pass').value || undefined }) });
    const data = await r.json();
    if (!data.ok) { $('token-result').innerHTML = `<span class="chip err">Falló</span> ${escapeHtml(data.error || '')}`; return; }
    const u = data.usuario || {};
    const roles = (u.roles || []).join(', ');
    $('token-result').innerHTML = `
      <div><span class="chip">${escapeHtml(data.email)}</span> ${escapeHtml(roles)}</div>
      <div class="token-box" id="token-jwt">${escapeHtml(data.accessToken || '')}</div>
      <button class="primary" id="token-copy">Copiar Bearer</button>
      <small class="muted"> vía ${escapeHtml(data.via || '')}</small>`;
    $('token-copy').addEventListener('click', () => copy(data.accessToken));
  } catch (e) {
    $('token-result').textContent = String(e);
  }
}

/* ---------- flujo e2e visual ---------- */
const E2E_STAGES = [
  { k: 'auth', label: 'Auth', desc: 'Inicia sesión → token JWT' },
  { k: 'catalogo', label: 'Catálogo', desc: 'Elige un derecho vulnerado' },
  { k: 'personas', label: 'Personas', desc: 'Registra requirente y NNA' },
  { k: 'requerimientos', label: 'Requerim.', desc: 'Crea y asigna el requerimiento' },
  { k: 'casos', label: 'Caso', desc: 'Nace solo al asignar', evento: true },
  { k: 'vulneraciones', label: 'FIR', desc: 'Ficha reservada cifrada', evento: true },
  { k: 'productos', label: 'Productos', desc: 'Informe + tarea' },
  { k: 'checks', label: 'Validaciones', desc: 'Seguridad: 401/404/409' },
];
let e2eSource = null;

function e2eInitPipeline() {
  $('e2e-pipeline').innerHTML = E2E_STAGES.map((s, i) => `
    <div class="e2e-node pending" data-stage="${s.k}">
      <span class="e2e-dot"></span>
      <span class="e2e-node-label">${s.label}${s.evento ? '<span class="e2e-ev" title="Ocurre solo, disparado por un evento (RabbitMQ)">⚡</span>' : ''}</span>
      <span class="e2e-node-desc">${s.desc}</span>
      <span class="e2e-node-val"></span>
    </div>${i < E2E_STAGES.length - 1 ? '<span class="e2e-arrow">→</span>' : ''}`).join('');
}
function e2eLog(line) {
  const el = $('e2e-log');
  if (el.textContent === '—') el.textContent = '';
  el.textContent += line + '\n';
  el.scrollTop = el.scrollHeight;
}
function e2eSetNode(stage, status, value) {
  const n = $('e2e-pipeline').querySelector(`[data-stage="${stage}"]`);
  if (!n) return;
  n.className = 'e2e-node ' + status;
  if (value) n.querySelector('.e2e-node-val').textContent = value;
}
function e2eStop() { if (e2eSource) { e2eSource.close(); e2eSource = null; } }

function runE2E() {
  e2eStop();
  e2eInitPipeline();
  $('e2e-data').innerHTML = '<div class="empty">Ejecutando…</div>';
  $('e2e-log').textContent = '—';
  $('e2e-status').textContent = 'ejecutando…';
  $('e2e-run').disabled = true;
  const es = new EventSource('/api/e2e/stream');
  e2eSource = es;
  es.onmessage = (m) => {
    let d; try { d = JSON.parse(m.data); } catch { return; }
    if (d.type === 'step') {
      e2eSetNode(d.stage, d.status, d.value);
      const ic = { running: '…', done: '✓', warn: '!', fail: '✗' }[d.status] || '·';
      e2eLog(`${ic} ${d.label}${d.value ? ': ' + d.value : ''}${d.detail ? ' (' + d.detail + ')' : ''}`);
    } else if (d.type === 'done') {
      e2eStop();
      $('e2e-run').disabled = false;
      $('e2e-status').textContent = d.ok ? 'completado ✓' : ('falló' + (d.error ? ': ' + d.error : ''));
      const rows = d.summary || [];
      $('e2e-data').innerHTML = rows.length
        ? rows.map((r) => `<div class="e2e-card-row"><span>${escapeHtml(r.k)}</span><b>${escapeHtml(r.v)}</b></div>`).join('')
        : '<div class="empty">Sin datos.</div>';
    }
  };
  es.onerror = () => { e2eStop(); $('e2e-run').disabled = false; $('e2e-status').textContent = 'conexión interrumpida'; };
}

/* ---------- tema ---------- */
const THEME_ICON = {
  dark: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="4"/><path d="M12 2v2M12 20v2M4.93 4.93l1.41 1.41M17.66 17.66l1.41 1.41M2 12h2M20 12h2M4.93 19.07l1.41-1.41M17.66 6.34l1.41-1.41"/></svg>',
  light: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79Z"/></svg>',
};
function applyTheme(theme) {
  document.documentElement.dataset.theme = theme;
  LS.set('theme', theme);
  // en oscuro ofrece pasar a claro (sol); en claro ofrece pasar a oscuro (luna)
  $('theme-btn').innerHTML = theme === 'dark' ? THEME_ICON.dark : THEME_ICON.light;
}

/* ---------- init ---------- */
function bind() {
  $('refresh-btn').addEventListener('click', refresh);
  $('auto-btn').addEventListener('click', () => {
    state.auto = !state.auto; LS.set('auto', state.auto);
    $('auto-btn').setAttribute('aria-pressed', String(state.auto));
    resetTimer();
  });
  $('theme-btn').addEventListener('click', () => applyTheme(document.documentElement.dataset.theme === 'dark' ? 'light' : 'dark'));
  $('help-btn').addEventListener('click', () => { $('help-panel').hidden = !$('help-panel').hidden; });
  $('doctor-btn').addEventListener('click', openDoctor);
  $('token-btn').addEventListener('click', () => { $('token-modal').hidden = false; });
  $('token-go').addEventListener('click', getToken);
  $('e2e-btn').addEventListener('click', () => { $('e2e-modal').hidden = false; e2eInitPipeline(); });
  $('e2e-run').addEventListener('click', runE2E);
  $('e2e-modal').addEventListener('click', (e) => { if (e.target === $('e2e-modal') || e.target.closest('[data-close]')) e2eStop(); });

  $('filter').addEventListener('input', renderTiers);
  for (const b of document.querySelectorAll('.seg [data-mode]')) {
    b.addEventListener('click', () => {
      state.filterMode = b.dataset.mode;
      for (const x of document.querySelectorAll('.seg [data-mode]')) x.classList.toggle('active', x === b);
      renderTiers();
    });
  }

  $('start-all').addEventListener('click', () => runAction('start'));
  $('stop-all').addEventListener('click', () => runAction('stop'));
  $('restart-all').addEventListener('click', () => runAction('restart'));
  $('svc-start').addEventListener('click', () => runAction('start', selectedList()));
  $('svc-stop').addEventListener('click', () => runAction('stop', selectedList()));
  $('svc-restart').addEventListener('click', () => runAction('restart', selectedList()));

  $('tab-out').addEventListener('click', () => setLogType('out'));
  $('tab-err').addEventListener('click', () => setLogType('err'));
  $('log-filter').addEventListener('input', () => { if (!state.live) loadLog(false); });
  $('live-btn').addEventListener('click', () => { if (state.live) { stopLive(); loadLog(); } else startLive(); });
  $('wrap-btn').addEventListener('click', () => {
    state.wrap = !state.wrap; LS.set('wrap', state.wrap);
    $('log-output').classList.toggle('wrap', state.wrap);
    $('wrap-btn').setAttribute('aria-pressed', String(state.wrap));
  });

  for (const el of document.querySelectorAll('[data-close]')) {
    el.addEventListener('click', () => { el.closest('.modal').hidden = true; });
  }
  for (const m of document.querySelectorAll('.modal')) {
    m.addEventListener('click', (e) => { if (e.target === m) m.hidden = true; });
  }
  document.addEventListener('keydown', (e) => {
    if (e.key === 'Escape') for (const m of document.querySelectorAll('.modal')) m.hidden = true;
    if (e.key === 'r' && !/input|textarea/i.test(document.activeElement.tagName)) refresh();
  });

  // persistencia de campos
  for (const id of ['app-xmx', 'maven-xmx', 'profile']) {
    const want = LS.get('field.' + id, null);
    const valid = [...$(id).options].map((o) => o.value);
    if (want && valid.includes(want)) $(id).value = want; // ignora valores viejos inválidos
    $(id).addEventListener('change', () => LS.set('field.' + id, $(id).value));
  }
  const checkPrefs = {
    'include-optional': 'include-experimental',
    'use-example-env': 'use-example-env',
  };
  for (const [id, key] of Object.entries(checkPrefs)) {
    $(id).checked = LS.get('field.' + key, false);
    $(id).addEventListener('change', () => LS.set('field.' + key, $(id).checked));
  }
}

function resetTimer() {
  if (state.timer) clearInterval(state.timer);
  state.timer = state.auto ? setInterval(refresh, 3500) : null;
}

function boot() {
  applyTheme(LS.get('theme', 'dark'));
  $('auto-btn').setAttribute('aria-pressed', String(state.auto));
  $('wrap-btn').setAttribute('aria-pressed', String(state.wrap));
  $('log-output').classList.toggle('wrap', state.wrap);
  bind();
  refresh();
  refreshInfra();
  resetTimer();
  state.actionTimer = setInterval(refreshInfra, 6000);
}

boot();
