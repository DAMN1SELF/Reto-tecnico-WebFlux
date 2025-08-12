package damn1self.com.servicio_alumno.repository;

import damn1self.com.servicio_alumno.model.Alumno;
import damn1self.com.servicio_alumno.model.Estado;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface AlumnoRepository extends ReactiveCrudRepository<Alumno, UUID> {

    Flux<Alumno> findByEstado(Estado estado);
    Mono<Boolean> existsByNombreIgnoreCaseAndApellidoIgnoreCase(String nombre, String apellido);

}
