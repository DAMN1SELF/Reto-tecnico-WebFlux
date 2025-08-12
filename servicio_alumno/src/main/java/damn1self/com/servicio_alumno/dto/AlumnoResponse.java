package damn1self.com.servicio_alumno.dto;

import damn1self.com.servicio_alumno.model.Estado;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Respuesta para solicitudes de alumno")
public record AlumnoResponse(
        @Schema(example = "796190a2-d8cc-49d5-8c37-79295f93ecab") UUID alumno_id,
        @Schema(example = "Bernabe Daniel") String alumno_nombres,
        @Schema(example = "Inche Ticlavilca") String alumno_apellidos,
        @Schema(example = "ACTIVO") Estado alumno_estado,
        @Schema(example = "31") Integer alumno_edad
) {}