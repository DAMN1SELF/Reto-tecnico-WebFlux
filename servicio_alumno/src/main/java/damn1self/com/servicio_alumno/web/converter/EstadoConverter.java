package damn1self.com.servicio_alumno.web.converter;

import damn1self.com.servicio_alumno.model.Estado;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebInputException;

@Component
public class EstadoConverter implements Converter<String, Estado> {
    @Override
    public Estado convert(String s) {
        if (s == null || s.isBlank()) return null;
        try {
            return Estado.valueOf(s.trim().toUpperCase());
        } catch (Exception e) {
            // Deja el mensaje claro aquí:
            throw new IllegalArgumentException("estado debe ser ACTIVO o INACTIVO");
        }
    }
}
