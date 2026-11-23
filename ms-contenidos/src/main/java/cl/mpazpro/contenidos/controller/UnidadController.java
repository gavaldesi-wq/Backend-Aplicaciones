package cl.mpazpro.contenidos.controller;

import cl.mpazpro.contenidos.dto.UnidadDTO;
import cl.mpazpro.contenidos.dto.PaginacionDTO;
import cl.mpazpro.contenidos.model.EstadoContenido;
import cl.mpazpro.contenidos.service.UnidadService;
import cl.mpazpro.contenidos.dto.TransicionEstadoDTO;
import org.springframework.http.HttpHeaders;

import lombok.RequiredArgsConstructor;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api/v1/utp/unidades")
@RequiredArgsConstructor
public class UnidadController {

    private final UnidadService unidadService;

        @GetMapping
        public ResponseEntity<PaginacionDTO<UnidadDTO>> getAll(
                @RequestParam(required = false) Long asignaturaId,
                @RequestParam(required = false) EstadoContenido estado,
                @RequestParam(defaultValue = "0") int pagina,
                @RequestParam(defaultValue = "20") int tamano) {

                List<UnidadDTO> unidades =
                        unidadService.getAll(asignaturaId);

                if (estado != null) {
                unidades = unidades.stream()
                        .filter(u -> u.getEstado() == estado)
                        .toList();
                }

                return ResponseEntity.ok(
                        PaginacionDTO.fromList(unidades, pagina, tamano)
                );
        }

        @GetMapping("/{id}")
        public ResponseEntity<UnidadDTO> getById(
                @PathVariable Long id) {

                return ResponseEntity.ok(
                        unidadService.getById(id)
                );
        }


        @PostMapping("/{id}/transiciones")
        public ResponseEntity<UnidadDTO> cambiarEstado(
                @PathVariable Long id,
                @Valid @RequestBody TransicionEstadoDTO dto,
                @RequestHeader(HttpHeaders.AUTHORIZATION)
                String authorization
        ) {
        return ResponseEntity.ok(
                unidadService.cambiarEstado(
                        id,
                        dto.getEstadoNuevo(),
                        authorization
                )
        );
        }


    @PostMapping
    public ResponseEntity<UnidadDTO> save(
            @Valid @RequestBody UnidadDTO dto) {

        UnidadDTO creada = unidadService.save(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(creada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UnidadDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody UnidadDTO dto) {

        return ResponseEntity.ok(
                unidadService.update(id, dto)
        );
    }
}
