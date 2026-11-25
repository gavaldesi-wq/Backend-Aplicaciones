package cl.mpazpro.progreso.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.mpazpro.progreso.dto.ResultadosEstudianteDTO;
import cl.mpazpro.progreso.service.DocenteResultadoService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/docente/estudiantes")
@RequiredArgsConstructor
public class DocenteResultadoController {

    private final DocenteResultadoService docenteResultadoService;

    @GetMapping("/{id}/resultados")
    public ResponseEntity<ResultadosEstudianteDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(
                docenteResultadoService.obtener(id)
        );
    }
}