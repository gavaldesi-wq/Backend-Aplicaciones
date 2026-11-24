package cl.mpazpro.actividades.dto;

import java.time.Instant;

import cl.mpazpro.actividades.model.EstadoContenido;
import cl.mpazpro.actividades.model.PruebaFinal;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PruebaFinalDTO {

    private Long id;

    @NotNull
    @Positive
    private Long objetivoAprendizajeId;

    @NotBlank
    @Size(max = 200)
    private String titulo;

    private EstadoContenido estado;

    private Instant disponibleDesde;

    private Instant disponibleHasta;

    @Min(1)
    private Integer limiteIntentos;

    public static PruebaFinalDTO fromModel(PruebaFinal prueba) {
        PruebaFinalDTO dto = new PruebaFinalDTO();

        dto.setId(prueba.getId());
        dto.setObjetivoAprendizajeId(prueba.getObjetivoAprendizajeId());
        dto.setTitulo(prueba.getTitulo());
        dto.setEstado(prueba.getEstado());
        dto.setDisponibleDesde(prueba.getDisponibleDesde());
        dto.setDisponibleHasta(prueba.getDisponibleHasta());
        dto.setLimiteIntentos(prueba.getLimiteIntentos());

        return dto;
    }

    public PruebaFinal toModel() {
        PruebaFinal prueba = new PruebaFinal();

        prueba.setTitulo(titulo);
        prueba.setDisponibleDesde(disponibleDesde);
        prueba.setDisponibleHasta(disponibleHasta);
        prueba.setLimiteIntentos(limiteIntentos);

        return prueba;
    }
}