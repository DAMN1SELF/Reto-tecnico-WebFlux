package damn1self.com.servicio_alumno.controller;

import damn1self.com.servicio_alumno.dto.AlumnoRequest;
import damn1self.com.servicio_alumno.dto.AlumnoResponse;
import damn1self.com.servicio_alumno.mapper.AlumnoMapper;
import damn1self.com.servicio_alumno.model.Alumno;
import damn1self.com.servicio_alumno.service.AlumnoService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    // POST /alumnos -> 204 si OK, 409 si id duplicado
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Void>> crear(@Valid @RequestBody AlumnoRequest alumno) {
        return service.crear(mapper.toEntity(alumno))
                .thenReturn(ResponseEntity.noContent().<Void>build());
//              .onErrorResume(DuplicateKeyException.class,
//                      e -> Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).<Void>build()));
    }

    // GET /alumnos?estado=ACTIVO -> por requerimiento listamos los ACTIVO
    @GetMapping
    public ResponseEntity<Flux<AlumnoResponse>> listarActivos() {
        return ResponseEntity.ok(service.listarActivos().map(mapper::toResponse));
    }
}