        # UCACUE Dockerization

This repository contains two applications:
- backend (Quarkus)
- frontend (Astro)

## Production with Docker Compose

Prerequisitos:
- Docker Desktop 4.24+

Comandos:
- docker compose build --no-cache
- docker compose up -d

Apps:
- Frontend: http://localhost:3000
- Backend: http://localhost:8080

Environment variables you may want to adjust:
- PUBLIC_API_URL (frontend) -> URL base del backend utilizado por el frontend.

## Development (local, sin Docker)

Use the helper PowerShell script from the repo root (Windows):

- ./dev.ps1

It will run:
- Backend: mvnw -f backend/pom.xml quarkus:dev
- Frontend: npm run dev (working directory: frontend)

Press Ctrl+C to stop both; the script will terminate both processes.
