
package cl.mpazpro.contenidos.controller;

import cl.mpazpro.contenidos.dto.ObjetivoAprendizajeDTO;
import cl.mpazpro.contenidos.dto.PaginacionDTO;
import cl.mpazpro.contenidos.service.ObjetivoAprendizajeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import cl.mpazpro.contenidos.dto.TransicionEstadoDTO;
import org.springframework.http.HttpHeaders;

import java.util.List;

@RestController
@RequestMapping("/api/v1/utp/objetivos")
@RequiredArgsConstructor
public class ObjetivoAprendizajeController {

    private final ObjetivoAprendizajeService objetivoService;

    @GetMapping
    public ResponseEntity<PaginacionDTO<ObjetivoAprendizajeDTO>> getAll(
            @RequestParam(required = false) Long unidadId,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "20") int tamano) {

        List<ObjetivoAprendizajeDTO> objetivos =
                objetivoService.getAll(unidadId);

        return ResponseEntity.ok(
                PaginacionDTO.fromList(objetivos, pagina, tamano)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ObjetivoAprendizajeDTO> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                objetivoService.getById(id)
        );
    }

    @PostMapping
    public ResponseEntity<ObjetivoAprendizajeDTO> save(
            @Valid @RequestBody ObjetivoAprendizajeDTO dto) {

        ObjetivoAprendizajeDTO creado =
                objetivoService.save(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ObjetivoAprendizajeDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody ObjetivoAprendizajeDTO dto) {

        return ResponseEntity.ok(
                objetivoService.update(id, dto)
        );
    }


        @PostMapping("/{id}/transiciones")
        public ResponseEntity<ObjetivoAprendizajeDTO> cambiarEstado(
                @PathVariable Long id,
                @Valid @RequestBody TransicionEstadoDTO dto,
                @RequestHeader(HttpHeaders.AUTHORIZATION)
                String authorization
        ) {
        return ResponseEntity.ok(
                objetivoService.cambiarEstado(
                        id,
                        dto.getEstadoNuevo(),
                        authorization
                )
        );
        }

}
