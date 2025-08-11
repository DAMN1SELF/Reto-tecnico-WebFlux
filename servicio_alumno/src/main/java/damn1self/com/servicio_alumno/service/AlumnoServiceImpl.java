package damn1self.com.servicio_alumno.service;

import org.springframework.dao.DuplicateKeyException;
import damn1self.com.servicio_alumno.model.Alumno;
import damn1self.com.servicio_alumno.model.Estado;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import damn1self.com.servicio_alumno.repository.AlumnoRepository;

@Service
public class AlumnoServiceImpl implements AlumnoService {

    private final AlumnoRepository repo;
    public AlumnoServiceImpl(AlumnoRepository repo) {
        this.repo = repo;
    }
    @Override
    public Mono<Void> crear(Alumno alumno) {
        return repo.existsByIdAlumno(alumno.getIdAlumno())
                .flatMap(exists -> exists
                        ? Mono.<Void>error(new DuplicateKeyException("id ya existe"))
                        : repo.save(alumno).then()
                );
    }

    @Override
    public Flux<Alumno> listarActivos() {
        return repo.findByEstado(Estado.ACTIVO);
    }
}
