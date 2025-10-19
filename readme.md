# TP1 - FProlog

Este proyecto es una implementación de un motor de inferencia lógica inspirado en Prolog, desarrollado en Scala 3 como parte del Trabajo Práctico N°1 de la materia Paradigmas de Programación.

El motor es capaz de interpretar una base de conocimientos compuesta por hechos y reglas, y responder consultas mediante unificación, sustitución y backtracking


# Tecnologia 
Lenguaje: Scala 3.
Herramienta de Construcción: sbt.

# Requisitos
- JDK 8 o superior.
- sbt (Scala Build Tool)

# Guía de Instalación

1.  **Instale JDK**:
    Descargue e instale el JDK desde un proveedor oficial como [Adoptium (OpenJDK)](https://adoptium.net/). Siga las instrucciones para su sistema operativo (Windows, macOS o Linux).

2. **Instale sbt**:
    Siga la guía oficial de instalación de sbt desde la [página de Scala](https://www.scala-sbt.org/download.html).

    **Instalar sbt  en terminal especifico de Linux:**

    Añadir la clave del repositorio de sbt:
``` bash
    sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 2EE0EA64E40A89B84B2DF73499E82A75642AC823
```

    Añadir el repositorio de sbt a lista de fuentes:
``` bash
    echo "deb https://repo.scala-sbt.org/scalasbt/debian all main" | sudo tee /etc/apt/sources.list.d/sbt.list
```

    instalacion:
``` bash
    sudo apt install sbt
```

# Compilación y Uso

# 1. Compilar el Proyecto
Para compilar el código fuente, ejecute el siguiente comando en la raíz del proyecto:
```bash
sbt compile
```

# 2. Ejecutar las Pruebas

El proyecto incluye un script `test_runner.sh` para una fácil ejecución de todos los casos de prueba

**Paso A: Dar permisos de ejecucion**
```bash
chmod +x test_runner.sh
```

**Paso B: Ejecutar el script**
``` bash
./test_runner.sh
```
# 3. Ejecutar Consultas Manualmente

Se ejecuta el programa pasándole como argumentos la ruta a la base de conocimientos y la ruta al archivo de consultas.

```bash
sbt "run ruta al archivo.pl ruta a consultas.txt "
```
# Ejemplos de Uso
Ejecutar las consultas del programa1:
```bash
sbt "run ejemplos/programas/programa1.pl ejemplos/inputs/programa1.txt"
```
Ejecutar las consultas del programa2:
``` bash
sbt "run ejemplos/programas/programa2.pl ejemplos/inputs/programa2.txt"
```
Ejecutar de forma interactiva (una sola consulta):
``` bash
sbt "run ejemplos/programas/programa3.pl -"
```

