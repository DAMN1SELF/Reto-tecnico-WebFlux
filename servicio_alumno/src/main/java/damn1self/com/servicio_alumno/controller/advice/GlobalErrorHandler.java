package damn1self.com.servicio_alumno.controller.advice;

import org.springframework.core.codec.DecodingException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebInputException;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalErrorHandler {
    private ProblemDetail base(HttpStatus status, String title, String detail, ServerWebExchange ex) {
        var req = ex.getRequest();

        ProblemDetail pd = ProblemDetail.forStatus(status);
        pd.setTitle(title);
        if (detail != null && !detail.isBlank()) {
            pd.setDetail(detail);
        }

        // Siempre enviar String -> URI
        String path = req.getPath().pathWithinApplication().value();
        pd.setInstance(URI.create(path));
        pd.setType(URI.create("about:blank"));

        pd.setProperty("timestamp", OffsetDateTime.now().toString());
        pd.setProperty("error", status.getReasonPhrase());
        pd.setProperty("requestId", req.getId());
        pd.setProperty("method", req.getMethod() != null ? req.getMethod().name() : null);

        // NUNCA pasar PathPattern a URI.create; guardarlo como String
        Object routeAttr = ex.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        pd.setProperty("route", routeAttr != null ? routeAttr.toString() : null);

        Map<String, String> pathVars =
                ex.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        pd.setProperty("pathVariables", pathVars != null ? pathVars : Map.of());

        // Serializable (no MultiValueMap)
        pd.setProperty("query", req.getQueryParams().toSingleValueMap());
        pd.setProperty("userAgent", req.getHeaders().getFirst(HttpHeaders.USER_AGENT));
        pd.setProperty("clientIp",
                Optional.ofNullable(req.getRemoteAddress())
                        .map(a -> a.getAddress().getHostAddress())
                        .orElse(null));

        return pd;
    }

    // 409 – índice único (id duplicado)
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ProblemDetail> duplicate(DuplicateKeyException ex, ServerWebExchange exchange) {
        var body = base(
                HttpStatus.CONFLICT,
                "Registro duplicado",
                Optional.ofNullable(ex.getMessage()).orElse("El identificador ya existe, intenta con otro"),
                exchange
        );
        body.setProperty("exception", ex.getClass().getName());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    // 400 – Bean Validation (@Valid)
    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<ProblemDetail> bodyValidation(WebExchangeBindException ex, ServerWebExchange exchange) {
        var fieldErrors = ex.getFieldErrors().stream()
                .collect(Collectors.toMap(
                        fe -> fe.getField(),
                        fe -> fe.getDefaultMessage(),
                        (a, b) -> a,
                        LinkedHashMap::new
                ));

        var globalErrors = ex.getGlobalErrors().stream()
                .map(e -> e.getDefaultMessage())
                .filter(Objects::nonNull)
                .toList();

        String detail = fieldErrors.entrySet().stream()
                .map(e -> e.getKey() + ": " + e.getValue())
                .collect(Collectors.joining("; "));

        if (!globalErrors.isEmpty()) {
            String joined = String.join("; ", globalErrors);
            detail = detail.isBlank() ? joined : detail + "; " + joined;
        }

        var body = base(HttpStatus.BAD_REQUEST, "Validación fallida", detail, exchange);
        body.setProperty("errors", fieldErrors);
        if (!globalErrors.isEmpty()) body.setProperty("globalErrors", globalErrors);

        return ResponseEntity.badRequest().body(body);
    }

    // 400 – Body JSON inválido / enum inválido
    @ExceptionHandler(DecodingException.class)
    public ResponseEntity<ProblemDetail> badJson(DecodingException ex, ServerWebExchange exchange) {
        String detail = unwrapMessage(ex, "Entrada inválida");
        var body = base(HttpStatus.BAD_REQUEST, "Entrada inválida", detail, exchange);
        return ResponseEntity.badRequest().body(body);
    }

//    // 400 – Query param inválido (converters, enums, etc.)
//    @ExceptionHandler(ServerWebInputException.class)
//    public ResponseEntity<ProblemDetail> badInput(ServerWebInputException ex, ServerWebExchange exchange) {
//        String reason = ex.getReason();
//        if (reason == null || reason.isBlank() || "Type mismatch.".equalsIgnoreCase(reason.trim())) {
//            reason = unwrapMessage(ex, "Entrada inválida");
//        }
//        var body = base(HttpStatus.BAD_REQUEST, "Entrada inválida", reason, exchange);
//        return ResponseEntity.badRequest().body(body);
//    }
//
    private String unwrapMessage(Throwable ex, String defaultMsg) {
        Throwable t = ex, last = null;
        while (t != null && t != last) { last = t; t = t.getCause(); }
        return (last != null && last.getMessage() != null && !last.getMessage().isBlank())
                ? last.getMessage()
                : defaultMsg;
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