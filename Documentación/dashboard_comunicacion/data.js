const DASHBOARD_DATA = {
  project: {
    name: "Aula Señora",
    key: "DEV",
    lead: "Oscar Cadavid",
    team: ["Oscar Cadavid", "Juan Lozano", "Veruzka Guapacha"],
    version: "Release 1.0.0",
    url: "https://github.com/zpeedy0818/Aula-Senora",
    jira: "https://ocadavidramirez9.atlassian.net/jira/software/projects/DEV"
  },

  sprints: [
    { id: 1, name: "Sprint 1 — Autenticacion", start: "2026-03-19", end: "2026-04-09", state: "Cerrado", storyPoints: 16 },
    { id: 2, name: "DEV Sprint 2", start: "2026-04-08", end: "2026-04-29", state: "Cerrado", storyPoints: 33 },
    { id: 3, name: "Sprint 3 — Final", start: "2026-05-01", end: "2026-05-21", state: "Cerrado", storyPoints: 34 },
    { id: 4, name: "Sprint 3 — Final 2", start: null, end: null, state: "Futuro", storyPoints: 0 }
  ],

  issues: {
    total: 57,
    byType: { Historia: 20, Subtask: 18, Riesgos: 10, Epic: 8, Tarea: 1 },
    byStatus: { "Tareas por hacer": { Epic: 8 }, "En curso": { Riesgos: 4 }, "Finalizada": { Historia: 20, Subtask: 18, Riesgos: 6, Tarea: 1 } },
    bySprint: {
      "Sprint 1 — Autenticacion": { total: 4, storyPoints: 16, done: 4 },
      "DEV Sprint 2": { total: 12, storyPoints: 33, done: 12 },
      "Sprint 3 — Final": { total: 22, storyPoints: 34, done: 22 }
    },
    byAssignee: {
      "Oscar Cadavid": 22,
      "Juan Lozano": 17,
      "Veruzka Guapacha": 18
    },
    byEpic: {
      "DEV-10": { name: "Autenticación y Seguridad Mobile", issues: 3, done: 3 },
      "DEV-11": { name: "Gestión de Usuarios y Perfiles", issues: 1, done: 1 },
      "DEV-23": { name: "Gestión Administrativa", issues: 5, done: 5 },
      "DEV-34": { name: "Modernización UX/UI", issues: 3, done: 3 },
      "DEV-35": { name: "Cronograma y Tutorías", issues: 3, done: 3 },
      "DEV-37": { name: "Integraciones Cloud & APIs", issues: 3, done: 3 },
      "DEV-38": { name: "Panel Admin y BI", issues: 2, done: 2 },
      "DEV-39": { name: "Estabilización, QA y Release", issues: 1, done: 1 }
    }
  },

  epics: [
    { key: "DEV-10", name: "Autenticación y Seguridad Mobile", sprint: "Sprint 1", sp: 16 },
    { key: "DEV-11", name: "Gestión de Usuarios y Perfiles", sprint: "Sprint 1", sp: 8 },
    { key: "DEV-23", name: "Gestión Administrativa y Control", sprint: "Sprint 2", sp: 12 },
    { key: "DEV-34", name: "Modernización UX/UI", sprint: "Sprint 2", sp: 10 },
    { key: "DEV-35", name: "Cronograma y Tutorías", sprint: "Sprint 2", sp: 13 },
    { key: "DEV-37", name: "Integraciones Cloud & APIs", sprint: "Sprint 3", sp: 18 },
    { key: "DEV-38", name: "Panel Admin y BI", sprint: "Sprint 3", sp: 13 },
    { key: "DEV-39", name: "Estabilización, QA y Release", sprint: "Sprint 3", sp: 3 }
  ],

  commits: {
    byPerson: { "Oscar Cadavid": 46, "Juan Lozano": 37, "Veruzka Guapacha": 22 },
    byMonth: {
      "2026-02": { commits: 4, authors: { "Juan Lozano": 4 } },
      "2026-03": { commits: 28, authors: { "Juan Lozano": 4, "Oscar Cadavid": 12, "Veruzka Guapacha": 12 } },
      "2026-04": { commits: 38, authors: { "Juan Lozano": 10, "Oscar Cadavid": 23, "Veruzka Guapacha": 5 } },
      "2026-05": { commits: 35, authors: { "Juan Lozano": 19, "Oscar Cadavid": 11, "Veruzka Guapacha": 5 } }
    },
    total: 105
  },

  risks: [
    { id: "R1", name: "Daily standups no ocurren", prob: "Media", impact: "Alto", level: "Crítico", owner: "Oscar", kri: "2 días sin daily" },
    { id: "R2", name: "Feedback del evaluador no documentado", prob: "Alta", impact: "Alto", level: "Crítico", owner: "Veruzka", kri: "48h sin acta" },
    { id: "R3", name: "Jira desactualizado", prob: "Alta", impact: "Medio", level: "Alto", owner: "Todo el equipo", kri: "3 días sin mover tareas" },
    { id: "R4", name: "Sin notificaciones a usuarios", prob: "Media", impact: "Alto", level: "Crítico", owner: "Veruzka", kri: "24h sin comunicar release" },
    { id: "R5", name: "Decisiones sin registro en canales informales", prob: "Alta", impact: "Medio", level: "Alto", owner: "Oscar", kri: "Decisión sin volcar a Jira" },
    { id: "R6", name: "Reportes semanales no enviados", prob: "Baja", impact: "Alto", level: "Alto", owner: "Oscar", kri: "Viernes sin reporte" },
    { id: "R7", name: "Documentación técnica desactualizada", prob: "Alta", impact: "Medio", level: "Alto", owner: "Juan", kri: "Historia cerrada sin docs" },
    { id: "R8", name: "Sprint goals ambiguos", prob: "Media", impact: "Alto", level: "Crítico", owner: "Oscar", kri: "Planning sin objetivo claro" },
    { id: "R9", name: "Silencio en bloqueadores", prob: "Media", impact: "Crítico", level: "Crítico", owner: "Todo el equipo", kri: "8h sin escalar bloqueador" },
    { id: "R10", name: "Sin comunicación a instituciones", prob: "Baja", impact: "Alto", level: "Alto", owner: "Oscar", kri: "Trimestre sin informe" },
    { id: "R11", name: "Post-mortem no se realiza", prob: "Alta", impact: "Medio", level: "Alto", owner: "Todo el equipo", kri: "Incidente sin post-mortem" },
    { id: "R12", name: "Lenguaje técnico inadecuado para usuarios", prob: "Media", impact: "Medio", level: "Medio", owner: "Veruzka", kri: "Ticket de soporte por confusión" }
  ],

  riskLevels: { Crítico: 5, Alto: 6, Medio: 1, Bajo: 0 },

  communication: {
    events: [
      { name: "Daily Standup", freq: "Diaria", responsible: "Todo el equipo", channel: "WhatsApp/Discord", status: "Activo" },
      { name: "Actualización Jira", freq: "Diaria", responsible: "Cada miembro", channel: "Jira", status: "Activo" },
      { name: "Commits con ref. Jira", freq: "Por tarea", responsible: "Cada miembro", channel: "GitHub + Jira", status: "Activo" },
      { name: "Sprint Planning", freq: "Cada 20d", responsible: "Todo el equipo", channel: "Meet + Jira", status: "Completado" },
      { name: "Sprint Review / Demo", freq: "Cada 20d", responsible: "Todo el equipo", channel: "Meet + Jira", status: "Completado" },
      { name: "Sprint Retrospective", freq: "Cada 20d", responsible: "Todo el equipo", channel: "Meet", status: "Completado" },
      { name: "Reporte Semanal", freq: "Semanal", responsible: "Oscar", channel: "Email + PDF", status: "Pendiente" },
      { name: "Notificación Estudiantes", freq: "Por evento", responsible: "Sistema/Admin", channel: "Push + Email", status: "Pendiente" },
      { name: "Notificación Voluntarios", freq: "Por evento", responsible: "Sistema/Admin", channel: "Email + Dashboard", status: "Pendiente" },
      { name: "Newsletter Mensual", freq: "Mensual", responsible: "Admin", channel: "Email", status: "Pendiente" },
      { name: "Informe Trimestral", freq: "Trimestral", responsible: "Oscar", channel: "Email + PDF", status: "Pendiente" },
      { name: "Reunión Coordinadores", freq: "Por sprint", responsible: "Oscar + Coordinadores", channel: "Meet", status: "Pendiente" },
      { name: "Benchmarking", freq: "Trimestral", responsible: "Equipo", channel: "Google Docs", status: "Pendiente" },
      { name: "Atención Usuarios", freq: "Bajo demanda", responsible: "Admin", channel: "Chat + Email", status: "Activo" },
      { name: "Redes Sociales", freq: "Quincenal", responsible: "Veruzka", channel: "Instagram/LinkedIn", status: "Pendiente" }
    ]
  },

  stakeholders: [
    { name: "Administradores del Sistema", power: "Alto", interest: "Alto", quadrant: "Gestionar de Cerca" },
    { name: "Coordinadores de Voluntariado", power: "Alto", interest: "Alto", quadrant: "Gestionar de Cerca" },
    { name: "Profesor / Evaluador", power: "Alto", interest: "Alto", quadrant: "Gestionar de Cerca" },
    { name: "Instituciones Educativas Origen", power: "Alto", interest: "Bajo", quadrant: "Mantener Satisfechos" },
    { name: "Instituciones Educativas Receptoras", power: "Medio", interest: "Alto", quadrant: "Mantener Informados" },
    { name: "Proveedores Cloud", power: "Alto", interest: "Bajo", quadrant: "Mantener Satisfechos" },
    { name: "Estudiantes", power: "Bajo", interest: "Alto", quadrant: "Mantener Informados" },
    { name: "Voluntarios / Educadores Base", power: "Bajo", interest: "Alto", quadrant: "Mantener Informados" },
    { name: "Usuarios Invitados", power: "Bajo", interest: "Bajo", quadrant: "Monitorear" },
    { name: "Público General", power: "Bajo", interest: "Bajo", quadrant: "Monitorear" },
    { name: "Plataformas / Competencias", power: "Bajo", interest: "Medio", quadrant: "Monitorear" },
    { name: "Equipo de Desarrollo", power: "Medio", interest: "Alto", quadrant: "Mantener Informados" }
  ],

  healthIndicators: [
    { name: "Cumplimiento de tareas", value: 93, status: "good", desc: "53/57 issues finalizadas" },
    { name: "Participación del equipo", value: 85, status: "good", desc: "105 commits, 3 miembros activos" },
    { name: "Gestión de riesgos", value: 58, status: "warning", desc: "5 riesgos críticos sin mitigar" },
    { name: "Comunicación con stakeholders", value: 40, status: "warning", desc: "Solo 4/15 eventos activos" },
    { name: "Documentación", value: 45, status: "warning", desc: "Plan creado, ejecución pendiente" }
  ]
};
