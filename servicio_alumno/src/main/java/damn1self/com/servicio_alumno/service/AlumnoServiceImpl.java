package damn1self.com.servicio_alumno.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import damn1self.com.servicio_alumno.model.Alumno;
import damn1self.com.servicio_alumno.model.Estado;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import damn1self.com.servicio_alumno.repository.AlumnoRepository;

@Service
@RequiredArgsConstructor
public class AlumnoServiceImpl implements AlumnoService {

    private final AlumnoRepository repo;

//    public AlumnoServiceImpl(AlumnoRepository repo) {
//        this.repo = repo;
//    }
    @Override
    public Mono<Void> crear(Alumno alumno) {
        return repo.existsByAlumnoId(alumno.getAlumnoId())
                .flatMap(exists -> exists
                        ? Mono.<Void>error(new DuplicateKeyException("id ya existe -> SERVICE"))
                        : repo.save(alumno).then()
                );
    }

    @Override
    public Flux<Alumno> listarByEstado(Estado estado) {
        return repo.findByEstado(estado);
    }

    @Override
    public Flux<Alumno> listarAlumnos() {
        return repo.findAll();
    }
}
