# Listado de Requerimientos (Mobile First) - Aula Señora 🎓

Este listado contempla tanto la versión web responsiva como la futura adaptación a una aplicación nativa Android.

## 1. Requerimientos Funcionales

| ID | Requerimiento | Descripción | Prioridad |
|----|---------------|-------------|-----------|
| RF-01 | Autenticación Adaptativa | Inicio de sesión seguro con soporte para autocompletado en navegadores móviles y gestores de contraseñas. | Alta |
| RF-02 | Registro Mobile-Friendly | Formularios optimizados para entrada táctil, con teclados específicos (email, numérico) según el campo. | Alta |
| RF-03 | Navegación por Gestos | La interfaz debe permitir la navegación fluida mediante gestos comunes en dispositivos móviles. | Media |
| RF-04 | Notificaciones Push (Android) | El sistema debe estar preparado para enviar notificaciones sobre actualizaciones de cursos o mensajes (vía FCM para Android). | Media |
| RF-05 | Visualización de Contenidos | Los recursos educativos (PDF, videos, texto) deben adaptarse automáticamente al tamaño de pantalla del dispositivo. | Alta |
| RF-06 | Acceso Offline Básico | Capacidad de visualizar ciertos contenidos previamente cargados sin conexión a internet (Cache/Storage). | Media |

## 2. Requerimientos No Funcionales

| ID | Requerimiento | Descripción |
|----|---------------|-------------|
| RNF-01 | Responsividad Extrema | La interfaz debe ser fluida desde 320px de ancho, asegurando que ningún elemento se desborde. |
| RNF-02 | Consumo de Datos | Optimización de imágenes y scripts para minimizar el consumo de datos móviles. |
| RNF-03 | Rendimiento Android | La aplicación debe mantener una tasa de refresco constante y no agotar excesivamente la batería. |
| RNF-04 | Compatibilidad Android | Preparación para empaquetado mediante WebView o desarrollo nativo en Android Studio (Min API 24). |
| RNF-05 | Seguridad Biométrica | (Futuro) Consideración para integración con Huella Dactilar/Facial en la versión Android. |

## 3. Consideraciones para Android Studio

*   **Arquitectura:** Preparación del backend (Spring Boot) para servir una API RESTful que la App Android consumirá.
*   **Interfaz:** Uso de componentes tipo "Material Design" para que la transición a Android se sienta nativa.
*   **Navegación:** Implementación de un "Bottom Navigation Bar" en móviles en lugar del menú lateral de escritorio.
