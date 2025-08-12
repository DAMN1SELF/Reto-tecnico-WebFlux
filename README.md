# Servicio Alumno — WebFlux + R2DBC + H2 + CAPAS

<details open>
  <summary><strong>Descripción</strong></summary>

Microservicio **reactivo** (Spring **WebFlux** + **Java 17**) para gestionar alumnos (validación, mapeo DTO ↔ entidad y errores con Problem Details).
</details>

<details open>
  <summary><strong>Endpoints</strong></summary>

- **POST** `/alumnos` → Crea un alumno validando campos y **unicidad (nombre+apellido)**.
  - **204 No Content** (respuesta vacía) — implementación actual.  
  - **201 Created** (alternativa) con header `Location`.  
  - **409 Conflict** si (nombre, apellido) ya existe.  
  - **400 Bad Request** por validaciones del DTO (manejadas globalmente).

- **GET** `/alumnos?estado=ACTIVO` → Lista alumnos filtrando por estado.
</details>

<details open>
  <summary><strong>Persistencia y arquitectura</strong></summary>

- Persistencia en **H2** (memoria/archivo) con **Spring Data R2DBC**.  
- Arquitectura por **capas**:
  - **Controller** (HTTP)
  - **Service** (lógica de aplicación)
  - **Repository** (R2DBC)
  - **Soporte**: DTO, mapper (MapStruct), validation (Bean Validation), advice (errores)
</details>

<details open>
  <summary><strong>Stack</strong></summary>

- **Java 17**, **Spring Boot 3.5.4**  
- **Spring WebFlux** (reactivo)  
- **Spring Data R2DBC**, **H2**  
- **Lombok**, **MapStruct**  
- **springdoc-openapi (Swagger UI)**  
- Build: **Maven**
</details>

<details>
  <summary><strong>Swagger / Run rápido</strong></summary>

- Swagger UI: `http://localhost:8091/swagger-ui.html`  
- Docs JSON: `/v3/api-docs`

```bash
mvn clean package
mvn spring-boot:run
