package damn1self.com.servicio_alumno.repository;

import damn1self.com.servicio_alumno.model.Alumno;
import damn1self.com.servicio_alumno.model.Estado;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AlumnoRepository extends ReactiveMongoRepository<Alumno, String> {

    Mono<Boolean> existsByAlumnoId(Long alumnoId);
    Flux<Alumno> findByEstado(Estado estado);

}
