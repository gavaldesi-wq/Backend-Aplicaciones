package cl.mpazpro.actividades.dto;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaginacionDTO<T> {

    private List<T> items;
    private long total;
    private int pagina;
    private int tamano;

    public static <T> PaginacionDTO<T> fromList(
            List<T> datos, int pagina, int tamano) {

        if (pagina < 0 || tamano < 1 || tamano > 100) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Parametros de paginacion invalidos"
            );
        }

        int inicio = (int) Math.min(
                (long) pagina * tamano,
                datos.size()
        );

        int fin = Math.min(inicio + tamano, datos.size());

        return new PaginacionDTO<>(
                datos.subList(inicio, fin),
                datos.size(),
                pagina,
                tamano
        );
    }
}