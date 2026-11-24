package cl.mpazpro.actividades.controller;

import java.util.List;

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

import cl.mpazpro.actividades.dto.PaginacionDTO;
import cl.mpazpro.actividades.dto.PruebaFinalDTO;
import cl.mpazpro.actividades.dto.TransicionEstadoDTO;
import cl.mpazpro.actividades.model.EstadoContenido;
import cl.mpazpro.actividades.service.PruebaFinalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/utp/pruebas-finales")
@RequiredArgsConstructor
public class PruebaFinalController {

    private final PruebaFinalService pruebaFinalService;

    @GetMapping
    public ResponseEntity<PaginacionDTO<PruebaFinalDTO>> getAll(
            @RequestParam(required = false) Long objetivoAprendizajeId,
            @RequestParam(required = false) EstadoContenido estado,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "20") int tamano) {

        List<PruebaFinalDTO> pruebas =
                pruebaFinalService.getAll(objetivoAprendizajeId);

        if (estado != null) {
            pruebas = pruebas.stream()
                    .filter(prueba -> prueba.getEstado() == estado)
                    .toList();
        }

        return ResponseEntity.ok(
                PaginacionDTO.fromList(pruebas, pagina, tamano)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<PruebaFinalDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                pruebaFinalService.getById(id)
        );
    }
    
    @PostMapping
    public ResponseEntity<PruebaFinalDTO> save(
            @Valid @RequestBody PruebaFinalDTO dto) {

        PruebaFinalDTO creada = pruebaFinalService.save(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(creada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PruebaFinalDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody PruebaFinalDTO dto) {

        return ResponseEntity.ok(
                pruebaFinalService.update(id, dto)
        );
    }
    @PostMapping("/{id}/transiciones")
    public ResponseEntity<PruebaFinalDTO> cambiarEstado(
            @PathVariable Long id,
            @Valid @RequestBody TransicionEstadoDTO dto) {
    
        return ResponseEntity.ok(
                pruebaFinalService.cambiarEstado(id, dto.getEstadoNuevo())
        );
    }
}