package cl.mpazpro.admin.service;

import cl.mpazpro.admin.dto.CursoDTO;
import cl.mpazpro.admin.model.Curso;
import cl.mpazpro.admin.model.Rol;
import cl.mpazpro.admin.model.Usuario;
import cl.mpazpro.admin.repository.CursoRepository;
import cl.mpazpro.admin.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CursoService {

    private final CursoRepository cursoRepository;
    private final UsuarioRepository usuarioRepository;

    public List<CursoDTO> listarCursos() {
        return cursoRepository.findAll()
                .stream()
                .map(CursoDTO::fromModel)
                .toList();
    }

    public CursoDTO obtenerCursoPorId(Long id) {
        return CursoDTO.fromModel(buscarCurso(id));
    }

    @Transactional
    public CursoDTO crearCurso(CursoDTO dto) {
        String nombre = dto.getNombre().trim();

        validarDocente(dto.getDocenteId());

        if (cursoRepository.existsByNombreIgnoreCaseAndAnioEscolar(
                nombre, dto.getAnioEscolar())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El curso ya existe para ese año"
            );
        }

        Curso curso = dto.toModel();
        curso.setNombre(nombre);

        return CursoDTO.fromModel(cursoRepository.save(curso));
    }

    @Transactional
    public CursoDTO actualizarCurso(Long id, CursoDTO dto) {
        Curso curso = buscarCurso(id);
        String nombre = dto.getNombre().trim();

        validarDocente(dto.getDocenteId());

        boolean cambioIdentificacion =
                !curso.getNombre().equalsIgnoreCase(nombre)
                || !curso.getAnioEscolar().equals(dto.getAnioEscolar());

        if (cambioIdentificacion &&
                cursoRepository.existsByNombreIgnoreCaseAndAnioEscolar(
                        nombre, dto.getAnioEscolar())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El curso ya existe para ese año"
            );
        }

        curso.setNombre(nombre);
        curso.setAnioEscolar(dto.getAnioEscolar());
        curso.setDocenteId(dto.getDocenteId());

        if (dto.getActivo() != null) {
            curso.setActivo(dto.getActivo());
        }

        return CursoDTO.fromModel(cursoRepository.save(curso));
    }

    @Transactional
    public void desactivarCurso(Long id) {
        Curso curso = buscarCurso(id);
        curso.setActivo(false);
        cursoRepository.save(curso);
    }

    private Curso buscarCurso(Long id) {
        return cursoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Curso no encontrado"
                ));
    }

    private void validarDocente(Long docenteId) {
        Usuario docente = usuarioRepository.findById(docenteId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Docente no encontrado"
                ));

        if (docente.getRol() != Rol.DOCENTE
                || !Boolean.TRUE.equals(docente.getActivo())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El usuario debe ser un docente activo"
            );
        }
    }
}
