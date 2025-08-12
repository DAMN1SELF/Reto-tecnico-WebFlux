package damn1self.com.servicio_alumno.mapper;

import damn1self.com.servicio_alumno.dto.AlumnoRequest;
import damn1self.com.servicio_alumno.dto.AlumnoResponse;
import damn1self.com.servicio_alumno.model.Alumno;
import damn1self.com.servicio_alumno.model.Estado;
import org.mapstruct.*;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface AlumnoMapper {

    // AlumnoRequest (record) -> Alumno (entity)
    @Mapping(target = "mongoId", ignore = true)
    @Mapping(target = "alumnoId",  source = "alumno_id")
    @Mapping(target = "nombre",   source = "alumno_nombres")
    @Mapping(target = "apellido", source = "alumnos_apellidos")
    @Mapping(target = "estado", source = "alumno_estado")
    @Mapping(target = "edad",      source = "alumno_edad")
    Alumno toEntity(AlumnoRequest alumnoInput);

    // Alumno (entity, camelCase) -> AlumnoResponse (record, snake_case)
    @Mapping(target = "alumno_id",        source = "alumnoId")
    @Mapping(target = "alumno_nombres",   source = "nombre")
    @Mapping(target = "alumno_apellidos", source = "apellido")
    @Mapping(target = "alumno_estado",    source = "estado")
    @Mapping(target = "alumno_edad",      source = "edad")
    AlumnoResponse toResponse(Alumno alumno);

    // helper
    default Estado toEstado(String value) {
        return (value == null) ? null : Estado.valueOf(value.trim().toUpperCase());
    }
}