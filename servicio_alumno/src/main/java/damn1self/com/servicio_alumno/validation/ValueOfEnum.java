package damn1self.com.servicio_alumno.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValueOfEnumValidator.class)
public @interface ValueOfEnum {
    Class<? extends Enum<?>> enumClass();
    boolean ignoreCase() default true;
    String message() default "valor inválido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}