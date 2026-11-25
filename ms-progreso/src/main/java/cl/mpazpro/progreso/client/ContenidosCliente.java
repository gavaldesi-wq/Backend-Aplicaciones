package cl.mpazpro.progreso.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.server.ResponseStatusException;
import cl.mpazpro.progreso.dto.ObjetivoDisponibleDTO;
@Component
public class ContenidosCliente {

    private final RestClient cliente;

    public ContenidosCliente(
            @Value("${mpaz.contenidos.url}") String baseUrl) {
        this.cliente = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public List<ObjetivoDisponibleDTO> listarObjetivosElegibles() {
        try {
            List<ObjetivoDisponibleDTO> objetivos = cliente.get()
                    .uri("/api/v1/interno/objetivos")
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});
    
            if (objetivos == null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_GATEWAY,
                        "No se pudo consultar los objetivos"
                );
            }
            return objetivos;
        } catch (RestClientResponseException ex) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "No se pudo consultar los objetivos"
            );
        }
    }
}