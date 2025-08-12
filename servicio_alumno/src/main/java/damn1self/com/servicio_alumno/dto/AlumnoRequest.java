package damn1self.com.servicio_alumno.dto;
import damn1self.com.servicio_alumno.model.Estado;
import damn1self.com.servicio_alumno.validation.ValueOfEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Solicitud para crear/actualizar alumno")
public record AlumnoRequest(

        @Schema(example = "101")
        @NotNull(message = "id es obligatorio")
        @Positive(message = "id debe ser positivo")
        Long alumno_id,                // <- id de negocio

        @Schema(example = "Bernabe Daniel")
        @NotBlank(message = "nombre es obligatorio")
        @Size(min = 2, max = 120, message = "nombre debe tener entre 2 y 120 caracteres")
        @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$", message = "nombre solo acepta letras y espacios")
        String alumno_nombres,

        @Schema(example = "Inche Ticlavilca")
        @NotBlank(message = "apellido es obligatorio")
        @Size(min = 2, max = 200, message = "apellido debe tener entre 2 y 200 caracteres")
        @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$", message = "apellido solo acepta letras y espacios")
        String alumnos_apellidos,

        @Schema(example = "ACTIVO", allowableValues = {"ACTIVO","INACTIVO"})
        @NotBlank(message = "estado es obligatorio")
        //@Pattern(regexp = "(?i)ACTIVO|INACTIVO", message = "estado debe ser ACTIVO o INACTIVO")
        @ValueOfEnum(enumClass = Estado.class, ignoreCase = true)
        String alumno_estado,

        @Schema(example = "31")
        @NotNull(message = "edad es obligatoria")
        @Min(value = 16, message = "edad mínima 16")
        @Max(value = 120, message = "edad máxima 120")
        Integer alumno_edad
) { }
