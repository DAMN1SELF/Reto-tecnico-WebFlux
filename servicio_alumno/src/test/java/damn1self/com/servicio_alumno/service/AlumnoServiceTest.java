package damn1self.com.servicio_alumno.service;

import damn1self.com.servicio_alumno.model.Alumno;
import damn1self.com.servicio_alumno.model.Estado;
import damn1self.com.servicio_alumno.repository.AlumnoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlumnoServiceTest {

    @Mock AlumnoRepository alumnoRepository;
    @InjectMocks AlumnoServiceImpl alumnoService;

    Alumno entity;

    @BeforeEach
    void setup() {
        entity = new Alumno(
                null,
                "DAMN",
                "INCHE",
                Estado.ACTIVO,
                31
        );
    }

    @Test
    @DisplayName("crear(): guarda y completa (Mono<Void>)")
    void crear_ok() {
        when(alumnoRepository.existsByNombreIgnoreCaseAndApellidoIgnoreCase(anyString(), anyString()))
                .thenAnswer(inv -> {
                    String nombreArg = inv.getArgument(0);
                    String apellidoArg = inv.getArgument(1);
                    System.out.println("[TEST] existsByNombreIgnoreCaseAndApellidoIgnoreCase llamado con: " +
                            nombreArg + " / " + apellidoArg);
                    return Mono.just(false);
                });

        when(alumnoRepository.save(any(Alumno.class)))
                .thenAnswer(inv -> {
                    Alumno al = inv.getArgument(0);
                    System.out.println("[TEST] save llamado con: " + al);
                    return Mono.just(al);
                });

        StepVerifier.create(alumnoService.crear(entity))
                .expectNextCount(1)
                .verifyComplete();

        verify(alumnoRepository).existsByNombreIgnoreCaseAndApellidoIgnoreCase("DAMN", "INCHE");
        verify(alumnoRepository).save(entity);
        verifyNoMoreInteractions(alumnoRepository);
    }

    @Test
    @DisplayName("crear(): duplicado ")
    void registro_duplicado() {
        // 1) Indica que NO existe (para que llegue a save)
        when(alumnoRepository.existsByNombreIgnoreCaseAndApellidoIgnoreCase(anyString(), anyString()))
                .thenReturn(Mono.just(false));
        // 2) Simula error de la BD en save
        when(alumnoRepository.save(any(Alumno.class)))
                .thenReturn(Mono.error(new DuplicateKeyException("duplicate")));

        StepVerifier.create(alumnoService.crear(entity))
                .expectError(DuplicateKeyException.class)
                .verify();

        verify(alumnoRepository).existsByNombreIgnoreCaseAndApellidoIgnoreCase("DAMN","INCHE");
        verify(alumnoRepository).save(entity);
        verifyNoMoreInteractions(alumnoRepository);
    }

    @Test
    @DisplayName("listarByEstado(): delega al repository")
    void listarPorEstado_OK() {
        when(alumnoRepository.findByEstado(Estado.ACTIVO)).thenReturn(Flux.just(entity));

        StepVerifier.create(alumnoService.listarByEstado(Estado.ACTIVO))
                .expectNext(entity)
                .verifyComplete();

        verify(alumnoRepository).findByEstado(Estado.ACTIVO);
        verifyNoMoreInteractions(alumnoRepository);
    }
}
