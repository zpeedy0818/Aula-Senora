# Informe de Planificación de Jira: Sprint 3 - Aula Señora 🎓

Este informe contiene toda la información necesaria para cargar el **Sprint 3** en Jira de forma profesional, incluyendo Épicas, Historias de Usuario, Estimaciones y Cronograma.

---

## 🏗️ Estructura de Épicas

| Clave Épica | Nombre de la Épica | Descripción |
| :--- | :--- | :--- |
| **DEV-300** | **Integraciones Cloud & APIs** | Implementación de servicios externos (Cloudinary, Google Meet) y gestión de archivos en la nube. |
| **DEV-400** | **Admin Dashboard & BI** | Desarrollo de la lógica administrativa funcional y visualización de datos mediante gráficas interactivas. |
| **DEV-500** | **Estabilización & UX Release** | Fase final de QA, responsividad extrema y pulido estético para la entrega final. |

---

## 📋 Listado Detallado de Tareas (Historias de Usuario)

### Épica: DEV-300
| ID | Título de la Tarea | Descripción Detallada | Fechas (Rec.) | SP | Prioridad |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **DEV-301** | Infraestructura y Servicios Base | Configuración de dependencias Maven (`pom.xml`), inicialización de clientes de Cloudinary y Google API, y manejo de variables de entorno. | 01 May - 03 May | 5 | Alta |
| **DEV-302** | Gestión de Materiales Educativos | Creación de entidad `Material`, repositorio y servicio. Implementación de subida de archivos (PDF/Imágenes) a Cloudinary y guardado de URLs. | 04 May - 06 May | 8 | Alta |
| **DEV-303** | Integración de Google Meet | Implementación de lógica para generar enlaces automáticos de Google Meet al agendar tutorías, vinculando el calendario del voluntario. | 07 May - 08 May | 5 | Media |

### Épica: DEV-400
| ID | Título de la Tarea | Descripción Detallada | Fechas (Rec.) | SP | Prioridad |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **DEV-304** | Dashboard Admin Funcional | Conectar los KPIs (Estudiantes, Voluntarios, Tutorías) a datos reales. Implementar tabla de aprobación/rechazo de voluntarios pendientes. | 09 May - 11 May | 5 | Alta |
| **DEV-305** | Visualización de Datos (Gráficas) | Integración de Chart.js en el frontend. Desarrollo de gráficas de pastel para distribución de roles y gráficas de barras para estados de tutorías. | 12 May - 15 May | 8 | Media |

### Épica: DEV-500
| ID | Título de la Tarea | Descripción Detallada | Fechas (Rec.) | SP | Prioridad |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **DEV-306** | Pulido, QA y UX Final | Auditoría de responsividad en 320px, implementación de micro-animaciones (transiciones suave), limpieza de código y documentación final. | 16 May - 20 May | 3 | Alta |

---

## ⚙️ Notas de Configuración Técnica (Para la descripción de tareas)

*   **Cloudinary:** Se requiere configurar `CLOUDINARY_URL` en el archivo `.env` o variables de entorno del sistema.
*   **Google Meet:** Requiere habilitar la "Google Calendar API" en Google Cloud Console y descargar el `credentials.json`.
*   **Charts:** Usar **Chart.js v4.x** vía CDN para evitar sobrecargar el peso del proyecto localmente.
*   **Base de Datos:** La tabla `Material` debe estar relacionada con `Aula` mediante un `Long aula_id`.

---

**Resumen del Sprint:**
- **Total Story Points:** 34
- **Fecha Inicio:** 01 Mayo 2026
- **Fecha Fin:** 20 Mayo 2026
- **Objetivo Principal:** Plataforma 100% funcional e integrada con servicios de terceros.
