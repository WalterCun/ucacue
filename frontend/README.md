# UCACUE - Sistema de Inscripciones (Astro + Tailwind)

Aplicación con dos páginas:
- Inscripción: formulario para registrar postulantes en /inscripcion
- Revisión: listado de registros en /revision

Datos se guardan en localStorage (demo). 

Cómo ejecutar:
1) npm install
2) npm run dev
3) Abrir http://localhost:4321

Rutas principales:
- /               Portada con enlaces
- /inscripcion    Formulario de inscripción
- /revision       Login para revisión (usuario: admin, sin contraseña)
- /estad          Página protegida con la revisión de inscripciones
