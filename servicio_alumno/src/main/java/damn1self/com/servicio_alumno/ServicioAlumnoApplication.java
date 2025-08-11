package damn1self.com.servicio_alumno;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "damn1self.com.servicio_alumno")
public class ServicioAlumnoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServicioAlumnoApplication.class, args);
    }

}
