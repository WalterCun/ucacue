# UCACUE

Este repositorio contiene dos aplicaciones:
- Backend (Quarkus, Java 21)
- Frontend (Astro, Node.js 18)

## Lenguajes de programación
- Java 21 (backend)
- TypeScript/JavaScript (frontend)

## Frameworks y versiones
- Quarkus: 3.26.1 (según backend/pom.xml)
- Astro: ^5.13.5 (según frontend/package.json)
- Tailwind CSS (plugins @astrojs/tailwind, typography, forms)

## Requisitos previos
- Docker Desktop 4.24+ (para la opción con Docker)
- Node.js 18+ y npm (para desarrollo local del frontend)
- Java 21 y Maven (solo si deseas compilar el backend localmente; con Docker no es necesario)

## Instrucciones de compilación y despliegue

### Opción A: Producción con Docker Compose
1) Compilar imágenes sin caché (opcional):
   - docker compose build --no-cache
2) Levantar servicios en segundo plano:
   - docker compose up -d
3) Acceder a las aplicaciones:
   - Frontend: http://localhost:3000
   - Backend: http://localhost:8080

Variables de entorno útiles:
- PUBLIC_API_URL (frontend) -> URL base del backend usado por el frontend (por defecto http://localhost:8080).

Notas:
- El backend se compila dentro del Dockerfile multi-stage (no necesitas ejecutar mvn package localmente para Docker).

### Opción B: Desarrollo local (sin Docker)
Desde la raíz del repositorio en Windows puedes usar el script de ayuda:
- ./dev.ps1

El script ejecuta en paralelo:
- Backend: mvnw -f backend/pom.xml quarkus:dev
- Frontend: npm run dev (directorio de trabajo: frontend)

Para detener ambos, presiona Ctrl+C (el script terminará ambos procesos).

### Compilación manual (avanzado)
- Backend (local):
  1) Ir a backend
  2) Compilar: ./mvnw package (Windows: mvnw.cmd package)
  3) Ejecutar en dev: ./mvnw quarkus:dev
- Frontend (local):
  1) Ir a frontend
  2) Instalar deps: npm ci
  3) Build: npm run build
  4) Previsualizar: npm run preview -- --host 0.0.0.0

## Infraestructura de contenedores
- Backend: se construye desde backend/src/main/docker/Dockerfile.jvm (base runtime: UBI9 OpenJDK 21)
- Frontend: Dockerfile basado en node:18-alpine (build y runtime separados)

## Enlaces rápidos
- Documentación Docker de este repo: README_DOCKER.md
