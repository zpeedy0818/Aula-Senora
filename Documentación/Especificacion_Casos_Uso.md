# Especificación Detallada de Casos de Uso - Aula Señora 🎓

Este documento sirve como respaldo documental para la defensa del diseño del sistema. Detalla el comportamiento exacto de las funcionalidades core de la plataforma, respondiendo al "qué hace", "quién lo hace" y "qué pasa si falla" de cada función.

---

## 🔐 1. Módulo de Seguridad y Autenticación (Core Actual)

### CU-04: Iniciar Sesión (Login)
*   **Actor Principal:** Estudiante / Voluntario / Administrador.
*   **Precondición:** El usuario debe estar registrado en la base de datos y tener una cuenta activa (no bloqueada).
*   **Flujo Principal:** 
    1. El usuario ingresa sus credenciales (correo y contraseña).
    2. El sistema invoca al `CU-30: Validar reCAPTCHA` mediante el `RecaptchaService`.
    3. Si el captcha es válido, Spring Security (mediante `UsuarioDetailsService`) comprueba las credenciales.
    4. El sistema redirige al dashboard correspondiente según el Rol del usuario.
*   **Flujo Alternativo (`<<extend>> CU-31`):** Si las credenciales son incorrectas, el sistema rastrea el fallo con `LoginAttemptService`. Si supera el límite, la IP o cuenta se bloquea por 24 horas y se impide el acceso.
*   **Postcondición:** Se crea un contexto de seguridad (sesión activa) en el backend y el usuario entra a la plataforma.

### CU-02 / CU-03: Registro de Usuario (Estudiante / Voluntario)
*   **Actor Principal:** Invitado.
*   **Precondición:** El correo electrónico ingresado no debe existir en la base de datos.
*   **Flujo Principal:**
    1. El invitado completa el formulario (`RegistroController`).
    2. El sistema invoca al `CU-30: Validar reCAPTCHA`.
    3. Se evalúa el selector de Rol. Si es Estudiante, se guarda en la tabla `usuarios`. Si es Voluntario, se guarda en `usuarios` y su información extendida en `voluntarios`.
    4. Se encripta la contraseña usando `BCryptPasswordEncoder`.
*   **Postcondición:** El usuario queda registrado y es redirigido a la pantalla de Login con un mensaje de éxito.

---

## 🧑‍🎓 2. Módulo de Aprendizaje (Estudiante)

### CU-09: Explorar Catálogo de Cursos
*   **Actor Principal:** Estudiante.
*   **Precondición:** El estudiante debe estar logueado en la plataforma.
*   **Flujo Principal:** El estudiante navega por la interfaz gráfica (Mobile First), visualizando la lista de contenidos educativos disponibles publicados por los voluntarios u organizados por el administrador.

### CU-11: Visualizar Material (Multimedia/PDF)
*   **Actor Principal:** Estudiante.
*   **Precondición:** El usuario debe tener acceso o estar inscrito (`<<extend>> CU-10` en el curso).
*   **Flujo Principal:** El sistema carga el visor de videos o de PDF embebido en la aplicación o navegador web, ajustándose responsivamente al tamaño de pantalla.
*   **Flujo Alternativo (`<<extend>> CU-12`):** Si el usuario se encuentra en versión de App Móvil, puede optar por descargar el material para consumo offline (almacenamiento en caché).

---

## 🧑‍🏫 3. Módulo de Voluntariado (Enseñanza)

### CU-16: Ver Dashboard Voluntario
*   **Actor Principal:** Voluntario.
*   **Precondición:** Autenticado y aprobado por un Administrador (cuenta verificada).
*   **Flujo Principal:** El voluntario visualiza sus estadísticas, materias o estudiantes a cargo, así como el listado de contenido que él mismo ha subido a la plataforma.

### CU-18: Subir Material
*   **Actor Principal:** Voluntario.
*   **Precondición:** El voluntario debe tener un curso previamente creado.
*   **Flujo Principal:** 
    1. El voluntario selecciona un archivo (PDF, DOCX) o enlaza un video.
    2. El servidor valida la extensión y tamaño del archivo.
    3. Se actualizan los punteros en la base de datos relacionando el contenido con el curso.
*   **Postcondición:** El material queda expuesto y disponible para los Estudiantes de ese curso.

---

## ⚙️ 4. Módulo de Administración (Global)

### CU-22: Gestionar Usuarios de la BD
*   **Actor Principal:** Administrador.
*   **Precondición:** Autenticación con credencial de `ROLE_ADMIN`.
*   **Flujo Principal:** El administrador puede visualizar el listado CRUD completo. Puede modificar roles, reiniciar contraseñas en caso de olvido, o eliminar cuentas (baja lógica).
*   **Postcondición:** La tabla `usuarios` y los permisos de Spring Security se actualizan en tiempo de ejecución.

### CU-24: Emitir Notificaciones Masivas (Push FCM)
*   **Actor Principal:** Administrador.
*   **Precondición:** El servidor debe tener configurado y asociado el proyecto de Firebase Cloud Messaging (FCM).
*   **Flujo Principal:**
    1. El admin escribe el título y el cuerpo del mensaje global.
    2. Al confirmar, el sistema ejecuta el `CU-32` comunicándose con la API de Firebase.
    3. Firebase distribuye el mensaje directamente a los dispositivos móviles de Estudiantes y Voluntarios.
*   **Postcondición:** Se reporta el éxito del despacho y los usuarios perciben la notificación en sus teléfonos.

---

## 🛡️ 5. Módulo de Automatización del Sistema

### CU-30: Validar reCAPTCHA
*   **Actor Automático:** Sistema.
*   **Flujo:** Implementación técnica del `RecaptchaService.java`. Antes de procesar cualquier formulario POST importante, el sistema hace un `POST` auxiliar a los servidores de Google confirmando el token oculto. Si Google detecta probabilidad de BOT (score bajo o captcha fallido), el sistema rechaza de plano la petición devolviendo al usuario a la vista origen sin tocar la base de datos.
