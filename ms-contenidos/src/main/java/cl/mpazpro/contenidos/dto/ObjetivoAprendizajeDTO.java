
package cl.mpazpro.contenidos.dto;

import cl.mpazpro.contenidos.model.ObjetivoAprendizaje;
import cl.mpazpro.contenidos.model.EstadoContenido;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ObjetivoAprendizajeDTO {

    private Long id;

    @NotNull
    @Positive
    private Long unidadId;

    @NotBlank
    @Size(max = 30)
    private String codigo;

    @NotBlank
    private String descripcion;

    @Size(max = 150)
    private String ejeCurricular;

    private String textoReferencia;

    @NotNull
    @Min(0)
    private Integer orden;

    private EstadoContenido estado;


    public static ObjetivoAprendizajeDTO fromModel(
            ObjetivoAprendizaje objetivo) {

        ObjetivoAprendizajeDTO dto =
                new ObjetivoAprendizajeDTO();

        dto.setId(objetivo.getId());
        dto.setUnidadId(objetivo.getUnidad().getId());
        dto.setCodigo(objetivo.getCodigo());
        dto.setDescripcion(objetivo.getDescripcion());
        dto.setEjeCurricular(objetivo.getEjeCurricular());
        dto.setTextoReferencia(objetivo.getTextoReferencia());
        dto.setOrden(objetivo.getOrden());
        dto.setEstado(objetivo.getEstado());

        return dto;
    }


    public ObjetivoAprendizaje toModel() {
        ObjetivoAprendizaje objetivo =
                new ObjetivoAprendizaje();

        objetivo.setCodigo(codigo);
        objetivo.setDescripcion(descripcion);
        objetivo.setEjeCurricular(ejeCurricular);
        objetivo.setTextoReferencia(textoReferencia);
        objetivo.setOrden(orden);


        return objetivo;
    }
}
