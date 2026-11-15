# Trabajo Práctico 4 — Aplicación de escritorio con base de datos

Sistema de gestión de cursos, alumnos e inscripciones para el Instituto de Idiomas. Trabajo integrador de **Programación II** (Módulo 5 · Unidades 11 y 12) - **UTN Mar del Plata**.

---

## Integrantes
- Mariano Yolando
- *(Nombre y apellido del compañero)*

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
   javac -cp "lib/mysql-connector-j-9.3.0.jar:." src/**/*.java
   ```

3. **Ejecución**:
   ```bash
   java -cp "lib/mysql-connector-j-9.3.0.jar:src:." Main
   ```

---

## Preguntas teóricas

### 1. ¿Qué pasa si dos personas usan el programa al mismo tiempo y se inscriben al último lugar de un curso? ¿Qué hace su implementación?
*(A responder al implementar la transacción)*

### 2. Muestren una consulta de su programa escrita con PreparedStatement y expliquen qué le pasaría si la hubieran armado concatenando lo que escribe el usuario.
*(A responder al implementar los DAOs)*

### 3. ¿Dónde decidieron que viva la regla del cupo: en la ventana, en el acceso a datos o en la base? Justifiquen la decisión.
*(A responder al definir la lógica del cupo)*
