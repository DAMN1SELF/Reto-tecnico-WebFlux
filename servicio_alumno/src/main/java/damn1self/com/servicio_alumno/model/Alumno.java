package damn1self.com.servicio_alumno.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import jakarta.validation.constraints.*;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@Table(name = "tmAlumno")
@NoArgsConstructor
@AllArgsConstructor
public class Alumno {

    @NotNull(message = "id es obligatorio")
    @Id
    private UUID Id;

    @NotBlank(message = "nombre es obligatorio")
    @Size(min = 2, max = 160, message = "nombre debe tener entre 2 y 160 caracteres")
    private String nombre;

    @NotBlank(message = "apellido es obligatorio")
    @Size(min = 2, max = 200, message = "apellido debe tener entre 2 y 180 caracteres")
    private String apellido;

    @NotNull(message = "estado es obligatorio")
    private Estado estado;

    @NotNull(message = "edad es obligatoria")
    @Min(value = 16, message = "edad mínima 16")
    @Max(value = 120, message = "edad máxima 120")
    private Integer edad;
}
