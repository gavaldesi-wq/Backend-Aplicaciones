package cl.mpazpro.progreso.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import cl.mpazpro.progreso.dto.PreguntaEstudianteDTO;
import cl.mpazpro.progreso.dto.PruebaPublicadaDTO;
import cl.mpazpro.progreso.dto.SolucionActividadDTO;
import cl.mpazpro.progreso.dto.SolucionPruebaDTO;
@Component
public class ActividadesCliente {

    private final RestClient cliente;

    public ActividadesCliente(
            @Value("${mpaz.actividades.url}") String baseUrl) {
        this.cliente = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public SolucionActividadDTO obtenerSolucionario(Long actividadId) {
        try {
            SolucionActividadDTO solucion = cliente.get()
                    .uri("/api/v1/interno/actividades/{id}/solucionario", actividadId)
                    .retrieve()
                    .body(SolucionActividadDTO.class);

            if (solucion == null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_GATEWAY,
                        "No se pudo consultar el solucionario"
                );
            }
            return solucion;
        } catch (RestClientResponseException ex) {
            if (ex.getStatusCode().value() == 404) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Actividad no encontrada"
                );
            }
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "No se pudo consultar el solucionario"
            );
        }
    }

    public PruebaPublicadaDTO obtenerPruebaPublicada(Long objetivoAprendizajeId) {
        try {
            PruebaPublicadaDTO prueba = cliente.get()
                    .uri("/api/v1/interno/objetivos/{id}/prueba-final", objetivoAprendizajeId)
                    .retrieve()
                    .body(PruebaPublicadaDTO.class);
    
            if (prueba == null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_GATEWAY,
                        "No se pudo consultar la prueba"
                );
            }
            return prueba;
        } catch (RestClientResponseException ex) {
            if (ex.getStatusCode().value() == 404) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Prueba no encontrada"
                );
            }
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "No se pudo consultar la prueba"
            );
        }
    }

    public PruebaPublicadaDTO obtenerPruebaPorId(Long pruebaId) {
        try {
            PruebaPublicadaDTO prueba = cliente.get()
                    .uri("/api/v1/interno/pruebas-finales/{id}", pruebaId)
                    .retrieve()
                    .body(PruebaPublicadaDTO.class);
    
            if (prueba == null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_GATEWAY,
                        "No se pudo consultar la prueba"
                );
            }
            return prueba;
        } catch (RestClientResponseException ex) {
            if (ex.getStatusCode().value() == 404) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Prueba no encontrada"
                );
            }
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "No se pudo consultar la prueba"
            );
        }
    }

    public List<PreguntaEstudianteDTO> obtenerPreguntasDePrueba(Long pruebaId) {
        try {
            List<PreguntaEstudianteDTO> preguntas = cliente.get()
                    .uri("/api/v1/interno/pruebas-finales/{id}/preguntas", pruebaId)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});
    
            if (preguntas == null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_GATEWAY,
                        "No se pudo consultar las preguntas"
                );
            }
            return preguntas;
        } catch (RestClientResponseException ex) {
            if (ex.getStatusCode().value() == 404) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Prueba no encontrada"
                );
            }
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "No se pudo consultar las preguntas"
            );
        }
    }

    public SolucionPruebaDTO obtenerSolucionarioPrueba(Long pruebaId) {
        try {
            SolucionPruebaDTO solucion = cliente.get()
                    .uri("/api/v1/interno/pruebas-finales/{id}/solucionario", pruebaId)
                    .retrieve()
                    .body(SolucionPruebaDTO.class);
    
            if (solucion == null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_GATEWAY,
                        "No se pudo consultar el solucionario"
                );
            }
            return solucion;
        } catch (RestClientResponseException ex) {
            if (ex.getStatusCode().value() == 404) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Prueba no encontrada"
                );
            }
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "No se pudo consultar el solucionario"
            );
        }
    }
}