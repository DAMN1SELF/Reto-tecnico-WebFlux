package damn1self.com.servicio_alumno.dto;
import damn1self.com.servicio_alumno.model.Estado;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Solicitud para crear/actualizar alumno")
public record AlumnoRequest(
        @Schema(example = "1") @NotNull @Positive Long id,                 // <- id de negocio
        @Schema(example = "Bernabe Daniel") @NotBlank @Size(min = 2, max = 120) String nombre,
        @Schema(example = "Inche Ticlavilca") @NotBlank @Size(min = 2, max = 120) String apellido,
        @Schema(example = "ACTIVO") @NotNull Estado estado,
        @Schema(example = "31") @NotNull @Min(18) @Max(120) Integer edad
) {}
