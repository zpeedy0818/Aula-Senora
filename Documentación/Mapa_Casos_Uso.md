# Mapa Exhaustivo de Casos de Uso (UML) - Aula Señora 🎓

Este documento contiene el desglose total, técnico y granular de **todos los Casos de Uso** (actuales y futuros) del proyecto para trasladarlos a una herramienta de modelado UML como Miro, Lucidchart o Draw.io.

## 👥 1. Inventario de Actores

Define estos nodos fuera del sistema (límites de la plataforma):
1.  **Invitado** (Usuario sin autenticar)
2.  **Estudiante** (Usuario registrado consumiendo aprendizaje)
3.  **Voluntario** (Usuario registrado que provee la enseñanza)
4.  **Administrador** (Usuario con control total sobre la plataforma)
5.  **Sistema de Seguridad Auth** (Actor de sistema interno)
6.  **Sistema Firebase / Push** (Actor externo para mensajería y notificaciones futuras)

---

## ⚙️ 2. Desglose Exhaustivo de Casos de Uso (Óvalos)

Esta es la lista detallada de cada función, ideal para representar como óvalos. Se especifica si es una función existente `[ACTUAL]` o a desarrollar `[FUTURO]`.

### 🟨 Subsistema: Acceso y Gestión de Identidad
*   **CU-01:** Explorar Landing Page y Secciones Informativas `[ACTUAL]`
*   **CU-02:** Registrarse como Estudiante `[ACTUAL]` *(Genera registro en tabla: usuarios)*
*   **CU-03:** Registrarse como Voluntario `[ACTUAL]` *(Genera registro en tablas: usuarios + voluntarios)*
*   **CU-04:** Iniciar Sesión `[ACTUAL]`
*   **CU-05:** Recuperar Contraseña / Olvidé mi Contraseña `[FUTURO]`
*   **CU-06:** Cerrar Sesión (Logout) `[ACTUAL]`
*   **CU-07:** Configurar Autenticación Biométrica (Huella / Facial en la App) `[FUTURO]`

### 🟩 Subsistema: Panel de Estudiante (Aprendizaje)
*   **CU-08:** Acceder al Dashboard Principal de Estudiante `[ACTUAL]`
*   **CU-09:** Configurar Perfil de Usuario (Editar datos, foto) `[FUTURO]`
*   **CU-10:** Explorar Catálogo General de Cursos/Clases `[FUTURO]`
*   **CU-11:** Inscribirse a un Curso / Clase específica `[FUTURO]`
*   **CU-12:** Visualizar Material Multimedia (Player de video, visualizador PDF) `[FUTURO]`
*   **CU-13:** Descargar Material Localmente (Cache móvil para modo offline) `[FUTURO]`
*   **CU-14:** Acceder y Leer Material en Modo Sin Conexión `[FUTURO]`
*   **CU-15:** Enviar Mensaje/Duda a un Voluntario `[FUTURO]`

### 🟦 Subsistema: Panel de Voluntario (Enseñanza)
*   **CU-16:** Acceder al Dashboard Principal de Voluntario `[ACTUAL]`
*   **CU-17:** Completar y Editar Ficha de Habilidades Técnicas `[FUTURO]`
*   **CU-18:** Gestionar Institución de Procedencia `[FUTURO]`
*   **CU-19:** Crear y Configurar Nuevo Curso o Módulo `[FUTURO]`
*   **CU-20:** Subir y Eliminar Material Multimedia dentro de un curso `[FUTURO]`
*   **CU-21:** Visualizar Estudiantes Inscritos `[FUTURO]`
*   **CU-22:** Responder Dudas a Estudiantes `[FUTURO]`

### 🟪 Subsistema: Panel de Administrador (Global)
*   **CU-23:** Acceder al Dashboard Consolidado General `[ACTUAL]`
*   **CU-24:** Listar, Buscar y Filtrar Usuarios de la Plataforma `[FUTURO]`
*   **CU-25:** Bloquear / Reactivar Cuenta de Usuario `[FUTURO]`
*   **CU-26:** Validar y Aprobar Perfiles de Voluntarios Nuevos `[FUTURO]`
*   **CU-27:** Gestionar Taxonomía (Etiquetas, Categorías de clases) `[FUTURO]`
*   **CU-28:** Auditar Controles Básicos del Sitio `[FUTURO]`
*   **CU-29:** Crear y Despachar Notificaciones Masivas (Push) `[FUTURO]`

### ⬛ Subsistema: Automatizaciones y Seguridad Backend
*   **CU-30:** Validar Integridad Humana (reCAPTCHA v2) `[ACTUAL]`
*   **CU-31:** Restringir Acceso por Intentos Fallidos (Fuerza Bruta) `[ACTUAL]`
*   **CU-32:** Despachar Alerta Push Local `[FUTURO]`

---

## 🔗 3. Estructura de Relaciones (Líneas y Flechas UML)

Para conectar los elementos en Miro, usa las siguientes reglas exactas:

### Conexiones Directas (Asociación Simple)
*   **Invitado** se conecta a: `CU-01`, `CU-02`, `CU-03`, `CU-04`.
*   **Estudiante** se conecta a: `CU-04`, `CU-06`, `CU-07`, `CU-08`, `CU-09`, `CU-10`, `CU-11`, `CU-12`, `CU-13`, `CU-14`, `CU-15`.
*   **Voluntario** se conecta a: `CU-04`, `CU-06`, `CU-07`, `CU-16`, `CU-17`, `CU-18`, `CU-19`, `CU-20`, `CU-21`, `CU-22`.
*   **Administrador** se conecta a: `CU-04`, `CU-06`, `CU-23`, `CU-24`, `CU-25`, `CU-26`, `CU-27`, `CU-28`, `CU-29`.
*   **Sistema de Seguridad Auth** interviene en: `CU-30`, `CU-31`.
*   **Sistema Firebase / Push** interviene en: `CU-32`.

### Relaciones Especiales UML (`<<include>>` y `<<extend>>`)
Usa líneas punteadas con flecha apuntando en el sentido indicado, y colócales una nota sobre la flecha:

*   **`<<include>>` (Es un paso obligatorio):**
    *   De `CU-02 (Reg. Estudiante)` 👉 apunta a 👉 `CU-30 (Validar reCAPTCHA)`
    *   De `CU-03 (Reg. Voluntario)` 👉 apunta a 👉 `CU-30 (Validar reCAPTCHA)`
    *   De `CU-04 (Iniciar Sesión)` 👉 apunta a 👉 `CU-30 (Validar reCAPTCHA)`
    *   De `CU-29 (Despachar Notificaciones Masivas)` 👉 apunta a 👉 `CU-32 (Despachar Alerta Push)`

*   **`<<extend>>` (Es un paso opcional o de desviación de flujo):**
    *   De `CU-05 (Recuperar Contraseña)` 👉 apunta hacia 👉 `CU-04 (Iniciar Sesión)`
    *   De `CU-31 (Restringir Acceso por Intentos Fallidos)` 👉 apunta hacia 👉 `CU-04 (Iniciar Sesión)`
    *   De `CU-13 (Descargar Material Localmente)` 👉 apunta hacia 👉 `CU-12 (Visualizar Material Multimedia)`
