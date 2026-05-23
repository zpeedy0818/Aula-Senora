# Informe Ejecutivo — Dashboard de Comunicación y Seguimiento

> **Proyecto:** Aula Señora — Plataforma de aprendizaje que conecta voluntarios con estudiantes
> **Período analizado:** Febrero — Mayo 2026
> **Fuentes:** Jira (DEV), Git log, Plan de Comunicación, Matriz de Riesgos

---

## 1. Resumen General

El dashboard consolida **5 dimensiones** clave del proyecto en un solo tablero visual:

| Dimensión | Indicador Principal | Estado |
|-----------|-------------------|--------|
| Cumplimiento de tareas | 93% de issues finalizadas (53/57) | ✅ Bueno |
| Participación del equipo | 105 commits, 3 miembros activos | ✅ Bueno |
| Comunicación con stakeholders | 27% de eventos del plan activos | ⚠️ Bajo |
| Gestión de riesgos | 5 riesgos críticos sin mitigación completa | ⚠️ Medio |
| Salud del proyecto | 3 sprints completados al 100% | ✅ Bueno |

---

## 2. Análisis por Dimensión

### 2.1 Cumplimiento de Tareas

| Métrica | Valor |
|---------|-------|
| Total issues | 57 |
| Finalizadas | 53 (93%) |
| En curso | 4 (Riesgos) |
| Story Points totales | 83 |
| Velocidad promedio | 27.7 SP/sprint |

**Evolución por sprint:**
- Sprint 1 (Autenticación): 16 SP — 100% completado
- Sprint 2 (DEV Sprint 2): 33 SP — 100% completado
- Sprint 3 (Final): 34 SP — 100% completado

### 2.2 Participación del Equipo

| Miembro | Issues Asignadas | Commits | Rol Principal |
|---------|-----------------|---------|-------------|
| Oscar Cadavid | 22 | 46 | Líder técnico, backend, calendar/meet |
| Juan Lozano | 17 | 37 | Aulas, scheduling, chat, migraciones |
| Veruzka Guapacha | 18 | 22 | Frontend, UI/UX, seguridad, documentación |

### 2.3 Frecuencia de Comunicación

Del plan de comunicación de **15 eventos** definidos:

| Estado | Cantidad | Eventos |
|--------|----------|---------|
| ✅ Activo | 3 | Daily standup, Actualización Jira, Atención a usuarios |
| ✅ Completado | 3 | Sprint Planning, Sprint Review, Sprint Retrospective |
| ⚠️ Pendiente | 9 | Reportes semanales, notificaciones, newsletter, redes, etc. |

**Brecha principal:** Solo 6/15 eventos (40%) están siendo ejecutados regularmente. Los eventos pendientes corresponden en su mayoría a comunicaciones externas (estudiantes, voluntarios, instituciones).

### 2.4 Riesgos de Comunicación

| Nivel | Cantidad | Acción |
|-------|----------|--------|
| 🔴 Crítico | 5 | Mitigación inmediata requerida |
| 🟡 Alto | 6 | Mitigación planificada |
| 🟢 Medio | 1 | Monitoreo activo |

**Top 3 riesgos críticos:**
1. **R2** — Feedback del evaluador no documentado (Prob. Alta, Impacto Alto)
2. **R4** — Sin notificaciones a usuarios sobre cambios (Prob. Media, Impacto Alto)
3. **R9** — Silencio en bloqueadores (Prob. Media, Impacto Crítico)

### 2.5 Estado del Proyecto

| Componente | % | Color |
|-----------|---|-------|
| Cumplimiento de tareas | 93% | 🟢 |
| Participación del equipo | 85% | 🟢 |
| Gestión de riesgos | 58% | 🟡 |
| Comunicación con stakeholders | 40% | 🟡 |
| Documentación | 45% | 🟡 |

---

## 3. Recomendaciones Ejecutivas

| Prioridad | Acción | Impacto Esperado |
|-----------|--------|-----------------|
| 1 | Establecer horario fijo de daily standup (R1) | Alineación diaria del equipo |
| 2 | Asignar tomador de minutas en sprint reviews (R2) | Trazabilidad del feedback del evaluador |
| 3 | Agregar checklist de comunicación en Definition of Done (R4) | Adopción de nuevas funcionalidades por usuarios |
| 4 | Definir criterios de aceptación en cada historia (R8) | Claridad en objetivos del sprint |
| 5 | Comunicar regla de bloqueadores >4h al equipo (R9) | Detección temprana de problemas |
| 6 | Configurar recordatorio semanal para reportes (R6) | Evaluador informado del progreso |

---

## 4. Dashboard Visual

Para acceder al dashboard interactivo:

```bash
# Abrir en el navegador
xdg-open Documentación/dashboard_comunicacion/index.html
```

El dashboard contiene:
- **6 gráficas** (donuts, barras, líneas)
- **Tablas dinámicas** de riesgos y comunicación
- **Indicadores de salud** con barras de progreso
- **Checklist** de eventos de comunicación
- **KRI** con semáforo de estado
- **3 filtros** (sprint, stakeholder, nivel de riesgo) con botón **"Aplicar Filtros"** que actualiza todas las gráficas, tablas, y tarjetas de resumen simultáneamente

---

## 5. Glosario de Abreviaturas

| Abreviatura | Significado |
|-------------|-------------|
| SP | Story Points |
| KRI | Key Risk Indicator (Indicador Clave de Riesgo) |
| R1–R12 | Riesgos de comunicación identificados |
| Done | Definition of Done (Definición de Terminado) |
| DEV | Project Key en Jira (Aula Señora) |
