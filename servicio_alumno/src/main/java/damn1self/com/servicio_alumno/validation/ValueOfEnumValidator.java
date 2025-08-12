package damn1self.com.servicio_alumno.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.*;
import java.util.stream.Collectors;

public class ValueOfEnumValidator implements ConstraintValidator<ValueOfEnum, String> {
    private Set<String> allowed;
    private boolean ignoreCase;

    @Override
    public void initialize(ValueOfEnum anno) {
        ignoreCase = anno.ignoreCase();
        allowed = Arrays.stream(anno.enumClass().getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext ctx) {
        if (value == null || value.isBlank()) return false; // deja que @NotBlank dé su mensaje
        boolean ok = ignoreCase
                ? allowed.stream().anyMatch(v -> v.equalsIgnoreCase(value))
                : allowed.contains(value);
        if (!ok) {
            ctx.disableDefaultConstraintViolation();
            ctx.buildConstraintViolationWithTemplate(
                    "estados válidos -> [" + String.join(", ", allowed) + "]"
            ).addConstraintViolation();
        }
        return ok;
    }
}