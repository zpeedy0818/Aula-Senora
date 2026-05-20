# Walkthrough: Extracción del Servicio de Gestión de Usuarios y Google Login

Se ha extraído con éxito la funcionalidad de usuarios del monolito al nuevo microservicio `user-service`, ubicado ahora dentro del directorio de microservicios. También se ha configurado la persistencia directa a **Supabase** (usando el pooler de conexión IPv4) y la autenticación con **Google OAuth2** y **JWT**.

## Cambios Realizados

1. **Nueva Ubicación del Microservicio:**
   - Ubicado en el módulo [user-service](file:///c:/Users/Ocada/Proyectos/Aula-Senora/microservicios/user-service) bajo la carpeta contenedora `microservicios`.
   - Configurado [pom.xml](file:///c:/Users/Ocada/Proyectos/Aula-Senora/microservicios/user-service/pom.xml) con dependencias para Web, JPA, Security, Google OAuth2 Client, PostgreSQL y JWT.

2. **Base de Datos (Supabase):**
   - Configurada la persistencia en [application.yml](file:///c:/Users/Ocada/Proyectos/Aula-Senora/microservicios/user-service/src/main/resources/application.yml) utilizando la cadena de conexión del pooler IPv4: `jdbc:postgresql://aws-1-us-west-2.pooler.supabase.com:6543/postgres?prepareThreshold=0` con el usuario `postgres.jthbboiwdagdpkindxdm`.
   - Inicializado el esquema en Supabase directamente a través del cliente de base de datos con tablas, índices y disparadores para la auditoría de tiempo (`updated_at`).
   - Se desactivó la inicialización automática por Spring Boot (`spring.sql.init.mode: never`) en [application.yml](file:///c:/Users/Ocada/Proyectos/Aula-Senora/microservicios/user-service/src/main/resources/application.yml) para evitar errores de división de bloques de funciones PL/pgSQL.

3. **Autenticación con Google y JWT:**
   - **JWT:** Implementada la lógica de tokens en [JwtUtils.java](file:///c:/Users/Ocada/Proyectos/Aula-Senora/microservicios/user-service/src/main/java/aulasenora/users/config/JwtUtils.java) y el filtro de interceptación [JwtFilter.java](file:///c:/Users/Ocada/Proyectos/Aula-Senora/microservicios/user-service/src/main/java/aulasenora/users/config/JwtFilter.java) para peticiones stateless.
   - **Google OAuth2:** Creado un cargador de usuarios personalizado [CustomOAuth2UserService.java](file:///c:/Users/Ocada/Proyectos/Aula-Senora/microservicios/user-service/src/main/java/aulasenora/users/service/CustomOAuth2UserService.java) que registra automáticamente en Supabase a los usuarios que ingresan con Google por primera vez.
   - **Success Redirect:** Configurado [OAuth2SuccessHandler.java](file:///c:/Users/Ocada/Proyectos/Aula-Senora/microservicios/user-service/src/main/java/aulasenora/users/config/OAuth2SuccessHandler.java) para generar el JWT tras un login exitoso de Google y redirigir al frontend.
   - **Configuración de Seguridad:** Ensamblado todo en [SecurityConfig.java](file:///c:/Users/Ocada/Proyectos/Aula-Senora/microservicios/user-service/src/main/java/aulasenora/users/config/SecurityConfig.java).

4. **Controlador REST:**
   - Creado [UserController.java](file:///c:/Users/Ocada/Proyectos/Aula-Senora/microservicios/user-service/src/main/java/aulasenora/users/controller/UserController.java) con los endpoints requeridos:
     - `POST /users/registro`: Registro tradicional.
     - `POST /users/login`: Login local con JWT como respuesta.
     - `GET /users/me`: Obtener perfil del usuario autenticado.
     - `PUT /users/me`: Modificar campos de perfil.

## Cómo Probarlo y Validarlo

1. **Ejecutar el microservicio desde la raíz:**
   ```powershell
   .\mvnw.cmd -f microservicios/user-service/pom.xml spring-boot:run
   ```

2. **Probar Registro y Login Local:**
   - **Registro:** Envía un POST a `http://localhost:8081/users/registro` con el body JSON de un `RegistroDTO`.
   - **Login:** Envía un POST a `http://localhost:8081/users/login` con:
     ```json
     {
       "username": "tu_usuario",
       "password": "tu_password"
     }
     ```
     Deberías recibir un JSON con un `"token"` JWT.

3. **Probar Login con Google:**
   - Accede en tu navegador a `http://localhost:8081/oauth2/authorization/google`.
   - Te redirigirá a Google para iniciar sesión. Al autorizar, te redirigirá a `http://localhost:8080/login-success?token=...` con el token generado.
