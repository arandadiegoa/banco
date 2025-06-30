🏦 Sistema Bancario en Java

Este proyecto consiste en un sistema bancario por consola desarrollado en Java. Permite a los usuarios registrarse, iniciar sesión, gestionar cuentas, depositar, retirar, consultar saldos y transferir dinero.

---

📚 Tecnologías y herramientas utilizadas

Java 17

JDBC para conexión a base de datos

MySQL como base de datos relacional

DAO Pattern para separar lógica de acceso a datos

Patrón Singleton para la conexión única a la base

JUnit 5 para test unitarios

---

🧐 Patrones y principios aplicados

📂 DAO (Data Access Object)

Separación de responsabilidades entre la lógica de negocio y el acceso a la base de datos.

Implementado en UserDao, CuentaDao, y sus clases Impl.

🧵 Singleton

Asegura una única instancia de conexión a la base de datos.

Clase: DataBaseConexion

🧪 Testing

Se aplicaron pruebas básicas con JUnit para testear:

Login

Consulta de saldos

Creación de cuentas

Obtener cuentas por usuario

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
