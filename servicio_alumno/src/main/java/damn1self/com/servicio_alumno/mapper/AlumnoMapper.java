package damn1self.com.servicio_alumno.mapper;

import damn1self.com.servicio_alumno.dto.AlumnoRequest;
import damn1self.com.servicio_alumno.dto.AlumnoResponse;
import damn1self.com.servicio_alumno.model.Alumno;
import org.mapstruct.*;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface AlumnoMapper {

    @Mapping(target = "mongoId", ignore = true)     // no viene en request
    @Mapping(target = "alumnoId", source = "id")     // DTO.id -> entity.alumnoId
    Alumno toEntity(AlumnoRequest req);

    @Mapping(target = "id", source = "alumnoId")     // entity.alumnoId -> DTO.id
    AlumnoResponse toResponse(Alumno alumno);

}