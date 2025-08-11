package damn1self.com.servicio_alumno.service;

import damn1self.com.servicio_alumno.model.Alumno;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface  AlumnoService {
    Mono<Void> crear(Alumno alumno);
    Flux<Alumno> listarActivos();
}
