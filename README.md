# Servicio Alumno — WebFlux + R2DBC + H2

<details open>
  <summary><strong>Descripción</strong></summary>

Microservicio **reactivo** (Spring **WebFlux** + **Java 17**).
</details>

<details>
  <summary><strong>Arquitectura CAPAS</strong></summary>

- Controller (HTTP)  
- Service (lógica)  
- Repository (R2DBC)  
- Soporte: DTO, mapper, validation, advice (errores)
</details>

<details>
  <summary><strong>Endpoints</strong></summary>

- **POST** `/alumnos` → Graba un alumno validando campos y unicidad (nombre+apellido).
  - **409 Conflict** si (nombre, apellido) ya existe.
  - **204 No Content** (respuesta vacía) o **201 Created** con `Location` (según tu implementación).
  - **400 Bad Request** por validaciones del DTO (manejado por el error handler global).

- **GET** `/alumnos?estado=ACTIVO` → Lista alumnos filtrando por estado.
</details>

<details>
  <summary><strong>Persistencia y arquitectura</strong></summary>

- Persistencia en **H2** (memoria/archivo) con **Spring Data R2DBC**.  
- Arquitectura por **capas** (controller, service, repository).
</details>

<details>
  <summary><strong>Stack</strong></summary>

- **Java 17**, **Spring Boot 3.5.4**  
- **Spring WebFlux** (reactivo)  
- **Spring Data R2DBC**, **H2**  
- **Lombok**, **MapStruct**  
- **springdoc-openapi (Swagger UI)**  
- Build: **Maven**
</details>
