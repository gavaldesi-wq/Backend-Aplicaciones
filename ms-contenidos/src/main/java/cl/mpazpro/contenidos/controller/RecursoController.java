package cl.mpazpro.contenidos.controller;

import cl.mpazpro.contenidos.service.RecursoService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class RecursoController {

    private final RecursoService recursoService;

    @PostMapping(
            value = "/api/v1/utp/recursos",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @PreAuthorize("hasRole('UTP')")
    public ResponseEntity<Map<String, String>> subir(
            @RequestParam("archivo") MultipartFile archivo
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(recursoService.subir(archivo));
    }

    @GetMapping("/api/v1/recursos/{nombre}")
    @PreAuthorize("hasAnyRole('UTP','ESTUDIANTE')")
    public ResponseEntity<Resource> descargar(
            @PathVariable String nombre,
            Authentication authentication
    ) {
        boolean esUtp = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_UTP"));

        Resource recurso = recursoService.obtener(nombre, esUtp);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(
                        recursoService.tipoContenido(nombre)
                ))
                .header("X-Content-Type-Options", "nosniff")
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + nombre + "\"")
                .body(recurso);
    }
}
