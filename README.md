
Microservicio **reactivo** (Spring **WebFlux** + **Java 17**) que expone:

## Endpoints

* **POST** `/alumnos` → Graba un alumno validando campos y **unicidad del `id`**.

  * Si el `id` ya existe → **409 Conflict** (mensaje claro).
  * Si todo OK → **204 No Content** *(respuesta vacía, como pide el reto)*.
    
* **GET** `/alumnos?estado=ACTIVO` → Lista alumnos con **estado ACTIVO**.

## Persistencia y arquitectura

* Persistencia **en memoria** con **Mongo DB**.
* Arquitectura propuesta: capas de la aplicación (controller, service y repository)

## 🚀 Stack

* **Java 17**, **Spring Boot 3.5.4
* **Spring WebFlux** *(reactivo)*
* **Spring Data MongoDB**
* **Lombox**
* Build: **Gradle (Maven)**
