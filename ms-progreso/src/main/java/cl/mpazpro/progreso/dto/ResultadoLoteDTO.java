package cl.mpazpro.progreso.dto;

import java.util.List;

import lombok.Data;

@Data
public class ResultadoLoteDTO {

    private List<ResultadoEventoDTO> resultados;
}