package damn1self.com.servicio_alumno.controller;

import damn1self.com.servicio_alumno.dto.AlumnoRequest;
import damn1self.com.servicio_alumno.dto.AlumnoResponse;
import damn1self.com.servicio_alumno.mapper.AlumnoMapper;
import damn1self.com.servicio_alumno.model.Alumno;
import damn1self.com.servicio_alumno.model.Estado;
import damn1self.com.servicio_alumno.service.AlumnoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/alumnos")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AlumnoController {

    private final AlumnoService service;
    private final AlumnoMapper mapper;


    // ---------------------------------------------------------
    // POST /alumnos
    // ---------------------------------------------------------
    @Operation(
            summary = "Crear alumno",
            description = "Crea un alumno. El ID se asigna en la BD."
    )
    @ApiResponses({
            @ApiResponse( responseCode = "204",description = "Creado sin contenido",
                    content = @Content(
                            schema = @Schema(hidden = true))),
//            @ApiResponse(responseCode = "201", description = "Creado",
//                    content = @Content(mediaType="application/json",
//                            schema = @Schema(implementation = AlumnoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validación fallida",
                    content = @Content(mediaType="application/problem+json",
                            schema = @Schema(implementation = org.springframework.http.ProblemDetail.class))),
            @ApiResponse(responseCode = "409", description = "Duplicado (nombre+apellido)",
                    content = @Content(mediaType="application/problem+json",
                            schema = @Schema(implementation = org.springframework.http.ProblemDetail.class)))
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Void>> crear(@Valid @RequestBody AlumnoRequest alumno) {
        return service.crear(mapper.toEntity(alumno))
//                .map(saved -> ResponseEntity.status(HttpStatus.CREATED)
//                        .body(mapper.toResponse(saved)))
                .then(Mono.just(ResponseEntity.noContent().build()));
//              .onErrorResume(DuplicateKeyException.class,
//                      e -> Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).<Void>build()));
    }

    // ---------------------------------------------------------
    // GET /alumnos?estado=ACTIVO|INACTIVO (si no se envía => todos)
    // ---------------------------------------------------------
    @Operation(summary = "Listar alumnos", description = "Permite filtrar por estado")
    @Parameters({
            @Parameter(name = "estado", description = "Estado del alumno",
                    schema = @Schema(implementation = Estado.class))
    })
    @GetMapping()
    public ResponseEntity<Flux<AlumnoResponse>> listar(
            @RequestParam(name = "estado", required = false) Estado estado
    ) {
        var flux = (estado == null) ? service.listarAlumnos() : service.listarByEstado(estado);
        return ResponseEntity.ok(flux.map(mapper::toResponse));
    }
}