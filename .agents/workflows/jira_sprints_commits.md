---
description: Rutina automática para conectar código con Jira (Commits, Sprints y Épicas)
---

# Reglas de Trabajo para Aula Señora (Jira y Commits)

Cada vez que el USUARIO (Ocada) pida hacer un "commit" o guardar código nuevo, el sistema DEBE:

1. **Revisar las tareas activas:** Preguntarle o averiguar en qué historia de Jira se está trabajando (Ej. `DEV-11`, `DEV-12`, etc.).
2. **Conexión Automática:** Hacer el commit automáticamente a GitHub (usando la terminal) incluyendo siempre las claves o identificadores de las historias al inicio del mensaje para que se autoconecten con los tickets de Jira.
   * *Ejemplo estricto:* `git commit -m "DEV-11 DEV-12: Implementación de la vista de registro final"`
3. **Gestión Ágil Constante:** Si el objetivo es empezar un nuevo requerimiento, el agente debe proponer primero la creación de las historias en Jira e invitarlo a agruparlas en el siguiente Sprint activo para asegurar el cumplimiento con el orden metodológico de su proyecto universitario.
   * Se debe recordar siempre rellenar Story Points, Épicas y Releases como se hizo para el Sprint 1.
