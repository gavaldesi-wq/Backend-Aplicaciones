package cl.mpazpro.contenidos.client;

import cl.mpazpro.contenidos.model.EstadoContenido;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.UUID;

@Component
public class HistorialAprobacionClient {

    private final RestClient restClient;

    public HistorialAprobacionClient(
            @Value("${mpaz.admin.url:http://127.0.0.1:8081}")
            String adminUrl
    ) {
        this.restClient = RestClient.create(adminUrl);
    }

    public void registrarUnidad(
            Long unidadId,
            EstadoContenido estadoAnterior,
            EstadoContenido estadoNuevo,
            String authorization
    ) {
        Map<String, Object> evento = Map.of(
                "eventoId", UUID.randomUUID().toString(),
                "tipoRecurso", "UNIDAD",
                "recursoId", unidadId,
                "estadoAnterior", estadoAnterior.name(),
                "estadoNuevo", estadoNuevo.name()
        );

        try {
            restClient.post()
                    .uri("/api/v1/utp/historial-aprobaciones")
                    .header(HttpHeaders.AUTHORIZATION, authorization)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(evento)
                    .retrieve()
                    .toBodilessEntity();

        } catch (RestClientResponseException ex) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "ms-admin rechazo el registro del historial",
                    ex
            );

        } catch (RestClientException ex) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "No fue posible conectar con ms-admin",
                    ex
            );
        }
    }



    public void registrarObjetivo(
            Long objetivoId,
            EstadoContenido estadoAnterior,
            EstadoContenido estadoNuevo,
            String authorization
    ) {
        Map<String, Object> evento = Map.of(
                "eventoId", UUID.randomUUID().toString(),
                "tipoRecurso", "OBJETIVO_APRENDIZAJE",
                "recursoId", objetivoId,
                "estadoAnterior", estadoAnterior.name(),
                "estadoNuevo", estadoNuevo.name()
        );

        try {
            restClient.post()
                    .uri("/api/v1/utp/historial-aprobaciones")
                    .header(HttpHeaders.AUTHORIZATION, authorization)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(evento)
                    .retrieve()
                    .toBodilessEntity();

        } catch (RestClientResponseException ex) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "ms-admin rechazo el registro del historial",
                    ex
            );

        } catch (RestClientException ex) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "No fue posible conectar con ms-admin",
                    ex
            );
        }
    }


    public void registrarContenido(
            Long contenidoId,
            EstadoContenido estadoAnterior,
            EstadoContenido estadoNuevo,
            String authorization
    ) {
        Map<String, Object> evento = Map.of(
                "eventoId", UUID.randomUUID().toString(),
                "tipoRecurso", "CONTENIDO_DIDACTICO",
                "recursoId", contenidoId,
                "estadoAnterior", estadoAnterior.name(),
                "estadoNuevo", estadoNuevo.name()
        );

        try {
            restClient.post()
                    .uri("/api/v1/utp/historial-aprobaciones")
                    .header(HttpHeaders.AUTHORIZATION, authorization)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(evento)
                    .retrieve()
                    .toBodilessEntity();

        } catch (RestClientResponseException ex) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "ms-admin rechazo el registro del historial",
                    ex
            );

        } catch (RestClientException ex) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "No fue posible conectar con ms-admin",
                    ex
            );
        }
}


}