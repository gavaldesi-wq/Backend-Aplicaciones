package cl.mpazpro.contenidos.service;

import cl.mpazpro.contenidos.dto.AsignaturaDTO;
import cl.mpazpro.contenidos.repository.AsignaturaRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AsignaturaService {

    private final AsignaturaRepository asignaturaRepository;

    @Transactional(readOnly = true)
    public List<AsignaturaDTO> getAll() {
        return asignaturaRepository.findAll()
                .stream()
                .map(AsignaturaDTO::fromModel)
                .toList();
    }

    @Transactional(readOnly = true)
    public AsignaturaDTO getById(Long id) {
        return asignaturaRepository.findById(id)
                .map(AsignaturaDTO::fromModel)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Asignatura no encontrada"
                ));
    }
}
