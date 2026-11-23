package cl.mpazpro.contenidos.controller;

import cl.mpazpro.contenidos.dto.ContenidoDidacticoDTO;
import cl.mpazpro.contenidos.dto.PaginacionDTO;
import cl.mpazpro.contenidos.service.ContenidoDidacticoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import cl.mpazpro.contenidos.dto.TransicionEstadoDTO;
import org.springframework.http.HttpHeaders;

import java.util.List;

@RestController
@RequestMapping("/api/v1/utp/contenidos")
@RequiredArgsConstructor
public class ContenidoDidacticoController {

    private final ContenidoDidacticoService contenidoService;

    @GetMapping
    public ResponseEntity<PaginacionDTO<ContenidoDidacticoDTO>> getAll(
            @RequestParam(required = false) Long objetivoAprendizajeId,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "20") int tamano) {

        List<ContenidoDidacticoDTO> contenidos =
                contenidoService.getAll(objetivoAprendizajeId);

        return ResponseEntity.ok(
                PaginacionDTO.fromList(contenidos, pagina, tamano)
        );
    }

    @PostMapping
    public ResponseEntity<ContenidoDidacticoDTO> save(
            @Valid @RequestBody ContenidoDidacticoDTO dto) {

        ContenidoDidacticoDTO creado =
                contenidoService.save(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContenidoDidacticoDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody ContenidoDidacticoDTO dto) {

        return ResponseEntity.ok(
                contenidoService.update(id, dto)
        );
    }

        @PostMapping("/{id}/transiciones")
        public ResponseEntity<ContenidoDidacticoDTO> cambiarEstado(
                @PathVariable Long id,
                @Valid @RequestBody TransicionEstadoDTO dto,
                @RequestHeader(HttpHeaders.AUTHORIZATION)
                String authorization
        ) {
        return ResponseEntity.ok(
                contenidoService.cambiarEstado(
                        id,
                        dto.getEstadoNuevo(),
                        authorization
                )
        );
        }
}
