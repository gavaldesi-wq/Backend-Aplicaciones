package cl.mpazpro.admin.controller;

import cl.mpazpro.admin.dto.MatriculaDTO;
import cl.mpazpro.admin.service.MatriculaService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/utp/matriculas")
@RequiredArgsConstructor
@PreAuthorize("hasRole('UTP')")
public class MatriculaController {

    private final MatriculaService matriculaService;

    @GetMapping
    public ResponseEntity<List<MatriculaDTO>> listarMatriculas(
            @RequestParam(required = false) Long cursoId
    ) {
        if (cursoId != null) {
            return ResponseEntity.ok(
                    matriculaService.listarPorCurso(cursoId)
            );
        }

        return ResponseEntity.ok(
                matriculaService.listarMatriculas()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatriculaDTO> obtenerPorId(
            @PathVariable @Positive Long id
    ) {
        return ResponseEntity.ok(
                matriculaService.obtenerMatriculaPorId(id)
        );
    }

    @PostMapping
    public ResponseEntity<MatriculaDTO> crearMatricula(
            @Valid @RequestBody MatriculaDTO dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(matriculaService.crearMatricula(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MatriculaDTO> actualizarMatricula(
            @PathVariable @Positive Long id,
            @Valid @RequestBody MatriculaDTO dto
    ) {
        return ResponseEntity.ok(
                matriculaService.actualizarMatricula(id, dto)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivarMatricula(
            @PathVariable @Positive Long id
    ) {
        matriculaService.desactivarMatricula(id);
        return ResponseEntity.noContent().build();
    }
}
