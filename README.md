# My Pokédex

## Descripción General

My Pokédex es una aplicación de Android nativa que permite a los usuarios explorar el vasto mundo de Pokémon. La aplicación consume datos de la [PokéAPI](https://pokeapi.co/) y presenta la información de una manera visualmente atractiva y fácil de usar. Los usuarios pueden navegar por una lista infinita de Pokémon, buscar a sus favoritos, ver sus detalles y estadísticas, y guardar sus Pokémon preferidos en una lista de favoritos. Tambien soporta el cambio de idioma(español/inglés).

La aplicación está desarrollada siguiendo las mejores prácticas de la actualidad, con un enfoque en la calidad del código, la arquitectura limpia y una experiencia de usuario fluida y receptiva.

## Arquitectura y Tecnologías

El proyecto está construido sobre una base sólida de tecnologías modernas de Android y sigue una arquitectura limpia y bien definida.

### Arquitectura

*   **Clean Architecture:** La aplicación está dividida en tres capas principales (Data, Domain, Presentation) para asegurar un código modular, escalable y fácil de mantener.
*   **MVVM (Model-View-ViewModel):** Se utiliza el patrón MVVM para separar la lógica de la interfaz de usuario del resto de la aplicación, mejorando la legibilidad y la capacidad de prueba.

### Tecnologías Utilizadas

*   **Lenguaje:** [Kotlin](https://kotlinlang.org/)
*   **Interfaz de Usuario:** [Jetpack Compose](https://developer.android.com/jetpack/compose)
*   **Navegación:** [Jetpack Navigation Compose](https://developer.android.com/jetpack/compose/navigation)
*   **Inyección de Dependencias:** [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
*   **Peticiones a la API:** [Retrofit](https://square.github.io/retrofit/)
*   **Base de Datos Local:** [Room](https://developer.android.com/training/data-storage/room)
*   **Persistencia de Datos:** [DataStore](https://developer.android.com/topic/libraries/architecture/datastore)
*   **Carga de Imágenes:** [Coil](https://coil-kt.github.io/coil/)
*   **Asincronía:** [Kotlin Coroutines & Flows](https://kotlinlang.org/docs/coroutines-overview.html)

## Cómo Ejecutar la Aplicación (.apk)

Para instalar y ejecutar la aplicación en un dispositivo Android, sigue estos sencillos pasos:

1.  **Generar el APK:**
    *   Abre el proyecto en Android Studio.
    *   Ve al menú `Build` > `Build Bundle(s) / APK(s)` > `Build APK(s)`.
    *   Una vez que la compilación termine, aparecerá una notificación en la esquina inferior derecha. Haz clic en `locate` para encontrar el archivo APK.

2.  **Instalar en tu Dispositivo:**
    *   Copia el archivo `app-debug.apk` a tu dispositivo Android (puedes hacerlo a través de un cable USB, Bluetooth, o cualquier servicio en la nube).
    *   En tu dispositivo, ve a la aplicación de `Archivos` (o `Files`) y busca el archivo `app-debug.apk`.
    *   Púlsalo para iniciar la instalación. Es posible que necesites habilitar la opción de "Instalar aplicaciones de fuentes desconocidas" en la configuración de seguridad de tu teléfono.
