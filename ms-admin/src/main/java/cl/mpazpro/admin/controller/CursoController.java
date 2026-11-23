package cl.mpazpro.admin.controller;

import cl.mpazpro.admin.dto.CursoDTO;
import cl.mpazpro.admin.service.CursoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/utp/cursos")
@RequiredArgsConstructor
@PreAuthorize("hasRole('UTP')")
public class CursoController {

    private final CursoService cursoService;

    @GetMapping
    public ResponseEntity<List<CursoDTO>> listarCursos() {
        return ResponseEntity.ok(cursoService.listarCursos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CursoDTO> obtenerPorId(
            @PathVariable @Positive Long id
    ) {
        return ResponseEntity.ok(
                cursoService.obtenerCursoPorId(id)
        );
    }

    @PostMapping
    public ResponseEntity<CursoDTO> crearCurso(
            @Valid @RequestBody CursoDTO dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cursoService.crearCurso(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CursoDTO> actualizarCurso(
            @PathVariable @Positive Long id,
            @Valid @RequestBody CursoDTO dto
    ) {
        return ResponseEntity.ok(
                cursoService.actualizarCurso(id, dto)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivarCurso(
            @PathVariable @Positive Long id
    ) {
        cursoService.desactivarCurso(id);
        return ResponseEntity.noContent().build();
    }
}
