# Modelo Relacional - Aula Señora 🎓

Este diagrama representa la estructura de base de datos actual para la gestión de usuarios y voluntarios en la plataforma **Aula Señora**.

![Modelo Relacional](file:///c:/Users/Ocada/Proyectos/AulaSenora/Documentación/Modelo_Relacional.png)

## Descripción de Entidades

1.  **usuarios:** Contiene la información básica de acceso y perfil de todos los miembros (Admin, Estudiante, Voluntario).
2.  **voluntarios:** Extiende la información de los usuarios que tienen el rol de voluntario, añadiendo su institución de origen y sus habilidades específicas.

*Relación: 1 a 1 entre `usuarios.id` y `voluntarios.id`.*
