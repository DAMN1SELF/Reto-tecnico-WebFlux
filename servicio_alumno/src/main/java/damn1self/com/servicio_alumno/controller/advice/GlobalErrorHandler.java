package damn1self.com.servicio_alumno.controller.advice;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.*;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebInputException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.FieldError;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class GlobalErrorHandler {

    private ProblemDetail pd(HttpStatus st, String title, String detail, ServerHttpRequest req) {
        ProblemDetail p = ProblemDetail.forStatus(st);
        p.setTitle(title); p.setDetail(detail);
        p.setInstance(URI.create(req.getPath().value()));
        return p;
    }

    // 400 - Validación DTO (@Valid)  -> errors: { campo: [mensajes...] }
    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<ProblemDetail> handleBind(WebExchangeBindException ex, ServerHttpRequest req) {
        Map<String, String> errors = ex.getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        (key, valor) -> key + "; " + valor,                 // concatena si hay múltiples
                        LinkedHashMap::new
                ));

        ProblemDetail body = pd(HttpStatus.BAD_REQUEST, "Validación fallida", "Hay campos inválidos.", req);
        body.setProperty("errors", errors); // <-- JSON estructurado
        return ResponseEntity.badRequest().body(body);
    }

    // 400 - Formato (JSON/tipos). Si la causa real es validación, reusa lo de arriba.
    @ExceptionHandler(ServerWebInputException.class)
    public ResponseEntity<ProblemDetail> handleInput(ServerWebInputException ex, ServerHttpRequest req) {
        if (ex.getCause() instanceof WebExchangeBindException webEx) return handleBind(webEx, req);
        ProblemDetail body = pd(HttpStatus.BAD_REQUEST, "Error de formato", "JSON inválido o tipos incorrectos.", req);
        return ResponseEntity.badRequest().body(body);
    }

    // 409 - Duplicado
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ProblemDetail> handleDup(DuplicateKeyException ex, ServerHttpRequest req) {
        ProblemDetail body = pd(HttpStatus.CONFLICT, "Registro duplicado", "El identificador ya existe.", req);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }
}