package cl.mpazpro.actividades.controller;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Error uniforme del contrato (seccion 7.8): codigo, mensaje, detalle, marcaTiempo, ruta.
 * Nunca expone trazas al frontend.
 */
@RestControllerAdvice
public class ManejadorErrores {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> estado(
            ResponseStatusException ex, HttpServletRequest solicitud) {
        HttpStatusCode codigo = ex.getStatusCode();
        String mensaje = ex.getReason() != null ? ex.getReason() : "Solicitud no procesada";
        return respuesta(codigo, codigoDominio(codigo), mensaje, null, solicitud);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> validacion(
            MethodArgumentNotValidException ex, HttpServletRequest solicitud) {
        String detalle = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return respuesta(HttpStatus.BAD_REQUEST, "DATOS_INVALIDOS",
                "Hay campos obligatorios vacios o invalidos", detalle, solicitud);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Map<String, Object>> malFormada(
            Exception ex, HttpServletRequest solicitud) {
        return respuesta(HttpStatus.BAD_REQUEST, "DATOS_INVALIDOS",
                "La solicitud esta mal formada", null, solicitud);
    }

    private static String codigoDominio(HttpStatusCode codigo) {
        return switch (codigo.value()) {
            case 400 -> "DATOS_INVALIDOS";
            case 401, 403 -> "ACCESO_DENEGADO";
            case 404 -> "RECURSO_NO_ENCONTRADO";
            case 409 -> "ESTADO_INVALIDO";
            case 503 -> "SERVICIO_NO_DISPONIBLE";
            default -> "ERROR";
        };
    }

    private static ResponseEntity<Map<String, Object>> respuesta(
            HttpStatusCode estado, String codigo, String mensaje, String detalle,
            HttpServletRequest solicitud) {
        Map<String, Object> cuerpo = new LinkedHashMap<>();
        cuerpo.put("codigo", codigo);
        cuerpo.put("mensaje", mensaje);
        cuerpo.put("detalle", detalle);
        cuerpo.put("marcaTiempo", Instant.now().toString());
        cuerpo.put("ruta", solicitud.getRequestURI());
        return ResponseEntity.status(estado).body(cuerpo);
    }
}
