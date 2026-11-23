package cl.mpazpro.admin.service;

import cl.mpazpro.admin.dto.CursoDTO;
import cl.mpazpro.admin.dto.UsuarioDTO;
import cl.mpazpro.admin.model.Curso;
import cl.mpazpro.admin.model.Rol;
import cl.mpazpro.admin.model.Usuario;
import cl.mpazpro.admin.repository.CursoRepository;
import cl.mpazpro.admin.repository.MatriculaRepository;
import cl.mpazpro.admin.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DocenteService {

    private final UsuarioRepository usuarioRepository;
    private final CursoRepository cursoRepository;
    private final MatriculaRepository matriculaRepository;

    public Map<String, Object> listarCursos(
            String codigoUsuario,
            int pagina,
            int tamano
    ) {
        Usuario docente = obtenerDocente(codigoUsuario);

        List<CursoDTO> cursos = cursoRepository
                .findByDocenteIdAndActivoTrue(docente.getId())
                .stream()
                .sorted(Comparator.comparing(Curso::getId))
                .map(CursoDTO::fromModel)
                .toList();

        return paginar(cursos, pagina, tamano);
    }

    public Map<String, Object> listarEstudiantes(
            String codigoUsuario,
            Long cursoId,
            int pagina,
            int tamano
    ) {
        Usuario docente = obtenerDocente(codigoUsuario);

        Curso curso = cursoRepository.findById(cursoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Curso no encontrado"
                ));

        if (!curso.getDocenteId().equals(docente.getId())
                || !Boolean.TRUE.equals(curso.getActivo())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "No tienes acceso a este curso"
            );
        }

        List<UsuarioDTO> estudiantes = matriculaRepository
                .findByCursoIdAndActivoTrue(cursoId)
                .stream()
                .flatMap(matricula -> usuarioRepository
                        .findById(matricula.getEstudianteId())
                        .stream())
                .filter(usuario ->
                        usuario.getRol() == Rol.ESTUDIANTE
                        && Boolean.TRUE.equals(usuario.getActivo()))
                .sorted(Comparator.comparing(Usuario::getId))
                .map(UsuarioDTO::fromModel)
                .toList();

        return paginar(estudiantes, pagina, tamano);
    }

    private Usuario obtenerDocente(String codigoUsuario) {
        Usuario usuario = usuarioRepository
                .findByCodigoUsuarioIgnoreCase(codigoUsuario)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "Acceso denegado"
                ));

        if (usuario.getRol() != Rol.DOCENTE
                || !Boolean.TRUE.equals(usuario.getActivo())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Acceso denegado"
            );
        }

        return usuario;
    }

    private <T> Map<String, Object> paginar(
            List<T> elementos,
            int pagina,
            int tamano
    ) {
        long inicio = (long) pagina * tamano;

        int desde = (int) Math.min(inicio, elementos.size());
        int hasta = (int) Math.min(inicio + tamano, elementos.size());

        return Map.of(
                "items", elementos.subList(desde, hasta),
                "total", elementos.size(),
                "pagina", pagina,
                "tamano", tamano
        );
    }
}
