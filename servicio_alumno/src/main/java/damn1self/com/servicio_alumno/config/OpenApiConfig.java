package damn1self.com.servicio_alumno.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .info(new Info()
                        .title("Servicio Alumno API")
                        .version("v1.0")
                        .description("API reactiva para gestión de alumnos")
                        .contact(new Contact()
                                .name("Linkedin-BERNABE DANIEL INCHE TICLAVILCA ")
                                .url("https://www.linkedin.com/in/danielinche/"))
                        .license(new License()
                                .name("MIT")
                                .url("https://opensource.org/licenses/MIT")))
                .externalDocs(new ExternalDocumentation()
                        .description("Repositorio (GitHub)")
                        .url("https://github.com/DAMN1SELF/Reto-tecnico-WebFlux"))
                .addServersItem(new Server()
                        .url("http://localhost:8091")
                        .description("Local"));
    }
}