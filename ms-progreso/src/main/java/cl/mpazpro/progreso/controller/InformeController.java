package cl.mpazpro.progreso.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.mpazpro.progreso.dto.CrearInformeDTO;
import cl.mpazpro.progreso.dto.InformeDTO;
import cl.mpazpro.progreso.dto.ValidacionInformeDTO;
import cl.mpazpro.progreso.service.InformeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
@RestController
@RequestMapping("/api/v1/docente/informes")
@RequiredArgsConstructor
public class InformeController {

    private final InformeService informeService;

    @PostMapping
    public ResponseEntity<InformeDTO> generar(
            @Valid @RequestBody CrearInformeDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(informeService.generar(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InformeDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(
                informeService.obtener(id)
        );
    }

    @GetMapping(value = "/{id}/descarga", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> descargar(@PathVariable Long id) {
        byte[] pdf = informeService.descargar(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"informe-" + id + ".pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
    
    @PatchMapping("/{id}/validacion")
    public ResponseEntity<InformeDTO> validar(
            @PathVariable Long id,
            @Valid @RequestBody ValidacionInformeDTO dto) {
    
        return ResponseEntity.ok(
                informeService.validar(id, dto)
        );
    }
}