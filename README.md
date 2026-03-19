# Proyecto Aula Señora 🎓

Este es el repositorio oficial del proyecto Aula Señora. Sigue estos pasos exactos para configurar tu entorno de desarrollo.

## Requisitos Previos Indispensables

1.  **Java 25**: El proyecto está configurado para usar Java 25. Asegúrate de tenerlo instalado y configurado en tu `PATH`.
    - Puedes verificarlo con: `java -version`
2.  **Maven**: El proyecto utiliza Maven para la gestión de dependencias.
3.  **PowerShell**: Si estás en Windows, usaremos scripts de PowerShell para facilitar el arranque.

## Configuración del Entorno

Para realizar el desarrollo local, es necesario configurar las variables de entorno para la conexión a la base de datos:

1.  Crea un archivo llamado `.env` en la raíz del proyecto.
2.  Define las siguientes variables con sus respectivos valores (solicítalos al administrador del proyecto):

```env
SPRING_DATASOURCE_URL=
SPRING_DATASOURCE_USERNAME=
SPRING_DATASOURCE_PASSWORD=
RECAPTCHA_SITE_KEY=
RECAPTCHA_SECRET_KEY=
```

## Cómo Ejecutar el Proyecto

Una vez configurado el archivo `.env`, puedes ejecutar la aplicación directamente usando Maven:

```bash
./mvnw spring-boot:run
```

**Nota para usuarios de Windows:** Asegúrate de que las variables de entorno de tu archivo `.env` estén cargadas en tu sesión de PowerShell antes de ejecutar `mvnw`.

## Notas Adicionales
- El archivo `.env` está en el `.gitignore` por seguridad. **Nunca** lo subas al repositorio.
- Si ves errores de "Bad SQL Grammar" al inicio, es normal mientras Hibernate sincroniza las tablas por primera vez.
