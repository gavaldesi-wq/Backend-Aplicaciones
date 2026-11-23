package cl.mpazpro.contenidos.controller;

import cl.mpazpro.contenidos.dto.ContenidoDidacticoDTO;
import cl.mpazpro.contenidos.dto.ObjetivoAprendizajeDTO;
import cl.mpazpro.contenidos.dto.PaginacionDTO;
import cl.mpazpro.contenidos.dto.UnidadDTO;
import cl.mpazpro.contenidos.service.EstudianteService;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/estudiante")
@RequiredArgsConstructor
@Validated
@PreAuthorize("hasRole('ESTUDIANTE')")
public class EstudianteController {

    private final EstudianteService estudianteService;

    @GetMapping("/unidades")
    public ResponseEntity<PaginacionDTO<UnidadDTO>> unidades(
            @RequestParam @Positive Long asignaturaId,
            @RequestParam(defaultValue = "0") @Min(0) int pagina,
            @RequestParam(defaultValue = "20")
            @Min(1) @Max(100) int tamano
    ) {
        return ResponseEntity.ok(
                PaginacionDTO.fromList(
                        estudianteService.listarUnidades(asignaturaId),
                        pagina,
                        tamano
                )
        );
    }

    @GetMapping("/objetivos")
    public ResponseEntity<PaginacionDTO<ObjetivoAprendizajeDTO>> objetivos(
            @RequestParam @Positive Long unidadId,
            @RequestParam(defaultValue = "0") @Min(0) int pagina,
            @RequestParam(defaultValue = "20")
            @Min(1) @Max(100) int tamano
    ) {
        return ResponseEntity.ok(
                PaginacionDTO.fromList(
                        estudianteService.listarObjetivos(unidadId),
                        pagina,
                        tamano
                )
        );
    }

    @GetMapping("/objetivos/{id}/contenidos")
    public ResponseEntity<PaginacionDTO<ContenidoDidacticoDTO>> contenidos(
            @PathVariable @Positive Long id,
            @RequestParam(defaultValue = "0") @Min(0) int pagina,
            @RequestParam(defaultValue = "20")
            @Min(1) @Max(100) int tamano
    ) {
        return ResponseEntity.ok(
                PaginacionDTO.fromList(
                        estudianteService.listarContenidos(id),
                        pagina,
                        tamano
                )
        );
    }
}
