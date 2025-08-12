package damn1self.com.servicio_alumno.service;

import damn1self.com.servicio_alumno.model.Alumno;
import damn1self.com.servicio_alumno.model.Estado;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface  AlumnoService {
    Mono<Alumno> crear(Alumno alumno);
    Flux<Alumno> listarByEstado(Estado estado);
    Flux<Alumno> listarAlumnos();
}
