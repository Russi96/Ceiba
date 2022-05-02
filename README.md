# Instrucciones de Compilación

Instalar JDK 11 se puede descargar del siguiente link = <a href="https://jdk.java.net/java-se-ri/1">JDK 11</a>

Pasos para la instalacion del JDK en Android Studio

1. File
2. Proyect Structure 
3. SDK Location 
4. JDK Location poner la ruta del JDK 11

![Captura de pantalla 2021-12-02 203944](https://user-images.githubusercontent.com/61768939/144530137-9547a8d2-e1fa-42b5-89dc-d0c221a5dae4.png)


Version Gradle 4.1.0

## Arquitectura 🚀
<p>
El proyecto tiene implementado Clean Arquitecture el cual esta conformado por las siguientes capas:

![clean_arquitecture](https://user-images.githubusercontent.com/61768939/141804064-cf90f5e5-dd51-4424-8fb1-58b1a174156f.jpg)

- Capas de Presentacion = Esta capa interactua con la interfaz de usuario. Aqui se concentraran 
tanto la vista (Activities, Fragments, etc) como los ViewModels. En esta capa se implementa los diferentes
patrones de arquitectura MVVM 

- Capas de Casos de Uso = Los casos de uso o interactors, manejan las acciones que el usuario puede
desencadenar, es aquel que detona una accion en la aplicacion, son los que interactuan con los viewModels

- Capa de Dominio = Contiene las reglas de negocio de la aplicacion, en esta se encuentran los modelos, logica
que valida las cosas como contraseñas o correos (Se creo de tipo JAVA Y KOTLIN). En la capa de dominio no se usan
librerias o dependencias.

- Capa de Datos = En esta capa se definen las abstracciones para acceder a las fuentes de datos de la aplicacion.
Se utiliza el patron de repositorio en esta capa (Se creo de tipo Android Library)

  - Repository utiliza dos fuentes una Local Data Source = Base de datos - Archivo y una Remote Data Source = un API o un Sensor

- Capa de Framework = En esta capa implementamos las bibliotecas  externas y definimos el comportamiento de las
interfaces de las fuentes de datos
</p>


## Compativilidad 🔧

<li><strong>Minimum Android SDK</strong>: Glide v4 requires a minimum API level of 26.</li>
<li><strong>Compile Android SDK</strong>: Glide v4 requires you to compile against API 23 or later.</li>
<li>Gradle Kotlin DSL Primer</li>

## Construido por 🛠️
```
Daniel Felipe Alvarado Russi
```
