package damn1self.com.servicio_alumno.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import jakarta.validation.constraints.*;

@Data
@Document("alumnos")
@NoArgsConstructor
@AllArgsConstructor
public class Alumno {

    @Id
    private String mongoId;

    @Indexed(unique = true)
    @NotNull(message = "id es obligatorio")
    @Positive(message = "id debe ser positivo")
    @JsonProperty("id")
    private Long alumnoId;

    @NotBlank(message = "nombre es obligatorio")
    @Size(min = 2, max = 120, message = "nombre debe tener entre 2 y 120 caracteres")
    private String nombre;

    @NotBlank(message = "apellido es obligatorio")
    @Size(min = 2, max = 120, message = "apellido debe tener entre 2 y 120 caracteres")
    private String apellido;

    @NotNull(message = "estado es obligatorio")
    private Estado estado;   // ACTIVO | INACTIVO

    @NotNull(message = "edad es obligatoria")
    @Min(value = 18, message = "edad mínima 18")
    @Max(value = 120, message = "edad máxima 120")
    private Integer edad;
}
