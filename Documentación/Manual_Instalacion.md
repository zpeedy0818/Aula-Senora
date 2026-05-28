# Manual de Instalación - Aula Señora

## 1. Introducción
El presente documento tiene como objetivo describir los pasos detallados para la instalación, configuración y ejecución del software **Aula Señora**. Este manual está dirigido a administradores de sistemas y desarrolladores encargados de desplegar la aplicación en un entorno de pruebas o producción, cumpliendo con los estándares requeridos para su registro ante la Dirección Nacional de Derecho de Autor (DNDA).

## 2. Requisitos del Sistema

### 2.1 Requisitos de Hardware
* **Procesador:** Intel Core i3 o superior, o equivalente en AMD.
* **Memoria RAM:** Mínimo 4 GB (Se recomiendan 8 GB para un mejor rendimiento durante el desarrollo).
* **Almacenamiento:** Mínimo 2 GB de espacio libre en disco para el código fuente y las dependencias.

### 2.2 Requisitos de Software
* **Sistema Operativo:** Windows 10/11, macOS, o cualquier distribución moderna de Linux.
* **Entorno de Ejecución de Java:** Java Development Kit (JDK) versión 25.
* **Gestor de Paquetes:** Apache Maven (integrado en el proyecto mediante Maven Wrapper).
* **Base de Datos:** PostgreSQL (o acceso a una base de datos PostgreSQL alojada en la nube, como Supabase).
* **Navegador Web:** Google Chrome, Mozilla Firefox, Microsoft Edge o Safari (versiones recientes).

## 3. Preparación del Entorno

### 3.1 Instalación de Java (JDK 25)
1. Descargue el instalador de JDK 25 desde el sitio web oficial de Oracle o adopte una versión OpenJDK compatible.
2. Siga las instrucciones del instalador según su sistema operativo.
3. Configure la variable de entorno `JAVA_HOME` apuntando al directorio de instalación del JDK.
4. Verifique la instalación abriendo una terminal (o PowerShell en Windows) y ejecutando:
   ```bash
   java -version
   ```

### 3.2 Clonación del Repositorio
Obtenga el código fuente de la aplicación Aula Señora desde el repositorio oficial:
```bash
git clone <URL_DEL_REPOSITORIO>
cd Aula-Senora
```
*(Sustituya `<URL_DEL_REPOSITORIO>` por la URL correspondiente al repositorio Git).*

## 4. Configuración de la Aplicación

La aplicación requiere la configuración de variables de entorno para su correcto funcionamiento, incluyendo la conexión a la base de datos y servicios externos como Google reCAPTCHA.

1. En la raíz del proyecto (directorio `Aula-Senora`), cree un archivo llamado `.env`.
2. Abra el archivo con un editor de texto y defina las siguientes variables (los valores específicos deben ser proporcionados por el administrador de la plataforma):

```env
# Configuración de Conexión a Base de Datos (PostgreSQL)
SPRING_DATASOURCE_URL=jdbc:postgresql://<HOST>:<PUERTO>/<NOMBRE_BD>?prepareThreshold=0
SPRING_DATASOURCE_USERNAME=<USUARIO_BD>
SPRING_DATASOURCE_PASSWORD=<CONTRASEÑA_BD>

# Configuración de Google reCAPTCHA
RECAPTCHA_SITE_KEY=<SU_SITE_KEY>
RECAPTCHA_SECRET_KEY=<SU_SECRET_KEY>
```
3. Guarde los cambios en el archivo `.env`.

*Nota:* Si está utilizando Windows, asegúrese de que la consola (PowerShell o CMD) cargue correctamente estas variables antes de iniciar el servidor, o inclúyalas directamente en las variables de entorno de su sistema operativo.

## 5. Compilación y Ejecución

El proyecto utiliza Maven Wrapper (`mvnw`), lo que garantiza que se utilice la versión correcta de Maven sin necesidad de instalarlo globalmente en el sistema.

### 5.1 Ejecución en Entorno de Desarrollo (Local)
Para iniciar la aplicación, abra una terminal en la raíz del proyecto y ejecute el siguiente comando:

* **En Windows (PowerShell/CMD):**
  ```cmd
  .\mvnw spring-boot:run
  ```
* **En Linux/macOS:**
  ```bash
  ./mvnw spring-boot:run
  ```

El sistema descargará automáticamente todas las dependencias necesarias de Java, compilará el código y lanzará el servidor embebido de Spring Boot.

### 5.2 Compilación para Producción
Si desea generar un archivo ejecutable `.jar` para un entorno de producción:
```bash
./mvnw clean package
```
Esto generará un archivo `.jar` en la carpeta `target/`. Puede ejecutarlo utilizando:
```bash
java -jar target/aula-senora-X.X.X.jar
```

## 6. Verificación de la Instalación
Una vez que la terminal indique que el servidor ha iniciado correctamente (usualmente mostrando un mensaje como `Started Application in X seconds`), abra su navegador web e ingrese a la siguiente dirección:

```
http://localhost:8080
```

Deberá visualizar la página de inicio o la pantalla de inicio de sesión de **Aula Señora**, lo cual confirmará que la aplicación ha sido instalada y se está ejecutando correctamente.

## 7. Solución de Problemas Frecuentes
* **Error "Bad SQL Grammar" al inicio:** Este error puede aparecer la primera vez que se ejecuta la aplicación debido a que Hibernate (el ORM) está sincronizando y creando las tablas en la base de datos. Es un comportamiento normal inicial y no afecta el sistema.
* **Error de conexión a la base de datos:** Verifique que las credenciales en el archivo `.env` sean correctas y que su equipo tenga acceso de red al servidor de la base de datos (por ejemplo, Supabase o un servidor local).
* **Puerto 8080 ocupado:** Si otro servicio está utilizando el puerto 8080, puede cambiar el puerto de la aplicación añadiendo `SERVER_PORT=8081` a su archivo `.env` o editando el archivo `application.properties`.
