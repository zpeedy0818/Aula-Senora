# Proyecto Aula Señora 🎓

Este es el repositorio oficial del proyecto Aula Señora. Sigue estos pasos exactos para configurar tu entorno de desarrollo.

## Requisitos Previos Indispensables

1.  **Java 25**: El proyecto está configurado para usar Java 25. Asegúrate de tenerlo instalado y configurado en tu `PATH`.
    - Puedes verificarlo con: `java -version`
2.  **Maven**: El proyecto utiliza Maven para la gestión de dependencias.
3.  **PowerShell**: Si estás en Windows, usaremos scripts de PowerShell para facilitar el arranque.

## Configuración del Entorno

Para que la aplicación se conecte a la base de datos de Supabase, necesitas crear un archivo de configuración local:

1.  Crea un archivo llamado `.env` en la raíz del proyecto (donde está este `README.md`).
2.  Copia y pega el siguiente contenido (solicita la contraseña al administrador si no la tienes):

```env
# Configuración de Supabase
SPRING_DATASOURCE_URL=jdbc:postgresql://aws-0-us-west-2.pooler.supabase.com:6543/postgres
SPRING_DATASOURCE_USERNAME=postgres.gdyzhnhijyfgohtlbfqd
SPRING_DATASOURCE_PASSWORD=ParaTOd0s2724
```

## Cómo Ejecutar el Proyecto

Hemos incluido un script que carga automáticamente las variables del `.env` por ti.

### En Windows (PowerShell):
Ejecuta el siguiente comando en la terminal:
```powershell
./run-dev.ps1
```

### En Mac/Linux:
Si no usas PowerShell, asegúrate de exportar las variables de tu `.env` y luego ejecuta:
```bash
./mvnw spring-boot:run
```

## Notas Adicionales
- El archivo `.env` está en el `.gitignore` por seguridad. No intentes subirlo al repositorio.
- Si ves errores de "Bad SQL Grammar" al inicio, es normal mientras Hibernate sincroniza las tablas por primera vez.
