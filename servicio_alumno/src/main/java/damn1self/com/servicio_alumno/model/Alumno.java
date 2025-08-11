package damn1self.com.servicio_alumno.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("alumnos")
@NoArgsConstructor
@AllArgsConstructor
public class Alumno {
    private String id;
    private Long idAlumno;
    private String apellido;
    private String nombre;
    private Estado estado;
    private int edad;
}
