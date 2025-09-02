# Modelo de Datos UCACUE

Este documento describe el modelo de datos implementado en la aplicación UCACUE usando Quarkus, Hibernate ORM y H2.

## Arquitectura

La aplicación sigue una arquitectura en capas:

- **Modelo (Model)**: Entidades JPA que representan las tablas de la base de datos
- **Repositorio (Repository)**: Capa de acceso a datos usando Panache
- **Servicio (Service)**: Lógica de negocio y validaciones
- **Controlador (Controller)**: API REST para exponer los servicios
- **DTO (Data Transfer Object)**: Objetos para transferir datos sin exponer la entidad completa

## Entidades

### User

La entidad principal que representa a los usuarios del sistema.

**Campos:**
- `id`: Identificador único (INTEGER AUTOINCREMENT)
- `username`: Nombre de usuario único (VARCHAR(50))
- `email`: Email único (VARCHAR(255))
- `password`: Contraseña hasheada (VARCHAR(255))
- `firstName`: Nombre del usuario (VARCHAR(100))
- `lastName`: Apellido del usuario (VARCHAR(100))
- `isActive`: Estado activo/inactivo (BOOLEAN)
- `createdAt`: Fecha de creación (DATETIME)
- `updatedAt`: Fecha de última actualización (DATETIME)

**Características:**
- Usa `PanacheEntity` para simplificar las operaciones CRUD
- Validaciones JPA Bean Validation
- Triggers automáticos para `updatedAt`
- Métodos estáticos para consultas comunes

## Repositorios

### UserRepository

Implementa `PanacheRepository<User>` y proporciona métodos personalizados:

- `findByUsername(String username)`: Busca por nombre de usuario
- `findByEmail(String email)`: Busca por email
- `findActiveUsers()`: Lista usuarios activos
- `findByNameContaining(String name)`: Búsqueda por nombre
- `isUsernameAvailable(String username)`: Verifica disponibilidad
- `isEmailAvailable(String email)`: Verifica disponibilidad de email

## Servicios

### UserService

Maneja la lógica de negocio para usuarios:

- **Crear usuario**: Valida unicidad de username/email
- **Actualizar usuario**: Permite modificar datos existentes
- **Buscar usuarios**: Por ID, username, email o nombre
- **Gestionar estado**: Activar/desactivar usuarios
- **Eliminar usuarios**: Eliminación física de la base de datos

## DTOs

### UserDTO
Representa un usuario para respuestas de API (sin contraseña).

### CreateUserDTO
DTO para crear/actualizar usuarios con validaciones.

## API REST

### Endpoints disponibles

- `POST /api/users` - Crear usuario
- `GET /api/users` - Listar usuarios activos
- `GET /api/users/{id}` - Obtener usuario por ID
- `GET /api/users/username/{username}` - Obtener por username
- `GET /api/users/search?name={name}` - Buscar por nombre
- `PUT /api/users/{id}` - Actualizar usuario
- `PATCH /api/users/{id}/activate` - Activar usuario
- `PATCH /api/users/{id}/deactivate` - Desactivar usuario
- `DELETE /api/users/{id}` - Eliminar usuario
- `GET /api/users/stats` - Estadísticas de usuarios

## Base de Datos

### Configuración

La aplicación se conecta a H2 usando las siguientes variables de entorno:

- `DB_NAME`: Nombre de la base de datos (default: ucacue)

### Migraciones

Se usa Flyway para gestionar las migraciones de base de datos:

- Las migraciones se ejecutan automáticamente al iniciar la aplicación
- Los archivos de migración están en `src/main/resources/db/migration/`
- Nomenclatura: `V{version}__{description}.sql`

### Script de migración inicial

`V1__Create_users_table.sql` crea:
- Tabla `users` con todos los campos necesarios
- Índices para optimizar consultas
- Trigger SQLite para actualizar automáticamente `updated_at`
- Usuario administrador por defecto

## Uso

### 1. Configurar base de datos

```bash
# H2 se crea automáticamente al ejecutar la aplicación
# No es necesario crear la base de datos manualmente
```

### 2. Configurar variables de entorno

```bash
export DB_NAME=ucacue
```

### 3. Ejecutar la aplicación

```bash
# Modo desarrollo
./mvnw quarkus:dev

# Modo producción
./mvnw package
java -jar target/quarkus-app/quarkus-run.jar
```

### 4. Probar la API

```bash
# Crear un usuario
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123",
    "firstName": "Test",
    "lastName": "User"
  }'

# Listar usuarios
curl http://localhost:8080/api/users

# Obtener usuario por ID
curl http://localhost:8080/api/users/1
```

## Características de Seguridad

- Validación de entrada usando Bean Validation
- DTOs separados para evitar exposición de datos sensibles
- Validación de unicidad de username y email
- Campos de auditoría (createdAt, updatedAt)

## Próximos Pasos

- Implementar autenticación JWT
- Agregar encriptación de contraseñas con BCrypt
- Implementar roles y permisos
- Agregar más entidades (Productos, Categorías, etc.)
- Implementar paginación en las consultas
- Agregar tests unitarios y de integración

## Dependencias

- `quarkus-hibernate-orm-panache`: ORM simplificado
- `quarkus-jdbc-h2`: Driver de H2
- `quarkus-hibernate-validator`: Validaciones
- `quarkus-flyway`: Migraciones de base de datos
- `quarkus-smallrye-health`: Health checks
