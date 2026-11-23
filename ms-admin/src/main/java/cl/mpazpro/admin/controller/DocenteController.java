package cl.mpazpro.admin.controller;

import cl.mpazpro.admin.service.DocenteService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/docente")
@RequiredArgsConstructor
@Validated
@PreAuthorize("hasRole('DOCENTE')")
public class DocenteController {

    private final DocenteService docenteService;

    @GetMapping("/cursos")
    public ResponseEntity<Map<String, Object>> listarCursos(
            Authentication authentication,
            @RequestParam(defaultValue = "0") @Min(0) int pagina,
            @RequestParam(defaultValue = "20")
            @Min(1) @Max(100) int tamano
    ) {
        return ResponseEntity.ok(
                docenteService.listarCursos(
                        authentication.getName(),
                        pagina,
                        tamano
                )
        );
    }

    @GetMapping("/estudiantes")
    public ResponseEntity<Map<String, Object>> listarEstudiantes(
            Authentication authentication,
            @RequestParam @Positive Long cursoId,
            @RequestParam(defaultValue = "0") @Min(0) int pagina,
            @RequestParam(defaultValue = "20")
            @Min(1) @Max(100) int tamano
    ) {
        return ResponseEntity.ok(
                docenteService.listarEstudiantes(
                        authentication.getName(),
                        cursoId,
                        pagina,
                        tamano
                )
        );
    }
}
