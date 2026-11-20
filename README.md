# Sistema de registro de exámenes

Aplicación de escritorio JavaFX para crear, consultar, listar, editar y eliminar
exámenes de sangre. Los datos se guardan en MariaDB mediante JDBC.

## Estructura y diseño

El proyecto Maven se encuentra en `Examen2/` y está organizado en capas:

- **Dominio:** `Examen`, `TipoSangre` y `FactorRh`, además de los patrones
  Factory Method, Strategy y Adapter.
- **Aplicación:** puertos de entrada para los casos de uso CRUD, servicios y
  el puerto de salida `RepositoryExamen`.
- **Infraestructura:** controlador JavaFX/FXML y conexión JDBC a MariaDB.
- **Persistencia:** el adaptador traduce el contrato del repositorio a las
  operaciones del servicio externo de persistencia.

El dominio contiene las reglas de validación de los datos. Los servicios
coordinan los casos de uso y dependen del puerto de salida, no de MariaDB. La
configuración de infraestructura se realiza en el punto de entrada JavaFX.

## Requisitos

- JDK 11.
- Apache Maven 3.8.9 o posterior.
- XAMPP con MariaDB iniciado (o una instancia compatible).

## Configuración de MariaDB en XAMPP

1. Inicia los módulos **Apache** (opcional, si utilizarás phpMyAdmin) y
   **MySQL** desde el panel de XAMPP.
2. Crea la base de datos y la tabla ejecutando el script
   [`schema.sql`](Examen2/src/main/resources/db/schema.sql) en phpMyAdmin o en
   el cliente de MariaDB. El script crea la base `examenes` y la tabla `examen`.
3. La configuración predeterminada de la aplicación es:
   - URL: `jdbc:mariadb://localhost:3306/examenes`
   - Usuario: `root`
   - Contraseña: vacía

Si tu instancia usa valores distintos, configura estas variables de entorno
antes de iniciar la aplicación:

```powershell
$env:EXAMENES_DB_URL = "jdbc:mariadb://localhost:3306/examenes"
$env:EXAMENES_DB_USER = "root"
$env:EXAMENES_DB_PASSWORD = "tu contraseña"
```

La aplicación también reconoce esas variables cuando se configura desde el
entorno de ejecución del IDE.

## Compilar y ejecutar

Desde la raíz del repositorio:

```powershell
cd Examen2
mvn clean javafx:run
```

## Pruebas

Las pruebas unitarias no requieren MariaDB:

```powershell
cd Examen2
mvn test
```

La suite unitaria se limita a Factory Method, Strategy y Adapter. La suite de
integración prueba esos patrones junto con la persistencia CRUD en MariaDB.

Las pruebas de integración ejercitan Factory Method, Strategy, Adapter y las
operaciones CRUD contra una instancia real de MariaDB. Primero crea la base con
el script indicado y verifica que el servicio esté iniciado; luego ejecuta:

```powershell
mvn "-Dexamenes.integration=true" test
```

Para configurar la conexión de integración se pueden definir las variables de
entorno `EXAMENES_DB_URL`, `EXAMENES_DB_USER` y `EXAMENES_DB_PASSWORD`, o pasar
propiedades de sistema:

```powershell
mvn "-Dexamenes.integration=true" test `
  "-Dexamenes.db.url=jdbc:mariadb://localhost:3306/examenes" `
  "-Dexamenes.db.user=root" `
  "-Dexamenes.db.password=tu contraseña"
```

La prueba de integración genera códigos únicos y elimina los registros que
crea al finalizar. No se habilita durante `mvn test` normal para que la suite
unitaria pueda ejecutarse sin base de datos.

## Uso de herramientas de apoyo

- **GitHub Copilot:** se utilizó como apoyo para implementar el código del
  proyecto.
- **ChatGPT:** se utilizó para comprender conceptos y recibir recomendaciones
  relacionadas con el examen.
