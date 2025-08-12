package damn1self.com.servicio_alumno.controller.advice;


import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.*;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebInputException;

import java.net.URI;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalErrorHandler {

    private ProblemDetail pd(HttpStatus st, String title, String detail, ServerHttpRequest req) {
        ProblemDetail p = ProblemDetail.forStatus(st);
        p.setTitle(title);
        p.setDetail(detail);
        p.setInstance(URI.create(req.getPath().value()));
        return p;
    }

    // 400 - Validación DTO (@Valid) -> usa SIEMPRE este nombre
    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<ProblemDetail> handleBindException(WebExchangeBindException ex, ServerHttpRequest req) {
        String msg = ex.getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return ResponseEntity.badRequest().body(pd(HttpStatus.BAD_REQUEST, "Validación fallida", msg, req));
    }

    // 400 - Si viene como ServerWebInputException, reenvía a handleBindException cuando aplica
    @ExceptionHandler(ServerWebInputException.class)
    public ResponseEntity<ProblemDetail> handleServerWebInput(ServerWebInputException ex, ServerHttpRequest req) {
        if (ex.getCause() instanceof WebExchangeBindException webEx) {
            return handleBindException(webEx, req); // <-- nombre consistente
        }
        return ResponseEntity.badRequest()
                .body(pd(HttpStatus.BAD_REQUEST, "Error de formato", "JSON inválido o tipos incorrectos.", req));
    }

    // 409 - Duplicado
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ProblemDetail> handleDuplicate(DuplicateKeyException ex, ServerHttpRequest req) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(pd(HttpStatus.CONFLICT, "Registro duplicado", "El identificador ya existe.", req));
    }
}

