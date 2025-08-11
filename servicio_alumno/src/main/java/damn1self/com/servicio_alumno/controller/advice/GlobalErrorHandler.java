package damn1self.com.servicio_alumno.controller.advice;


import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalErrorHandler {

    // Crea un ProblemDetail con metadata común
    private ProblemDetail base(HttpStatus status, String title, String detail, ServerHttpRequest req) {
        ProblemDetail pd = ProblemDetail.forStatus(status);
        pd.setTitle(title);
        if (detail != null && !detail.isBlank()) {
            pd.setDetail(detail);
        }

        pd.setInstance(URI.create(req.getPath().value()));
        pd.setType(URI.create("about:blank"));

        pd.setProperty("timestamp", OffsetDateTime.now().toString());
        pd.setProperty("error", status.getReasonPhrase());
        pd.setProperty("requestId", req.getId());
        return pd;
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ProblemDetail> duplicate(DuplicateKeyException ex, ServerHttpRequest request) {
        ProblemDetail body = base(
                HttpStatus.CONFLICT,
                "Registro duplicado",
                ex.getMessage() != null ? ex.getMessage() : "El id ya existe xd",
                request
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }


}
