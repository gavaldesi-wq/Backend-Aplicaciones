package cl.mpazpro.progreso.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import cl.mpazpro.progreso.client.ContenidosCliente;
import cl.mpazpro.progreso.dto.ObjetivoDisponibleDTO;
import cl.mpazpro.progreso.security.SesionActual;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ObjetivoDisponibleService {

    private final ContenidosCliente contenidosCliente;
    private final SesionActual sesionActual;

    @Transactional(readOnly = true)
    public List<ObjetivoDisponibleDTO> listar(Long cursoId) {
        sesionActual.docenteId();
        if (cursoId == null || cursoId < 1) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "cursoId es obligatorio"
            );
        }
        return contenidosCliente.listarObjetivosElegibles();
    }
}