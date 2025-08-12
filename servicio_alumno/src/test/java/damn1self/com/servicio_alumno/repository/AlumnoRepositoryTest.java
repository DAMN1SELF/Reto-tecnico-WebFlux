package damn1self.com.servicio_alumno.repository;

import damn1self.com.servicio_alumno.model.Alumno;
import damn1self.com.servicio_alumno.model.Estado;
import io.r2dbc.spi.ConnectionFactory;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.UUID;

@DataR2dbcTest
@Import(AlumnoRepositoryTest.R2dbcSchemaTestConfig.class)
class AlumnoRepositoryTest {

    @Autowired AlumnoRepository repository;

    @BeforeEach
    void clean() { StepVerifier.create(repository.deleteAll()).verifyComplete(); }

    private Alumno newAlumno(UUID id, String nombres, String apellidos, Estado estado, int edad) {
        return new Alumno(id, nombres, apellidos, estado, edad);
    }

    @Test
    @DisplayName("crear / buscar: persiste y recupera")
    void crearAlumno() {
        Alumno a = newAlumno(null, "Bernabe Daniel", "Inche Ticlavilca", Estado.ACTIVO, 31);

        StepVerifier.create(
                repository.save(a)
                        .flatMap(saved -> repository.findById(saved.getId())))
                .expectNextMatches
                        (found ->
                                "Bernabe Daniel".equals(found.getNombre()) &&
                                "Inche Ticlavilca".equals(found.getApellido()) &&
                                Estado.ACTIVO == found.getEstado() &&
                                31 == found.getEdad()
                        )
                .verifyComplete();
    }

    @Test
    @DisplayName("filtrar por estado")
    void BuscarPorEstado() {
        Flux<Alumno> seed = repository.saveAll(Flux.just(
                newAlumno(null,"Bernabe", "Inche", Estado.ACTIVO, 31),
                newAlumno(null,"Jose Javier", "Inche Ticlavilca", Estado.ACTIVO, 27),
                newAlumno(null, "Ana", "Mejia Cubas", Estado.INACTIVO, 22)
        ));

        StepVerifier.create(seed.thenMany(repository.findByEstado(Estado.ACTIVO)))
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    @DisplayName("Duplicidad (nombres, apellidos)")
    void validarDuplicidad() {
        // ids distintos, mismos (nombres, apellidos)
        Alumno a1 = newAlumno(null, "Bernabe Daniel", "Inche Ticlavilca", Estado.ACTIVO, 31);
        Alumno a2 = newAlumno(null, "Bernabe Daniel", "Inche Ticlavilca", Estado.INACTIVO, 31);

        StepVerifier.create(repository.save(a1))
                .expectNextCount(1)
                .verifyComplete();

        StepVerifier.create(repository.save(a2))
                .expectErrorSatisfies(ex -> Assertions.assertTrue(
                        ex instanceof org.springframework.dao.DuplicateKeyException,
                        "Esperaba DuplicateKeyException , pero fue: " + ex))
                .verify();
    }

    // Carga schema.sql desde classpath
    static class R2dbcSchemaTestConfig {
        @Bean
        ConnectionFactoryInitializer initializer(ConnectionFactory cf) {
            ConnectionFactoryInitializer i = new ConnectionFactoryInitializer();
            i.setConnectionFactory(cf);
            i.setDatabasePopulator(new ResourceDatabasePopulator(new ClassPathResource("schema.sql")));
            return i;
        }
    }
}
