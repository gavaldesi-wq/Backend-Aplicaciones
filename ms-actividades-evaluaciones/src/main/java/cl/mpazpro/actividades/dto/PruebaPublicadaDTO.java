package cl.mpazpro.actividades.dto;

import java.time.Instant;

import cl.mpazpro.actividades.model.EstadoContenido;
import cl.mpazpro.actividades.model.PruebaFinal;
import lombok.Data;

@Data
public class PruebaPublicadaDTO {

    private Long id;
    private Long objetivoAprendizajeId;
    private EstadoContenido estado;
    private Instant disponibleDesde;
    private Instant disponibleHasta;
    private Integer limiteIntentos;

    public static PruebaPublicadaDTO fromModel(PruebaFinal prueba) {
        PruebaPublicadaDTO dto = new PruebaPublicadaDTO();
        dto.setId(prueba.getId());
        dto.setObjetivoAprendizajeId(prueba.getObjetivoAprendizajeId());
        dto.setEstado(prueba.getEstado());
        dto.setDisponibleDesde(prueba.getDisponibleDesde());
        dto.setDisponibleHasta(prueba.getDisponibleHasta());
        dto.setLimiteIntentos(prueba.getLimiteIntentos());
        return dto;
    }
}