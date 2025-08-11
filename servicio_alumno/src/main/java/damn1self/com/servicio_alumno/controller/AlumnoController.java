package damn1self.com.servicio_alumno.controller;

import damn1self.com.servicio_alumno.dto.AlumnoRequest;
import damn1self.com.servicio_alumno.dto.AlumnoResponse;
import damn1self.com.servicio_alumno.mapper.AlumnoMapper;
import damn1self.com.servicio_alumno.model.Alumno;
import damn1self.com.servicio_alumno.model.Estado;
import damn1self.com.servicio_alumno.service.AlumnoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
    @Operation(summary = "Crear alumno", description = "Devuelve 204 si se crea, 409 si el id ya existe")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Creado"),
            @ApiResponse(responseCode = "409", description = "Duplicado"),
            @ApiResponse(responseCode = "400", description = "Validación")
    })
    @PostMapping( consumes=MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Void>> crear(@Valid @RequestBody AlumnoRequest alumno) {
        return service.crear(mapper.toEntity(alumno))
                .thenReturn(ResponseEntity.noContent().<Void>build());
//              .onErrorResume(DuplicateKeyException.class,
//                      e -> Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).<Void>build()));
    }

    // ---------------------------------------------------------
    // GET /alumnos?estado=ACTIVO|INACTIVO (si no se envía => todos)
    // ---------------------------------------------------------
    @Operation(
            summary = "Listar alumnos",
            description = "Si 'estado' se omite => lista todos. Si se envía => filtra por ACTIVO/INACTIVO."
    )
    @GetMapping
    public ResponseEntity<Flux<AlumnoResponse>> listar(
            @Parameter(description = "Filtra por estado", schema = @Schema(allowableValues = {"ACTIVO", "INACTIVO"}))
            @RequestParam(value = "estado", required = false) String estado) {

        Flux<Alumno> flux;
        if (estado == null || estado.isBlank()) {
            flux = service.listarAlumnos();
        } else {
            final Estado e;
            try {
                e = Estado.valueOf(estado.trim().toUpperCase());
            } catch (IllegalArgumentException ex) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "estado debe ser ACTIVO o INACTIVO");
            }
            flux = service.listarByEstado(e);
        }
        return ResponseEntity.ok(flux.map(mapper::toResponse));

        //return ResponseEntity.ok(service.listarActivos().map(mapper::toResponse));
    }
}