# Trabajo Práctico 4 — Aplicación de escritorio con base de datos

Sistema de gestión de cursos, alumnos e inscripciones para el Instituto de Idiomas. Trabajo integrador de **Programación II** (Módulo 5 · Unidades 11 y 12) - **UTN Mar del Plata**.
Prof: Carbonell Cesar Augusto.

---

## Integrantes
- Mariano Yolando.
- Alan Seoane.

---

## Cómo crear la base de datos
1. Asegurarse de tener el servicio de MySQL iniciado en el puerto 3306.
2. Desde la terminal, ubicarse en la raíz del proyecto y ejecutar:
   ```bash
   mysql -u root -p < instituto.sql
   ```
   *(También puede abrirse y ejecutarse el archivo `instituto.sql` desde MySQL Workbench o DBeaver).*

El script `instituto.sql` es idempotente: elimina la base si existía previamente, la crea limpia, define las tablas con sus restricciones e inserta los datos de prueba requeridos.

---

## Cómo configurar y ejecutar el programa

1. **Configuración de credenciales**:
   Copiar el archivo de ejemplo `config.properties.example` y nombrarlo `config.properties`:
   ```bash
   cp config.properties.example config.properties
   ```
   Editar `config.properties` y colocar el usuario y la contraseña local de MySQL.

2. **Compilación**:
   ```bash
   javac -d out -cp lib/mysql-connector-j-9.3.0.jar src/conexion/*.java src/modelo/*.java src/dao/*.java src/vista/*.java src/Main.java
   ```

3. **Ejecución (macOS / Linux)**:
   ```bash
   java -cp out:lib/mysql-connector-j-9.3.0.jar Main
   ```
   *(En Windows usar `;` en lugar de `:`: `java -cp out;lib/mysql-connector-j-9.3.0.jar Main`).*

---

# Preguntas

### **1. ¿Qué pasa si dos personas usan el programa al mismo tiempo y se inscriben al último lugar de un curso? ¿Qué hace su implementación?**

Si dos personas intentan ocupar la última vacante, ambas operaciones no pueden completarse correctamente. porque nuestra implementación se basa en utilizar una transacción para guardar la inscripción y descontar la vacante. Además, la base de datos no permite que las vacantes sean negativas. Si una operación falla, se realiza un rollback y no queda una inscripción a medias.

### **2. Muestren una consulta de su programa escrita con PreparedStatement y expliquen qué le pasaría si la hubieran armado concatenando lo que escribe el usuario.**

En AlumnoDAO utilizamos PreparedStatement para buscar un alumno por su DNI. En la consulta escribimos un "?" como marcador y luego usamos "ps.setString(1, dni)" para colocar el DNI ingresado por el usuario. Si concatenáramos directamente ese dato en la consulta, alguien podría ingresar texto que modifique la instrucción SQL y acceda o altere información que no debería. Por eso usamos PreparedStatement, que trata el DNI como un dato y no como parte del código SQL.

### **3. ¿Dónde decidieron que viva la regla del cupo: en la ventana, en el acceso a datos o en la base? Justifiquen la decisión.**

Decidimos manejar la inscripción y el descuento de vacantes desde el acceso a datos, en InscripcionDAO. Allí utilizamos una transacción para que ambas operaciones se realicen juntas. También agregamos restricciones en la base de datos para evitar que las vacantes sean negativas y mantener la información consistente.
