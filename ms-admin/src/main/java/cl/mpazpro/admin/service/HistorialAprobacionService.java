package cl.mpazpro.admin.service;

import cl.mpazpro.admin.dto.HistorialAprobacionDTO;
import cl.mpazpro.admin.model.HistorialAprobacion;
import cl.mpazpro.admin.model.Rol;
import cl.mpazpro.admin.model.Usuario;
import cl.mpazpro.admin.repository.HistorialAprobacionRepository;
import cl.mpazpro.admin.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HistorialAprobacionService {

    private final HistorialAprobacionRepository historialRepository;
    private final UsuarioRepository usuarioRepository;
    

    @Transactional(readOnly = true)
    public Map<String, Object> listarPorRecurso(
            String tipoRecurso,
            Long recursoId,
            int pagina,
            int tamano
    ) {
        Page<HistorialAprobacion> resultado =
                historialRepository.findByTipoRecursoAndRecursoId(
                        tipoRecurso,
                        recursoId,
                        PageRequest.of(
                                pagina,
                                tamano,
                                Sort.by(Sort.Direction.DESC, "creadoEn")
                        )
                );

        return Map.of(
                "items", resultado.getContent()
                        .stream()
                        .map(HistorialAprobacionDTO::fromModel)
                        .toList(),
                "total", resultado.getTotalElements(),
                "pagina", pagina,
                "tamano", tamano
        );
    }

    @Transactional
    public HistorialAprobacionDTO registrarEvento(
            UUID eventoId,
            String tipoRecurso,
            Long recursoId,
            String estadoAnterior,
            String estadoNuevo,
            Long usuarioId
    ) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuario no encontrado"
                ));

        if (usuario.getRol() != Rol.UTP
                || !Boolean.TRUE.equals(usuario.getActivo())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "El usuario no tiene permisos UTP"
            );
        }

        var previo = historialRepository.findByEventoId(
                eventoId.toString()
        );

        if (previo.isPresent()) {
            HistorialAprobacion existente = previo.get();

            if (!Objects.equals(existente.getTipoRecurso(), tipoRecurso)
                    || !Objects.equals(existente.getRecursoId(), recursoId)
                    || !Objects.equals(existente.getEstadoAnterior(), estadoAnterior)
                    || !Objects.equals(existente.getEstadoNuevo(), estadoNuevo)
                    || !Objects.equals(existente.getUsuarioId(), usuarioId)) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "El evento ya existe con otros datos"
                );
            }

            return HistorialAprobacionDTO.fromModel(existente);
        }

        HistorialAprobacion historial = new HistorialAprobacion();

        historial.setEventoId(eventoId.toString());
        historial.setTipoRecurso(tipoRecurso);
        historial.setRecursoId(recursoId);
        historial.setEstadoAnterior(estadoAnterior);
        historial.setEstadoNuevo(estadoNuevo);
        historial.setUsuarioId(usuarioId);

        return HistorialAprobacionDTO.fromModel(
                historialRepository.save(historial)
        );
    }
}
