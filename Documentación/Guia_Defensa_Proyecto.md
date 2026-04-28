# Guía de Emergencia: Defensa del Proyecto "Aula Señora" 🛡️

Este documento está diseñado como tu **"Acordeón" o "Cheat Sheet"** para responder preguntas difíciles, técnicas o de diseño que el profesor pueda hacerte durante la sustentación del proyecto.

## 1. Arquitectura y Patrones de Diseño

**❓ "Pregunta: ¿Qué arquitectura utilizaron para desarrollar el proyecto?"**
*   **Respuesta Ideal:** Utilizamos una **Arquitectura en Capas basada en el patrón MVC (Modelo-Vista-Controlador)**. 
    *   **Controladores (`controller`):** Reciben las peticiones HTTP de los usuarios, coordinan y envían las respuestas (las vistas HTML o redirecciones).
    *   **Servicios (`service`):** Contienen toda nuestra **Lógica de Negocio** (por ejemplo, `UsuarioService`). Aseguramos que los controladores no procesen datos.
    *   **Repositorios / Acceso a Datos:** Usamos Spring Data JPA que abstrae el acceso a la Base de Datos.
    *   **Modelo / Entidades:** Las clases que representan las tablas en la Base de Datos.

**❓ "Pregunta: ¿Por qué usaron clases con el sufijo DTO como `RegistroDTO` en lugar de usar directamente la Entidad Usuario?"**
*   **Respuesta Ideal:** Usamos el patrón **DTO (Data Transfer Object)** por seguridad y limpieza. Nos permite recibir de la vista únicamente los datos necesarios (como correo, contraseña, rol) sin exponer nuestra estructura de base de datos completa. Además, previene ataques de **Over-posting** o inyección masiva de propiedades.

## 2. Seguridad y Autenticación

**❓ "Pregunta: ¿Qué pasa si un hacker accede a su base de datos? ¿Pueden ver las contraseñas de los usuarios?"**
*   **Respuesta Ideal:** No. Las contraseñas **no se guardan en texto plano**. Utilizamos **BCrypt** (a través de `BCryptPasswordEncoder` de Spring Security) para hacer un "hash" de la contraseña en una sola vía con "Salting". Ni siquiera nosotros como administradores podemos saber cuál es la contraseña original.

**❓ "Pregunta: En la pantalla de registro y login, ¿cómo evitan que bots saturen el sistema o hagan ataques de fuerza bruta?"**
*   **Respuesta Ideal:** Implementamos **Google reCAPTCHA v2**. El lado del cliente (formulario web) envía un token de desafío al backend. Nuestro controlador (ej. `RegistroController`) procesa y envía este token a los servidores de Google para validar si la interacción fue humana o bot, **antes** de interactuar con nuestra base de datos.

## 3. Base de Datos

**❓ "Pregunta: ¿Qué tecnología de base de datos usan y por qué?"**
*   **Respuesta Ideal:** Utilizamos una base de datos relacional (**MySQL**). Al tratarse de un modelo que vincula Estudiantes con Voluntarios y Cursos, una base de datos SQL nos permite asegurar consistencia (Integridad Referencial) y realizar cruces de información estructurados. El intermediario (ORM) encargado de hacer las consultas es **Hibernate (vía JPA)**.

## 4. Metodología de Trabajo y Gestión

**❓ "Pregunta: ¿Cómo se organizaron para desarrollar esto en equipo?"**
*   **Respuesta Ideal:** Aplicamos un marco de trabajo ágil. Las tareas, Historias de Usuario y Bugs los gestionamos mediante **Jira**. Vinculamos nuestro repositorio de código permitiendo que cada **Commit en Git** estuviera atado directamente al ID de la tarea en Jira (ej. `PROJ-123: Agregar RegistroDTO`). Esto nos dio trazabilidad total de quién hizo qué y por qué, organizando las entregas por Sprints.

## 🌟 Tips para salir de apuros:

*   **Si no recuerdas una sintaxis exacta (ej. cómo se llama una anotación):**
    *   *"Profesor, no tengo de memoria el nombre exacto de la directiva, pero el concepto es que utilizamos la inyección de dependencias del framework mediante anotaciones (como `@Autowired` o constructores) para desacoplar las clases."*
*   **Si pregunta por algo que no alcanzaron a hacer (Ej. "¿Hicieron la app móvil nativa?"):**
    *   *"Como vimos en la Matriz de Poder e Interés, priorizamos a los usuarios base. Para esta primera fase (MVP) la premisa técnica fue **Mobile-First App Web**. Toda la plataforma (CSS/HTML) es responsiva para dispositivos móviles desde el navegador, lo cual prepara el terreno para una futura app con React Native / Flutter consumiendo nuestros ya listos Endpoints REST."*
*   **Si el código falla en vivo (Efecto Demo):**
    *   *"Hemos capturado este tipo de excepciones en los logs. Generalmente el archivo `run_env_log.txt` o `application.log` que configuramos arrojará el `stacktrace` exacto. Permítame revisar qué excepción arrojó... [revisa si es un NullPointer o Conexión rechazada a la BDD]."*
