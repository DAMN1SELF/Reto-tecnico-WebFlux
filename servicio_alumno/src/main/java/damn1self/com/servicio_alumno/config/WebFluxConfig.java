package damn1self.com.servicio_alumno.config;

import damn1self.com.servicio_alumno.web.converter.EstadoConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
public class WebFluxConfig implements WebFluxConfigurer {
    private final EstadoConverter estadoConverter;
    public WebFluxConfig(EstadoConverter estadoConverter) { this.estadoConverter = estadoConverter; }
    @Override public void addFormatters(FormatterRegistry registry) { registry.addConverter(estadoConverter); }
}