package damn1self.com.servicio_alumno.controller;

import damn1self.com.servicio_alumno.controller.advice.GlobalErrorHandler;
import damn1self.com.servicio_alumno.dto.AlumnoRequest;
import damn1self.com.servicio_alumno.dto.AlumnoResponse;
import damn1self.com.servicio_alumno.mapper.AlumnoMapper;
import damn1self.com.servicio_alumno.model.Alumno;
import damn1self.com.servicio_alumno.model.Estado;
import damn1self.com.servicio_alumno.service.AlumnoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = AlumnoController.class)
@Import(GlobalErrorHandler.class)
class AlumnoControllerTest {

    @Autowired WebTestClient webTestClient;
    @MockitoBean AlumnoService alumnoService;
    @MockitoBean AlumnoMapper alumnoMapper;
    

    // -------------------- helpers --------------------
    private static final UUID ID = UUID.fromString("196190a2-d8cc-49d5-8c37-79295f93ecab");

    private Alumno alumno(String nombre, String apellido, Estado estado, int edad) {
        return new Alumno(
                UUID.fromString("196190a2-d8cc-49d5-8c37-79295f93ecab"),
                nombre,
                apellido,
                estado,
                edad
        );
    }

    private AlumnoResponse resp(Alumno a) {
        return new AlumnoResponse(a.getId(), a.getNombre(), a.getApellido(), a.getEstado(), a.getEdad());
    }

    // -------------------- tests ----------------------
    @Test
    @DisplayName("POST /alumnos -> 204 No Content (payload válido)")
    void created_204() {
        String body = """
          {
          "alumno_nombres":"Bernabe Daniel",
           "alumnos_apellidos":"Inche Ticlavilca",
           "alumno_estado":"ACTIVO",
           "alumno_edad":"44"
           }
        """;
        Alumno entity = alumno("Bernabe Daniel", "Inche Ticlavilca", Estado.ACTIVO, 31);

        // Mapper: DTO -> Entity
        when(alumnoMapper.toEntity(any(AlumnoRequest.class))).thenReturn(entity);
        // Service: crear(Alumno) -> Mono<Void>
        when(alumnoService.crear(any(Alumno.class))).thenReturn(Mono.empty());

        webTestClient.post().uri("/alumnos")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @DisplayName("POST /alumnos -> 400 Bad Request (validación)")
    void validacion_400() {

        String invalid = """
          {
          "alumno_nombres":"Damn123",
           "alumnos_apellidos":"@inche",
           "alumno_estado":"DESCONOCIDO",
           "alumno_edad":"44"
           }
        """;

        webTestClient.post().uri("/alumnos")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalid)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody().jsonPath("$.status").isEqualTo(400);
    }

    @Test
    @DisplayName("POST /alumnos -> 409 Conflict (duplicado)")
    void duplicidad_409() {
        String body = """
          {
          "alumno_nombres":"Bernabe Daniel",
           "alumnos_apellidos":"Inche Ticlavilca",
           "alumno_estado":"ACTIVO",
           "alumno_edad":"44"
           }
        """;
        Alumno entity = alumno("Bernabe Daniel", "Inche Ticlavilca", Estado.ACTIVO, 31);

        when(alumnoMapper.toEntity(any(AlumnoRequest.class))).thenReturn(entity);
        when(alumnoService.crear(any(Alumno.class))).thenReturn(Mono.error(new DuplicateKeyException("duplicate")));

        webTestClient.post().uri("/alumnos")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody().jsonPath("$.status").isEqualTo(409);
    }

    @Test
    @DisplayName("GET /alumnos?estado=ACTIVO -> 200 OK y lista de AlumnoResponse")
    void listado_OK() {
        Alumno e1 = alumno("Ana Luisa","Mejia Cubas", Estado.INACTIVO, 22);
        Alumno e2 = alumno("José","Inche", Estado.ACTIVO, 27);

        when(alumnoService.listarByEstado(Estado.ACTIVO)).thenReturn(Flux.just(e2));

        when(alumnoMapper.toResponse(any(Alumno.class))).thenAnswer(inv -> resp(inv.getArgument(0)));

        webTestClient.get().uri(uri -> uri.path("/alumnos").queryParam("estado","ACTIVO").build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.length()").isEqualTo(1)
                .jsonPath("$[0].alumno_nombres").isEqualTo("José")
                .jsonPath("$[0].alumno_apellidos").isEqualTo("Inche")
                .jsonPath("$[0].alumno_edad").isEqualTo("27")
                .jsonPath("$[0].alumno_estado").isEqualTo("ACTIVO");
    }


    @Test
    @DisplayName("GET /alumnos con estado inválido -> 400 Bad Request")
    void listado_invalido_400() {
        webTestClient.get()
                .uri(uri -> uri.path("/alumnos").queryParam("estado","DESCONOCIDO").build())
                .exchange()
                .expectStatus().isBadRequest();
    }
}