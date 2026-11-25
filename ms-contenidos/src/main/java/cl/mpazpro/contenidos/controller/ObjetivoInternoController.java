package cl.mpazpro.contenidos.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.mpazpro.contenidos.dto.ObjetivoElegibleDTO;
import cl.mpazpro.contenidos.service.ObjetivoAprendizajeService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/interno/objetivos")
@RequiredArgsConstructor
public class ObjetivoInternoController {

    private final ObjetivoAprendizajeService objetivoService;

    @GetMapping
    public List<ObjetivoElegibleDTO> listar() {
        return objetivoService.listarElegibles();
    }
}