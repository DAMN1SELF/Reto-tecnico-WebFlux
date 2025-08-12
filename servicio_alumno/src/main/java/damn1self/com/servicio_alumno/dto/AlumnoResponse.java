package damn1self.com.servicio_alumno.dto;

import damn1self.com.servicio_alumno.model.Estado;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta para solicitudes de alumno")
public record AlumnoResponse(
        @Schema(example = "101") Long alumno_id,
        @Schema(example = "Bernabe Daniel") String alumno_nombres,
        @Schema(example = "Inche Ticlavilca") String alumno_apellidos,
        @Schema(example = "ACTIVO") Estado alumno_estado,
        @Schema(example = "31") Integer alumno_edad
) {}