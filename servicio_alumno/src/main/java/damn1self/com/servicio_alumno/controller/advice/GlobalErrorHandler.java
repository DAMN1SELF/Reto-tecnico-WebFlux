package damn1self.com.servicio_alumno.controller.advice;


import org.springframework.core.codec.DecodingException;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebInputException;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalErrorHandler {

    // ---------- helper: crea ProblemDetail con metadatos comunes ----------
    private ProblemDetail base(HttpStatus status, String title, String detail, ServerWebExchange ex) {
        var req = ex.getRequest();
        var pd = ProblemDetail.forStatus(status);
        pd.setTitle(title);
        if (detail != null && !detail.isBlank()) pd.setDetail(detail);

        pd.setType(URI.create("about:blank"));
        pd.setInstance(URI.create(req.getPath().value()));

        pd.setProperty("timestamp", OffsetDateTime.now().toString());
        pd.setProperty("error", status.getReasonPhrase());
        pd.setProperty("requestId", req.getId());
        pd.setProperty("method", req.getMethod().name());
        pd.setProperty("route", String.valueOf(ex.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE)));
        pd.setProperty("pathVariables",
                Optional.ofNullable(ex.<Map<String,String>>getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE))
                        .orElseGet(LinkedHashMap::new));
        pd.setProperty("query", req.getQueryParams());
        pd.setProperty("userAgent", req.getHeaders().getFirst(HttpHeaders.USER_AGENT));
        pd.setProperty("clientIp",
                Optional.ofNullable(req.getRemoteAddress()).map(a -> a.getAddress().getHostAddress()).orElse(null));
        return pd;
    }


    // 409 – índice único (id duplicado)
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ProblemDetail> duplicate(DuplicateKeyException ex, ServerWebExchange exchange) {
        var body = base(HttpStatus.CONFLICT, "Registro duplicado",
                ex.getMessage() != null ? ex.getMessage() : "El identificador ya existe, intenta con otro", exchange);
        body.setProperty("exception", ex.getClass().getName());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<ProblemDetail> bodyValidation(WebExchangeBindException ex, ServerWebExchange exchange) {
        var fieldErrors = ex.getFieldErrors().stream()
                .collect(Collectors.toMap(
                        fe -> fe.getField(),
                        fe -> fe.getDefaultMessage(),
                        (a,b) -> a, LinkedHashMap::new
                ));

        var globalErrors = ex.getGlobalErrors().stream()
                .map(e -> e.getDefaultMessage())
                .collect(Collectors.toList());

        var detail = fieldErrors.entrySet().stream()
                .map(e -> e.getKey() + ": " + e.getValue())
                .collect(Collectors.joining("; "));

        if (!globalErrors.isEmpty()) {
            var joined = String.join("; ", globalErrors);
            detail = detail.isBlank() ? joined : (detail + "; " + joined);
        }

        var body = base(HttpStatus.BAD_REQUEST, "Validación fallida", detail, exchange);
        body.setProperty("errors", fieldErrors);
        if (!globalErrors.isEmpty()) body.setProperty("globalErrors", globalErrors);
        return ResponseEntity.badRequest().body(body);
    }


    @ExceptionHandler(org.springframework.core.codec.DecodingException.class)
    public ResponseEntity<ProblemDetail> badJson(DecodingException ex, ServerWebExchange exchange) {
        // Saca el root cause para recuperar mensajes como "estado debe ser ACTIVO o INACTIVO"
        Throwable t = ex, last = null;
        while (t != null && t != last) { last = t; t = t.getCause(); }
        String detail = (last != null && last.getMessage() != null && !last.getMessage().isBlank())
                ? last.getMessage()
                : "Entrada inválida";

        var body = base(HttpStatus.BAD_REQUEST, "Entrada inválida", detail, exchange);
        return ResponseEntity.badRequest().body(body);
    }

}






/**

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
                ex.getMessage() != null ? ex.getMessage() : "El identificador ya existe, intenta con otro",
                request
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }


}
**/