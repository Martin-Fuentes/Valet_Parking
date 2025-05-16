# 🚗 Valet Parking App

Aplicación móvil desarrollada en Android Studio para la gestión de vehículos y conductores en un servicio de valet parking. Permite registrar datos de autos y conductores de forma rápida y segura, utilizando almacenamiento local con SQLite.

## 🧩 Funcionalidades

- Registro de conductores con nombre y teléfono.
- Registro de autos con placa, marca, modelo y relación con un conductor.
- Visualización de listas de conductores y vehículos registrados.
- Almacenamiento de datos local utilizando SQLite sin bibliotecas ORM (sin Room).

## 🛠 Herramientas y Tecnologías Utilizadas

| Herramienta         | Descripción                                                             |
|---------------------|-------------------------------------------------------------------------|
| **Android Studio**  | Entorno de desarrollo integrado (IDE) para la creación de apps Android. |
| **Kotlin**            | Lenguaje principal de programación de la aplicación.                    |
| **SQLite**          | Motor de base de datos local utilizado para persistencia de datos.      |
| **XML**             | Utilizado para el diseño de interfaces gráficas (layouts).              |

## 🗃 Estructura de la Base de Datos

- **Tabla `conductor`**
  - `Cedula` (INTEGER, PRIMARY KEY)
  - `nombre` (TEXT)
  - `telefono` (TEXT)

- **Tabla `vehiculo`**
  - `placa` (TEXT, PRIMARY KEY)
  - `marca` (TEXT)
  - `modelo` (TEXT)
  - `color` (TEXT)
  - `tipo` (TEXT)
  
- **Tabla `parking`**
  - `placa` (TEXT, FOREIGH KEY)
  - `cedula` (TEXT, FOREIGH KEY)
  - `hora_salida` (DATETIME)
  - `hora_entrada` (DATETIME)
  - `costo_pagar` (DECIMAL(10,2))

## 🔧 Próximas Funcionalidades

- Registro de hora de entrada y salida de vehículos.
- Filtro de búsqueda por placa o conductor.
- Generación de reportes o historial de autos atendidos.
- Notificaciones al usuario sobre registros exitosos.

## 📌 Requisitos

- Android Studio instalado (recomendado: última versión estable).
- Dispositivo físico o emulador con Android 5.0 o superior.

## 👨‍💻 Autor

**MARTIN FUENTES**  
*Desarrollador de Software*

---

