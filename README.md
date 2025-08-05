🏦 Sistema Bancario en Java

Este proyecto consiste en un sistema bancario desarrollado en Java. Permite a los usuarios iniciar sesión, gestionar cuentas, depositar, retirar, consultar saldos, transferir dinero y ver sus movimientos realizados.

---

Datos de prueba

Empleado:

email: elcuervo@gmail.com

pass: 1234

Cliente:

Id: 3

email: prueba@prueba.com

pass: 1234

---

📚 Tecnologías y herramientas utilizadas

Java 17

GUI: Java Swing (JFrame, JPanel, JButton, etc.)

JDBC para conexión a base de datos

MySQL como base de datos relacional

DAO Pattern para separar lógica de acceso a datos

Patrón Singleton para la conexión única a la base

JUnit 5 para pruebas unitarios

Laragon como entorno de desarrollo

---

🧐 Patrones y principios aplicados

📂 DAO (Data Access Object)

Separación de responsabilidades entre la lógica de negocio y el acceso a la base de datos.

Implementado en UserDao, CuentaDao, y sus clases Impl.

🧵 Singleton

Asegura una única instancia de conexión a la base de datos(DataBaseConexion)

🧪 Testing

Pruebas básicas con JUnit para: login, consulta de saldos, creación de cuentas, obtención de cuentas por usuario.

---

🔧 Funcionalidades

✅ Registro automático de usuario si no existe

✅ Validación de email y contraseña

✅ Inicio de sesión

✅ Crear cuenta bancaria con saldo inicial

✅ Ver saldos de todas las cuentas del usuario

✅ Depositar saldo

✅ Retirar saldo

✅ Transferir dinero entre cuentas del mismo usuario

✅ Mensajes de error claros ante fallos

✅ Visualizar transacciones realizadas (movimientos)

✅ Gestionar cuentas de clientes (habilitar, bloquear, cerrar)

---
Base de datos

Scripts y estructura en la carpeta db

Base MySQL configurada para Laragon
