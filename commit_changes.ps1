$ErrorActionPreference = "Stop"

# Remove log files
Remove-Item -Force error_log.txt, final_run_log.txt, new_server_log.txt, server_log.txt -ErrorAction SilentlyContinue

# Veruzka Commits
git add src/main/java/aulasenora/model/Usuario.java src/main/java/aulasenora/repository/UsuarioRepository.java src/main/java/aulasenora/dto/RegistroDTO.java
git commit --author="Veruzka <Veruzkagq@gmail.com>" -m "feat: Ampliación del modelo de Usuario y actualización del repositorio`n`nSe han agregado nuevos campos al modelo de Usuario para soportar un perfil más completo, incluyendo información biográfica, de contacto y académica. Además, se actualizó el repositorio y el DTO de registro para manejar adecuadamente esta nueva información durante el flujo de alta."

git add src/main/java/aulasenora/config/SecurityConfig.java src/main/java/aulasenora/service/UsuarioDetailsService.java
git commit --author="Veruzka <Veruzkagq@gmail.com>" -m "sec: Refactorización de la configuración de seguridad y servicios de autenticación`n`nSe ajustaron las rutas permitidas en SecurityConfig para reflejar los nuevos endpoints y garantizar un acceso seguro. Asimismo, se perfeccionó la lógica de carga de detalles de usuario en UsuarioDetailsService, resolviendo advertencias y optimizando la obtención de roles."

git add src/main/resources/templates/login.html src/main/resources/templates/register.html
git commit --author="Veruzka <Veruzkagq@gmail.com>" -m "ui: Migración a reCAPTCHA v3 y mejora estética en vistas de autenticación`n`nSe eliminó el widget intrusivo de reCAPTCHA v2 en favor de la versión 3 (invisible), integrando la lógica de validación basada en puntaje. También se estandarizó el diseño de las pantallas de login y registro para alinearse con el nuevo tema claro de alto contraste."

git add src/main/java/aulasenora/controller/WebController.java
git commit --author="Veruzka <Veruzkagq@gmail.com>" -m "refactor: Optimización de rutas y lógica en WebController`n`nSe limpiaron importaciones no utilizadas y se reestructuró el mapeo de vistas generales de la plataforma, preparando el controlador para manejar mejor las sesiones activas y redirigir eficientemente según el rol del usuario autenticado."

git add src/main/resources/templates/student/dashboard.html src/main/java/aulasenora/controller/StudentController.java
git commit --author="Veruzka <Veruzkagq@gmail.com>" -m "feat(student): Actualización del panel de estudiante y su controlador`n`nSe modernizó el dashboard del estudiante integrando componentes visuales dinámicos. A nivel de backend, el StudentController se mejoró para despachar de forma óptima las estadísticas y la información de las tutorías pendientes y finalizadas."

git add src/main/resources/templates/student/profile.html
git commit --author="Veruzka <Veruzkagq@gmail.com>" -m "ui(student): Rediseño completo de la página de perfil del estudiante`n`nSe implementó una nueva interfaz para el perfil del estudiante, exponiendo la información extendida del usuario en tarjetas bien estructuradas y manteniendo una paleta de colores coherente con las directrices de diseño modernas y claras."

git add src/main/resources/templates/student/aula-detail.html
git commit --author="Veruzka <Veruzkagq@gmail.com>" -m "ui(student): Refinamiento visual del detalle de tutoría para estudiantes`n`nMejora en la presentación de la información detallada de cada sesión de aula. Se resolvieron problemas de contraste y legibilidad, asegurando que los materiales y estados de la sesión sean fácilmente identificables por el alumno."

git add src/main/resources/templates/fragments/app-layout.html
git commit --author="Veruzka <Veruzkagq@gmail.com>" -m "ui: Implementación de fondos animados y corrección de Tailwind en layout principal`n`nSe resolvieron las advertencias de Tailwind CSS (@apply) ajustando la configuración y se introdujeron animaciones de ondas de fondo y efectos 'glassmorphism' para elevar significativamente la calidad estética de toda la plataforma."

git add src/main/java/aulasenora/AulasenoraApplication.java
git commit --author="Veruzka <Veruzkagq@gmail.com>" -m "chore: Mantenimiento y optimización del punto de entrada de la aplicación`n`nSe eliminaron referencias obsoletas y se afinó la configuración inicial de Spring Boot en AulasenoraApplication, preparando el terreno para una mejor gestión del ciclo de vida de la aplicación y la resolución de conflictos de puertos."

git add src/main/resources/templates/admin/dashboard.html
git commit --author="Veruzka <Veruzkagq@gmail.com>" -m "ui(admin): Estandarización visual del panel de administración`n`nSe actualizó la interfaz del administrador para que comparta el mismo lenguaje de diseño claro y de alto contraste aplicado en las vistas de voluntarios y estudiantes, asegurando consistencia a lo largo de toda la plataforma."

# Oscar Commits
git add src/main/java/aulasenora/controller/VolunteerController.java src/main/resources/templates/volunteer/dashboard.html
git commit --author="Oscar <Ocadavid0818@hotmail.com>" -m "feat(volunteer): Renovación del panel de voluntarios y lógica de negocio asociada`n`nSe actualizó el VolunteerController para proporcionar datos precisos sobre las horas impartidas y solicitudes pendientes. En el frontend, el dashboard de voluntarios ahora exhibe gráficos y métricas más legibles, además de corregir problemas de scroll."

git add src/main/resources/templates/volunteer/aula-detail.html
git commit --author="Oscar <Ocadavid0818@hotmail.com>" -m "ui(volunteer): Mejora en la interfaz de detalle de aula para voluntarios`n`nSe optimizó la vista donde los voluntarios interactúan con el detalle de una clase. La actualización incluye modales más responsivos y alertas con mejor contraste para la gestión de asistencia y materiales."

git add src/main/java/aulasenora/controller/AulaController.java src/main/java/aulasenora/service/AulaService.java
git commit --author="Oscar <Ocadavid0818@hotmail.com>" -m "feat(aula): Robustecimiento en la gestión de aulas y servicios core`n`nSe solventaron advertencias de seguridad de nulos (null-safety) y se reestructuró la lógica de asignación y creación de aulas. Estas mejoras previenen fallos en tiempo de ejecución al procesar datos incompletos."

git add src/main/java/aulasenora/service/HorarioAulaService.java src/main/java/aulasenora/service/MensajeAulaService.java
git commit --author="Oscar <Ocadavid0818@hotmail.com>" -m "refactor(aula): Correcciones de calidad en servicios de horarios y mensajería`n`nLimpieza profunda de dependencias circulares e imports innecesarios. Se añadieron validaciones defensivas en las operaciones de base de datos para garantizar la integridad al agendar sesiones y procesar notificaciones."

git add src/main/java/aulasenora/model/HorarioDisponible.java src/main/java/aulasenora/repository/HorarioAulaRepository.java
git commit --author="Oscar <Ocadavid0818@hotmail.com>" -m "feat(horario): Actualización de modelos y repositorios para validación de tiempo`n`nSe introdujo soporte para validar de forma nativa que las horas de fin sean posteriores a las horas de inicio. El repositorio fue ampliado para ofrecer consultas más eficientes en la búsqueda de colisiones de horarios."

git add src/main/resources/templates/fragments/sidebar.html
git commit --author="Oscar <Ocadavid0818@hotmail.com>" -m "ui: Modernización del menú lateral de navegación`n`nEl sidebar ha sido rediseñado para incorporar micro-animaciones, mejores efectos de 'hover' y adaptarse perfectamente a la nueva estética luminosa de la aplicación. Se ajustaron íconos y espaciados para maximizar la usabilidad."

git add src/main/java/aulasenora/controller/CalendarController.java
git commit --author="Oscar <Ocadavid0818@hotmail.com>" -m "feat(calendar): Creación del nuevo controlador unificado de calendario`n`nSe agregó un nuevo controlador encargado exclusivamente de la gestión de eventos del calendario, separando responsabilidades y permitiendo a estudiantes y voluntarios interactuar mediante una API limpia e integrada."

git add src/main/resources/templates/student/calendar.html
git commit --author="Oscar <Ocadavid0818@hotmail.com>" -m "feat(student): Implementación de la vista interactiva de calendario para estudiantes`n`nSe añadió la nueva funcionalidad que permite a los estudiantes visualizar los espacios disponibles de tutoría, agendar citas y gestionar sus eventos en una vista de calendario dinámica y responsiva."

git add src/main/resources/templates/volunteer/calendar.html
git commit --author="Oscar <Ocadavid0818@hotmail.com>" -m "feat(volunteer): Desarrollo del calendario de gestión para voluntarios`n`nSe integró una vista especializada de calendario donde los voluntarios pueden administrar sus bloques de disponibilidad, aprobar solicitudes de estudiantes y visualizar sus próximos compromisos de manera intuitiva."

Write-Host "All commits applied successfully."
