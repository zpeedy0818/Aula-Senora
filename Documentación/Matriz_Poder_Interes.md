# Análisis de Stakeholders — Aula Señora

> **Proyecto:** Plataforma de aprendizaje que conecta voluntarios (educadores) con estudiantes que necesitan apoyo académico.
> **Equipo:** Oscar Cadavid (Lead), Juan Lozano, Veruzka Guapacha
> **Sprints:** Sprint 1 — Autenticacion (Mar 19 → Abr 9), DEV Sprint 2 (Abr 8 → Abr 29), Sprint 3 — Final (May 1 → May 21)
> **Fecha:** Mayo 2026

---

## 1. Listado de Stakeholders

| # | Stakeholder | Rol en el Proyecto |
|---|-------------|-------------------|
| 1 | **Administradores del Sistema** | Gestionan usuarios, aprueban contenidos, supervisan la plataforma |
| 2 | **Coordinadores de Voluntariado** | Definen los programas educativos y coordinan a los voluntarios |
| 3 | **Profesor / Evaluador** | Evalúa el proyecto (contexto académico), define nota y viabilidad técnica |
| 4 | **Instituciones Educativas de Origen** | Universidades o entidades que respaldan a los voluntarios |
| 5 | **Instituciones Educativas Receptoras** | Escuelas/colegios donde estudian los estudiantes beneficiarios |
| 6 | **Proveedores Cloud** | Supabase (DB), Google (OAuth2, Calendar API), GitHub, Cloudinary |
| 7 | **Estudiantes** | Usuarios finales que reciben las tutorías |
| 8 | **Voluntarios / Educadores Base** | Dictan las clases y crean el contenido educativo |
| 9 | **Usuarios Invitados** | Visitantes no registrados que exploran el landing page |
| 10 | **Público General** | Audiencia amplia que puede conocer el proyecto a futuro |
| 11 | **Plataformas Secundarias / Competencias** | Otras iniciativas similares de tutorías |
| 12 | **Equipo de Desarrollo** | Oscar (Lead/Líder técnico), Juan, Veruzka — desarrollan y mantienen la plataforma |

---

## 2. Clasificación por Poder, Influencia, Interés e Impacto

| Stakeholder | Poder | Influencia | Interés | Impacto | Descripción |
|---|---|---|---|---|---|
| Administradores del Sistema | Alto | Alta | Alto | Alto | Control total sobre la plataforma, usuarios y decisiones técnicas |
| Coordinadores de Voluntariado | Alto | Alta | Alto | Alto | Deciden qué programas y voluntarios participan |
| Profesor / Evaluador | Alto | Alta | Alto | Alto | Define la calificación y viabilidad del proyecto |
| Instituciones Educativas de Origen | Alto | Alta | Bajo | Medio | Pueden retirar apoyo institucional; no participan en el día a día |
| Instituciones Educativas Receptoras | Medio | Media | Alto | Alto | Sus estudiantes son los beneficiarios directos |
| Proveedores Cloud | Alto | Baja | Bajo | Alto | Su servicio es crítico, pero no les interesa el éxito del proyecto |
| Estudiantes | Bajo | Media | Alto | Alto | Usuarios principales; su experiencia define el éxito |
| Voluntarios / Educadores Base | Bajo | Media | Alto | Alto | Generan el contenido; sin ellos no hay tutorías |
| Usuarios Invitados | Bajo | Baja | Bajo | Bajo | Potenciales registros; impacto mínimo |
| Público General | Bajo | Baja | Bajo | Bajo | Audiencia lejana; impacto a largo plazo |
| Plataformas Secundarias / Competencias | Bajo | Baja | Medio | Bajo | Referencia competitiva, sin poder sobre el proyecto |
| Equipo de Desarrollo | Medio | Media | Alto | Alto | Ejecutan el proyecto; su motivación y productividad determinan el éxito |

---

## 3. Matriz Poder / Interés

```
                    INTERÉS
                BAJO                ALTO
        ┌────────────────────────────────────┐
     A  │                                    │
     L  │   MANTENER SATISFECHOS    │   GESTIONAR DE CERCA    │
     T  │   (Alto Poder, Bajo Interés)      │   (Alto Poder, Alto Interés)       │
     O  │                                    │                                    │
        │   • Inst. Educativas de Origen     │   • Administradores del Sistema    │
     P  │   • Proveedores Cloud              │   • Coordinadores de Voluntariado  │
     O  │                                    │   • Profesor / Evaluador           │
     D  │                                    │                                    │
     E  ├────────────────────────────────────┤
     R  │                                    │
        │   MONITOREAR              │   MANTENER INFORMADOS          │
     B  │   (Bajo Poder, Bajo Interés)       │   (Bajo Poder, Alto Interés)       │
     A  │                                    │                                    │
      J  │   • Usuarios Invitados             │   • Estudiantes                    │
      O  │   • Público General                │   • Voluntarios / Educadores Base  │
         │   • Plataformas / Competencias     │   • Inst. Educativas Receptoras    │
         │                                    │   • Equipo de Desarrollo           │
        │                                    │                                    │
        └────────────────────────────────────┘
```

---

## 4. Estrategias de Gestión por Cuadrante

### 🟥 Gestionar de Cerca (Alto Poder / Alto Interés)
*Objetivo: Mantenerlos comprometidos y satisfechos con involvement activo.*

| Stakeholder | Estrategia |
|---|---|
| Administradores del Sistema | Reuniones semanales de seguimiento; dashboards de métricas en tiempo real; involucrarlos en decisiones de arquitectura y prioridades del backlog |
| Coordinadores de Voluntariado | Sesiones de planificación de programas cada sprint; canal directo para propuestas de nuevas funcionalidades; reportes de actividad de voluntarios |
| Profesor / Evaluador | Demostraciones periódicas (sprint reviews); entregar documentación técnica clara; alinear entregables con rúbrica de evaluación |

### 🟧 Mantener Satisfechos (Alto Poder / Bajo Interés)
*Objetivo: Evitar que se conviertan en obstáculo; mantenerlos contentos con mínimo esfuerzo.*

| Stakeholder | Estrategia |
|---|---|
| Instituciones Educativas de Origen | Enviar reportes trimestrales de impacto; reconocer su apoyo en comunicaciones públicas; cumplir con convenios y políticas institucionales |
| Proveedores Cloud | Monitorear uso y costos; seguir buenas prácticas de seguridad para evitar bloqueos; mantener contratos y términos de servicio al día |

### 🟩 Mantener Informados (Bajo Poder / Alto Interés)
*Objetivo: Mantenerlos comprometidos mediante comunicación constante.*

| Stakeholder | Estrategia |
|---|---|
| Estudiantes | Notificaciones push y correos con nuevas clases/horarios; encuestas de satisfacción periódicas; canal de soporte y feedback |
| Voluntarios / Educadores Base | Newsletter con actualizaciones de la plataforma; guías y tutoriales de nuevas herramientas; reconocimiento público a su labor |
| Instituciones Educativas Receptoras | Reportes de progreso de estudiantes; reuniones periódicas para alinear expectativas; portal de seguimiento de tutorías |
| Equipo de Desarrollo | Daily standups; retrospectivas por sprint; Jira actualizado diariamente; canales informales (WhatsApp/Discord) para bloqueantes |

### 🟦 Monitorear (Bajo Poder / Bajo Interés)
*Objetivo: Vigilar cambios que puedan aumentar su interés o poder; mínima inversión.*

| Stakeholder | Estrategia |
|---|---|
| Usuarios Invitados | Analítica de landing page (Google Analytics); optimizar tasa de conversión a registro; A/B testing en formularios |
| Público General | Campañas en redes sociales; contenido educativo abierto (blog/videos) para atraer interés |
| Plataformas / Competencias | Benchmarking trimestral; identificar funcionalidades diferenciadoras; mantener ventaja competitiva |

---

## 5. Mapa Visual de Stakeholders

### Capas del Mapa (Modelo de Cebolla / Onion Diagram)

```
                  ┌─────────────────────────────┐
                  │       ENTORNO EXTERNO        │
                  │  ┌───────────────────────┐   │
                  │  │    STAKEHOLDERS        │   │
                  │  │    CERCANOS           │   │
                  │  │  ┌─────────────────┐  │   │
                  │  │  │   INTERNOS      │  │   │
                  │  │  │                 │  │   │
                  │  │  │ • Administrador │  │   │
                  │  │  │ • Coordinadores │  │   │
                  │  │  │ • Profesor/Eval │  │   │
                  │  │  └─────────────────┘  │   │
                  │  │                       │   │
                  │  │ • Estudiantes         │   │
                  │  │ • Voluntarios         │   │
                  │  │ • Inst. Receptoras    │   │
                  │  └───────────────────────┘   │
                  │                              │
                  │ • Inst. Origen              │
                  │ • Proveedores Cloud          │
                  │ • Invitados / Público        │
                  │ • Competencias               │
                  └─────────────────────────────┘
```

### Descripción de las Capas

| Capa | Stakeholders | Relación con el Proyecto | Frecuencia de Interacción | Estrategia Dominante |
|---|---|---|---|---|
| **Interno** | Administradores, Coordinadores, Profesor/Evaluador | Toman decisiones, definen rumbo, evalúan resultados | Diaria / Semanal | Gestionar de Cerca |
| **Cercano** | Estudiantes, Voluntarios, Instituciones Receptoras | Usan la plataforma, generan valor, reciben el servicio | Diaria / Mensual | Mantener Informados |
| **Externo** | Instituciones de Origen, Proveedores Cloud, Invitados, Público, Competencias | Afectan o son afectados indirectamente; no operan el sistema | Trimestral / Anual | Mantener Satisfechos + Monitorear |

### Flujo de Relaciones

```
                   ┌──────────────────────┐
                   │  Instituciones de     │
                   │  Origen (Voluntarios) │──────────┐
                   └──────────────────────┘          │
                        │                            │
                        ▼                            ▼
        ┌──────────────────────────┐    ┌────────────────────┐
        │   Coordinadores de       │───▶│  VOLUNTARIOS       │
        │   Voluntariado           │    │  (Educadores Base) │
        └──────────────────────────┘    └────────┬───────────┘
                                                 │
                                                 ▼
        ┌──────────────────────┐      ┌──────────────────────────┐
        │  Profesor/Evaluador  │──────│   AULA SEÑORA           │
        └──────────────────────┘      │   (Plataforma)          │
                                      └──────────────────────────┘
                                                 │
                        ┌────────────────────────┼────────────────────┐
                        ▼                        ▼                    ▼
        ┌──────────────────────────┐  ┌────────────────────┐  ┌────────────────┐
        │  Inst. Educativas        │  │  ESTUDIANTES       │  │  Proveedores   │
        │  Receptoras              │  │  (Usuarios Finales)│  │  Cloud         │
        └──────────────────────────┘  └────────────────────┘  └────────────────┘
                                                                       │
                                                                       ▼
                                                      ┌──────────────────────────┐
                                                      │  Invitados / Público /   │
                                                      │  Competencias            │
                                                      └──────────────────────────┘
```

---

## 6. Priorización de Engagement

| Prioridad | Stakeholder | Acción Inmediata |
|---|---|---|
| 🔴 Crítica | Administradores del Sistema | Reunión semanal + dashboard en tiempo real |
| 🔴 Crítica | Profesor / Evaluador | Alinear entregables con rúbrica; demo cada sprint |
| 🟡 Alta | Coordinadores de Voluntariado | Canal directo para requerimientos de programas |
| 🟡 Alta | Estudiantes | Encuesta de satisfacción + notificaciones push |
| 🟡 Alta | Voluntarios / Educadores Base | Newsletter + guías de plataforma |
| 🟢 Media | Instituciones Receptoras | Reportes mensuales de progreso |
| 🟢 Media | Instituciones de Origen | Reporte trimestral de impacto |
| 🔵 Baja | Proveedores Cloud | Monitoreo de costos y SLAs |
| ⚪ Mínima | Invitados / Público | Analítica de landing page |
| ⚪ Mínima | Competencias | Benchmarking trimestral |
| 🟡 Alta | Equipo de Desarrollo | Daily standups, Jira actualizado, retrospectivas |

---

## 7. Datos del Proyecto Extraídos de Jira

> Fuente: Jira Software — `ocadavidramirez9.atlassian.net` — Proyecto DEV (Aula Señora)

| Métrica | Valor |
|---------|-------|
| **Total de issues** | 57 |
| **Epics** | 8 |
| **Historias** | 20 |
| **Subtareas** | 18 |
| **Riesgos** | 10 |
| **Tareas** | 1 |
| **Issues finalizadas** | 53 |
| **Issues en curso** | 4 (DEV-14, DEV-16, DEV-18, DEV-19, DEV-21) |
| **Miembros del equipo** | Oscar Cadavid (Lead), Veruzka Guapacha, Juan Lozano |
| **Workflow** | Tareas por hacer → En curso → Finalizada |
| **Versión** | Release 1.0.0 (Abr 9, 2026) |

### Sprints reales en Jira

| Sprint | Período | Estado |
|--------|---------|--------|
| Sprint 1 — Autenticacion | 19 Mar → 9 Abr 2026 | Cerrado |
| DEV Sprint 2 | 8 Abr → 29 Abr 2026 | Cerrado |
| Sprint 3 — Final | 1 May → 21 May 2026 | Cerrado |
| Sprint 3 — Final 2 | Sin fecha | Futuro |

### Epics reales en Jira

| Epic | Nombre | Sprint |
|------|--------|--------|
| DEV-10 | Autenticación y Seguridad Mobile | Sprint 1 |
| DEV-11 | Gestión de Usuarios y Perfiles | Sprint 1 |
| DEV-23 | Gestión Administrativa y Control de Usuarios | Sprint 2 |
| DEV-34 | Modernización de Interfaz UX/UI | Sprint 2 |
| DEV-35 | Sistema de Cronograma y Tutorías | Sprint 2 |
| DEV-37 | Integraciones Cloud & APIs | Sprint 3 |
| DEV-38 | Panel Admin y Business Intelligence | Sprint 3 |
| DEV-39 | Estabilización, QA y Release | Sprint 3 |
