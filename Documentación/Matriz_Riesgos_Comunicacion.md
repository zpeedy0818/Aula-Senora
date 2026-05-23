# Matriz de Riesgos de Comunicación — Aula Señora

> **Proyecto:** Plataforma de aprendizaje que conecta voluntarios con estudiantes
> **Equipo:** Oscar Cadavid (Lead), Juan Lozano, Veruzka Guapacha
> **Base:** Plan de Comunicación — Aula Señora + Análisis de Stakeholders
> **Fecha:** Mayo 2026

---

## 1. Listado de Riesgos de Comunicación

| # | Riesgo | Descripción | Causa Raíz |
|---|--------|-------------|------------|
| R1 | **Daily standups no ocurren** | El equipo pierde la alineación diaria, cada miembro trabaja en prioridades distintas y se duplican esfuerzos | Falta de disciplina en la ceremonia Scrum; equipo pequeño asume que "ya saben lo que hay que hacer" |
| R2 | **Feedback del evaluador no documentado** | Los comentarios del profesor/evaluador en sprint reviews se pierden porque no se registran en Jira ni en actas | No hay un rol asignado para tomar minutas; se confía en la memoria |
| R3 | **Jira desactualizado** | Las tareas avanzan pero no se reflejan en el tablero Jira, perdiendo la trazabilidad del progreso real | Prioridad al código sobre la administración del tablero; tareas sin mover de columna |
| R4 | **Sin notificaciones a usuarios sobre cambios** | Nuevas funcionalidades (Google Meet, Cloudinary) se lanzan sin avisar a estudiantes y voluntarios, resultando en baja adopción | Falta de un proceso de release communication integrado al flujo de desarrollo |
| R5 | **Decisiones en canales informales sin registro** | Discusiones importantes vía WhatsApp/Discord no quedan documentadas, se pierden acuerdos y decisiones técnicas | Cultura de mensajería instantánea sin disciplina de volcado a Jira o documento compartido |
| R6 | **Reportes semanales no se envían al evaluador** | El profesor/evaluador no recibe actualizaciones periódicas del progreso, generando incertidumbre sobre el avance real | No hay un recordatorio automatizado ni responsable fijo para la elaboración del reporte |
| R7 | **Documentación técnica desactualizada** | El código cambia pero la documentación (arquitectura, endpoints, modelos) no se actualiza, dificultando el mantenimiento futuro | La documentación se ve como actividad final, no como parte del desarrollo |
| R8 | **Sprint goals ambiguos** | Los objetivos del sprint no se comunican con claridad, cada miembro interpreta las prioridades de forma diferente | Planning insuficiente; historias de usuario sin criterios de aceptación claros |
| R9 | **Silencio en bloqueadores** | Un miembro del equipo encuentra un bloqueador pero no lo comunica, el issue escala sin que nadie más lo sepa | Miedo a reportar retrasos; cultura de "lo resuelvo solo" |
| R10 | **Sin comunicación a instituciones educativas** | Las instituciones de origen y receptoras no reciben informes de impacto, debilitando su apoyo al proyecto | No hay un proceso establecido para la generación periódica de informes |
| R11 | **Post-mortem no se realiza** | Después de incidentes (bugs críticos, caídas) no se hace análisis de causas raíz, los mismos errores se repiten en futuros sprints | Cultura de "apagar incendios" sin pausa para reflexión |
| R12 | **Lenguaje técnico inadecuado para usuarios** | Las comunicaciones a estudiantes y voluntarios usan jerga técnica que ellos no entienden, generando confusión y tickets de soporte | Falta de revisión de contenido por un perfil no técnico antes de publicar |

---

## 2. Clasificación de Riesgos

| # | Riesgo | Probabilidad | Impacto | Nivel de Exposición | Stakeholders Afectados |
|---|--------|-------------|---------|-------------------|----------------------|
| R1 | Daily standups no ocurren | Media | Alto | **Crítico** | Equipo de Desarrollo, Administradores |
| R2 | Feedback del evaluador no documentado | Alta | Alto | **Crítico** | Profesor/Evaluador, Equipo de Desarrollo |
| R3 | Jira desactualizado | Alta | Medio | **Alto** | Equipo de Desarrollo, Coordinadores |
| R4 | Sin notificaciones a usuarios | Media | Alto | **Crítico** | Estudiantes, Voluntarios |
| R5 | Decisiones sin registro en canales informales | Alta | Medio | **Alto** | Equipo de Desarrollo |
| R6 | Reportes semanales no enviados | Baja | Alto | **Alto** | Profesor/Evaluador |
| R7 | Documentación técnica desactualizada | Alta | Medio | **Alto** | Equipo de Desarrollo, Administradores |
| R8 | Sprint goals ambiguos | Media | Alto | **Crítico** | Equipo de Desarrollo |
| R9 | Silencio en bloqueadores | Media | Crítico | **Crítico** | Equipo de Desarrollo, Administradores, Profesor/Evaluador |
| R10 | Sin comunicación a instituciones | Baja | Alto | **Alto** | Instituciones de Origen, Instituciones Receptoras |
| R11 | Post-mortem no se realiza | Alta | Medio | **Alto** | Equipo de Desarrollo, Administradores |
| R12 | Lenguaje técnico inadecuado | Media | Medio | **Medio** | Estudiantes, Voluntarios |

### Escala de Valoración

| Probabilidad | Descripción |
|-------------|-------------|
| Alta | Ocurre frecuentemente (> 70% de los casos) |
| Media | Ocurre ocasionalmente (30% – 70%) |
| Baja | Ocurre raramente (< 30%) |

| Impacto | Descripción |
|---------|-------------|
| Crítico | Amenaza la finalización del proyecto o causa daño irreversible |
| Alto | Afecta significativamente el cronograma, calidad o relaciones con stakeholders clave |
| Medio | Impacto moderado, manejable con recursos adicionales |
| Bajo | Impacto menor, no afecta los objetivos del proyecto |

---

## 3. Matriz de Riesgos (Probabilidad × Impacto)

```
                    IMPACTO
          BAJO      MEDIO      ALTO      CRÍTICO
        ┌────────────────────────────────────────────┐
     A  │                                            │
     L  │              R3       R2                   │
     T  │              R5       R7        R9         │
     A  │              R11      R12                  │
        │                                            │
     P  │                                            │
     R  │              R1                            │
     O  │              R4              R9            │
     B  │              R8                            │
     A  │                                            │
     B  │                                  R6        │
     I  │                                  R10       │
     L  │                                            │
     I  │                                            │
     D  │                                            │
     A  └────────────────────────────────────────────┘
     D         Zona Baja    Zona Media    Zona Alta    Zona Crítica
```

### Cuadrantes de la Matriz

| Zona | Color | Riesgos | Acción Requerida |
|------|-------|---------|-----------------|
| **Crítica** (Prob. Media-Alta × Impacto Alto-Crítico) | 🔴 | R1, R2, R4, R8, R9 | Mitigación inmediata. Plan de acción antes del próximo sprint |
| **Alta** (Prob. Alta × Impacto Medio, o Prob. Baja × Impacto Alto) | 🟡 | R3, R5, R6, R7, R10, R11 | Mitigación planificada. Incluir en la gestión de riesgos del sprint |
| **Media** (Prob. Media × Impacto Medio) | 🟢 | R12 | Monitoreo activo. Revisar periodicamente |
| **Baja** (Prob. Baja × Impacto Bajo) | ⚪ | — | Solo monitoreo |

---

## 4. Plan de Mitigación

### 🔴 Zona Crítica — Mitigación Inmediata

| # | Riesgo | Estrategia | Acciones Concretas | Responsable | Plazo |
|---|--------|-----------|-------------------|-------------|-------|
| R1 | Daily standups no ocurren | **Prevenir** — Establecer ritual obligatorio | • Fijar horario fijo diario (ej. 9:00 AM, 15 min máx)<br>• Usar un bot en WhatsApp/Discord que recuerde el daily<br>• Rotar quién lidera el daily cada semana | Oscar (Lead) | Inmediato |
| R2 | Feedback del evaluador no documentado | **Prevenir** — Asignar minutas obligatorias | • Designar un tomador de minutas en cada sprint review (rotativo)<br>• Usar plantilla de Acta de Sprint Review ya diseñada<br>• Subir el acta a Jira como issue adjunto dentro de 24h | Juan / Veruzka (rotativo) | Cada sprint review |
| R4 | Sin notificaciones a usuarios sobre cambios | **Prevenir** — Integrar release communication | • Agregar checklist de "comunicación a usuarios" en la definición de Done<br>• Redactar anuncio para estudiantes y otro para voluntarios antes de cada release<br>• Usar plantilla de Notificación a Usuarios ya diseñada | Veruzka | Por release |
| R8 | Sprint goals ambiguos | **Prevenir** — Mejorar calidad del Sprint Planning | • Definir un Sprint Goal de una línea al inicio del planning<br>• Cada historia debe tener criterios de aceptación antes de entrar al sprint<br>• Validar que los 3 miembros interpreten igual el objetivo | Oscar (Lead) | Cada sprint planning |
| R9 | Silencio en bloqueadores | **Mitigar** — Cultura de transparencia | • Regla: si un bloqueador dura >4h sin avance, se comparte en el chat del equipo<br>• Los bloqueadores se registran en Jira con etiqueta "bloqueador"<br>• El Lead pregunta activamente "¿hay bloqueadores?" en cada daily | Todo el equipo | Inmediato |

### 🟡 Zona Alta — Mitigación Planificada

| # | Riesgo | Estrategia | Acciones Concretas | Responsable | Plazo |
|---|--------|-----------|-------------------|-------------|-------|
| R3 | Jira desactualizado | **Mitigar** — Disciplina de tablero | • Mover tareas de columna inmediatamente al empezar/terminar trabajo<br>• Al final del día, 5 min para actualizar el tablero<br>• Usar la integración GitHub + Jira para transiciones automáticas desde commits | Todo el equipo | Diario |
| R5 | Decisiones sin registro en canales informales | **Mitigar** — Volcar decisiones a Jira | • Regla: toda decisión técnica discutida en WhatsApp se resume en un comentario del issue relacionado en Jira<br>• Si no hay issue relacionado, crear una tarea "Decisión: [tema]" | Oscar (Lead) | Cada decisión |
| R6 | Reportes semanales no enviados | **Prevenir** — Automatizar recordatorio | • Configurar recordatorio semanal (ej. Google Calendar, cada viernes 4 PM)<br>• Usar la plantilla de Reporte Semanal ya diseñada<br>• Oscar elabora, Juan y Veruzka revisan antes de enviar | Oscar (Lead) | Cada viernes |
| R7 | Documentación técnica desactualizada | **Mitigar** — Documentación como parte del desarrollo | • En la definición de Done de cada historia, incluir "documentación actualizada"<br>• Mantener un README de arquitectura vivo en la raíz del proyecto<br>• Documentar endpoints nuevos en el código con Swagger/OpenAPI | Juan | Por historia |
| R10 | Sin comunicación a instituciones | **Prevenir** — Establecer ciclo de reportes | • Programar recordatorio trimestral para informe a instituciones<br>• Usar plantilla de Informe Trimestral ya diseñada<br>• Oscar elabora y envía, con copia al profesor/evaluador | Oscar (Lead) | Trimestral |
| R11 | Post-mortem no se realiza | **Mitigar** — Cultura de mejora continua | • Después de cualquier incidente Nivel 3 o 4 (ver protocolo de escalamiento), programar post-mortem en máximo 5 días<br>• Usar formato: 5 Porqués + plan de acción<br>• Documentar en Jira como issue tipo "Riesgo" cerrado con resolución | Todo el equipo | Por incidente |

### 🟢 Zona Media — Monitoreo Activo

| # | Riesgo | Estrategia | Acciones Concretas | Responsable | Plazo |
|---|--------|-----------|-------------------|-------------|-------|
| R12 | Lenguaje técnico inadecuado para usuarios | **Mitigar** — Revisión de contenido | • Que un miembro del equipo lea en voz alta cualquier comunicación a usuarios antes de enviarla<br>• Si hay dudas, simplificar: preguntar "¿lo entendería mi abuela?"<br>• Incluir ejemplos concretos en lugar de términos técnicos | Veruzka | Por cada comunicación a usuarios |

---

## 5. Matriz de Relación Riesgo ↔ Stakeholder

```
                          STAKEHOLDERS
                  ┌─────────────────────────────────────────────────────────────┐
                  │  E   A   P   I   I   P   E   V   U   P   C   E            │
                  │  q   d   r   O   R   r   s   o   s   ú   o   q            │
                  │  u   m   o   r   e   o   t   l   u   b   m   u            │
                  │  i   i   f   i   c   v   u   u   a   l   p   i            │
                  │  p   n   e   g   e   e   d   n   r   i   e   p            │
                  │  o     s   e   p   e   i   t   i   c   t   o            │
                  │     D   o   n   t   d   a   a   o   e   e   D            │
                  │     e   r     o   o   r   r   G   n   n   e            │
                  │     s     E     r   r   i   i   e   c   c   s            │
                  │     a     v   r   e   o   o   n   i   i   a            │
                  │     r     a   a   s   s   s   e   a   a   r            │
                  │     r     l     s         r   r   s   s   r            │
R                │     o     u     o     d   a   a   l     o            │
I                │     l     a     r     o   l     l     l   l           │
E                │     l     d     g     r     m     m     l   l         │
S                │     o     o     e     e     e     e     o   o         │
G                │     r     r     n     s     n     n     s     s       │
O                │                     /                                │
                  │                     E                                │
                  │                     v                                │
                  │                     a                                │
                  │                     l                                │
                  ├─────────────────────────────────────────────────────────────┤
      R1          │  ●     ●                                                      │
      R2          │        ●        ●                                             │
      R3          │  ●     ●                                                      │
      R4          │                    ●        ●                                │
      R5          │  ●                                                            │
      R6          │                 ●                                            │
      R7          │  ●     ●                                                      │
      R8          │  ●                                                            │
      R9          │  ●     ●        ●                                            │
      R10         │                          ●        ●                          │
      R11         │  ●     ●                                                      │
      R12         │                          ●        ●                          │
                  └─────────────────────────────────────────────────────────────┘
```

### Leyenda de Stakeholders

| Abreviatura | Stakeholder | Abreviatura | Stakeholder |
|-------------|-------------|-------------|-------------|
| **Eq. Des.** | Equipo de Desarrollo | **Vol.** | Voluntarios / Educadores Base |
| **Admin.** | Administradores del Sistema | **Usu. Inv.** | Usuarios Invitados |
| **Prof. Eval.** | Profesor / Evaluador | **Púb. Gen.** | Público General |
| **Inst. Origen** | Instituciones Educativas de Origen | **Comp.** | Plataformas / Competencias |
| **Inst. Rec.** | Instituciones Educativas Receptoras | **Coord.** | Coordinadores de Voluntariado |
| **Prov. Cloud** | Proveedores Cloud | | |

---

## 6. Relación con Riesgos Existentes en Jira

El proyecto ya cuenta con **10 issues tipo Riesgo** en Jira (DEV-13 a DEV-22). Algunos están directamente vinculados con fallas de comunicación:

| Riesgo Jira | Descripción | Relación con este documento |
|-------------|-------------|---------------------------|
| DEV-14 | Complejidad excesiva en interfaz para estudiantes/voluntarios | Se relaciona con **R12** (lenguaje técnico inadecuado) — ambos afectan la experiencia de usuario por mala comunicación |
| DEV-15 | Desviación del alcance por cambios frecuentes de requerimientos | Se relaciona con **R2** (feedback no documentado) y **R5** (decisiones sin registro) — cambios no comunicados formalmente |
| DEV-16 | Insuficiente financiación para servidores y mantenimiento | Se relaciona con **R10** (sin comunicación a instituciones) — falta de reportes puede llevar a pérdida de apoyo financiero |
| DEV-21 | Problemas de privacidad y protección de datos de menores | Se relaciona con **R9** (silencio en bloqueadores) — un issue de seguridad no reportado a tiempo puede escalar |

---

## 7. Indicadores Clave de Riesgo (KRI)

| # | KRI | Disparador | Riesgo Asociado |
|---|-----|-----------|-----------------|
| KRI-1 | Días sin daily standup | 2 días consecutivos sin daily | R1 |
| KRI-2 | Sprint review sin acta subida a Jira | 48h después del sprint review | R2 |
| KRI-3 | Tareas en columna "En curso" sin actualizar por +3 días | 3 días sin movimiento en el tablero | R3 |
| KRI-4 | Release sin comunicación enviada a usuarios | 24h después del despliegue | R4 |
| KRI-5 | Bloqueador reportado >8h sin resolución ni escalamiento | 8h sin actividad en un issue bloqueador | R9 |
| KRI-6 | Reporte semanal no enviado antes del lunes siguiente | Viernes sin envío | R6 |

---

## 8. Resumen Ejecutivo

| Métrica | Valor |
|---------|-------|
| **Total de riesgos identificados** | 12 |
| **Riesgos en zona Crítica** | 5 (R1, R2, R4, R8, R9) |
| **Riesgos en zona Alta** | 6 (R3, R5, R6, R7, R10, R11) |
| **Riesgos en zona Media** | 1 (R12) |
| **Riesgos en zona Baja** | 0 |
| **Estrategias de mitigación definidas** | 12 |
| **Indicadores KRI para monitoreo** | 6 |
| **Stakeholders impactados** | 8 de 12 |
| **Riesgos Jira relacionados** | 4 (DEV-14, DEV-15, DEV-16, DEV-21) |

### Acciones Prioritarias (Siguiente Sprint)

1. ✅ Establecer horario fijo de daily standup (R1)
2. ✅ Asignar tomador de minutas para próxima sprint review (R2)
3. ✅ Agregar checklist de comunicación a usuarios en Definition of Done (R4)
4. ✅ Definir criterios de aceptación en cada historia antes del planning (R8)
5. ✅ Comunicar regla de bloqueadores >4h al equipo (R9)
