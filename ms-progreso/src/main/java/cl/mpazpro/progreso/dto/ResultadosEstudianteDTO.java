package cl.mpazpro.progreso.dto;

import java.util.List;

import lombok.Data;

@Data
public class ResultadosEstudianteDTO {

    private Long estudianteId;
    private List<EnvioResultadoDTO> envios;
    private List<IntentoResultadoDTO> intentos;
}