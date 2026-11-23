package cl.mpazpro.contenidos.dto;

import cl.mpazpro.contenidos.model.ContenidoDidactico;
import cl.mpazpro.contenidos.model.EstadoContenido;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ContenidoDidacticoDTO {

    private Long id;

    @NotNull
    @Positive
    private Long objetivoAprendizajeId;

    @NotBlank
    @Size(max = 200)
    private String titulo;

    private String explicacionEscrita;

    @Size(max = 500)
    private String archivoAudio;

    private List<String> imagenes = new ArrayList<>();

    private String ejemplosGuiados;

    private String instrucciones;

    @NotNull
    @Min(0)
    private Integer orden;

    @NotBlank
    @Size(max = 200)
    private String responsableOrigen;

    @NotBlank
    @Size(max = 200)
    private String licencia;

    private EstadoContenido estado;

    public static ContenidoDidacticoDTO fromModel(
            ContenidoDidactico contenido) {

        ContenidoDidacticoDTO dto =
                new ContenidoDidacticoDTO();

        dto.setId(contenido.getId());

        dto.setObjetivoAprendizajeId(
                contenido.getObjetivoAprendizaje().getId()
        );

        dto.setTitulo(contenido.getTitulo());
        dto.setExplicacionEscrita(
                contenido.getExplicacionEscrita()
        );
        dto.setArchivoAudio(contenido.getArchivoAudio());

        dto.setImagenes(
                contenido.getImagenes() == null
                        ? new ArrayList<>()
                        : new ArrayList<>(contenido.getImagenes())
        );

        dto.setEjemplosGuiados(contenido.getEjemplosGuiados());
        dto.setInstrucciones(contenido.getInstrucciones());
        dto.setOrden(contenido.getOrden());

        dto.setResponsableOrigen(contenido.getResponsableOrigen());
        dto.setLicencia(contenido.getLicencia());
        dto.setEstado(contenido.getEstado());

        return dto;
    }

    public ContenidoDidactico toModel() {
        ContenidoDidactico contenido =
                new ContenidoDidactico();

        contenido.setTitulo(titulo);
        contenido.setExplicacionEscrita(explicacionEscrita);
        contenido.setArchivoAudio(archivoAudio);

        contenido.setImagenes(
                imagenes == null
                        ? new ArrayList<>()
                        : new ArrayList<>(imagenes)
        );

        contenido.setEjemplosGuiados(ejemplosGuiados);
        contenido.setInstrucciones(instrucciones);
        contenido.setOrden(orden);
        contenido.setResponsableOrigen(responsableOrigen);
        contenido.setLicencia(licencia);

        return contenido;
    }
}
