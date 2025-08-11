package damn1self.com.servicio_alumno.dto;

import damn1self.com.servicio_alumno.model.Estado;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta para solicitudes de alumno")
public record AlumnoResponse(
        @Schema(example = "101") Long id,
        @Schema(example = "Bernabe Daniel") String nombre,
        @Schema(example = "Inche Ticlavilca") String apellido,
        @Schema(example = "ACTIVO") Estado estado,
        @Schema(example = "31") Integer edad
) {}