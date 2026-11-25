package cl.mpazpro.progreso.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.mpazpro.progreso.dto.AsignacionDTO;
import cl.mpazpro.progreso.dto.PaginacionDTO;
import cl.mpazpro.progreso.service.AsignacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
@RestController
@RequestMapping("/api/v1/docente/asignaciones")
@RequiredArgsConstructor
public class AsignacionController {

    private final AsignacionService asignacionService;

    @GetMapping
    public ResponseEntity<PaginacionDTO<AsignacionDTO>> getAll(
            @RequestParam Long cursoId,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "20") int tamano) {
    
        return ResponseEntity.ok(
                PaginacionDTO.fromList(
                        asignacionService.getAll(cursoId),
                        pagina,
                        tamano
                )
        );
    }
    
    @PostMapping
    public ResponseEntity<AsignacionDTO> save(
            @Valid @RequestBody AsignacionDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(asignacionService.save(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AsignacionDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody AsignacionDTO dto) {
    
        return ResponseEntity.ok(
                asignacionService.update(id, dto)
        );
    }
}