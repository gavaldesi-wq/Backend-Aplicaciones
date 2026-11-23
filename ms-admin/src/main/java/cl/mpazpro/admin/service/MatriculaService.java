package cl.mpazpro.admin.service;

import cl.mpazpro.admin.dto.MatriculaDTO;
import cl.mpazpro.admin.model.Curso;
import cl.mpazpro.admin.model.Matricula;
import cl.mpazpro.admin.model.Rol;
import cl.mpazpro.admin.model.Usuario;
import cl.mpazpro.admin.repository.CursoRepository;
import cl.mpazpro.admin.repository.MatriculaRepository;
import cl.mpazpro.admin.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MatriculaService {

    private final MatriculaRepository matriculaRepository;
    private final CursoRepository cursoRepository;
    private final UsuarioRepository usuarioRepository;

    public List<MatriculaDTO> listarMatriculas() {
        return matriculaRepository.findAll()
                .stream()
                .map(MatriculaDTO::fromModel)
                .toList();
    }

    public List<MatriculaDTO> listarPorCurso(Long cursoId) {
        return matriculaRepository.findByCursoId(cursoId)
                .stream()
                .map(MatriculaDTO::fromModel)
                .toList();
    }

    public MatriculaDTO obtenerMatriculaPorId(Long id) {
        return MatriculaDTO.fromModel(buscarMatricula(id));
    }

    @Transactional
    public MatriculaDTO crearMatricula(MatriculaDTO dto) {
        validarCurso(dto.getCursoId());
        validarEstudiante(dto.getEstudianteId());

        if (matriculaRepository.findByCursoIdAndEstudianteId(
                dto.getCursoId(),
                dto.getEstudianteId()
        ).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El estudiante ya tiene una matricula en este curso"
            );
        }

        Matricula matricula = dto.toModel();

        return MatriculaDTO.fromModel(
                matriculaRepository.save(matricula)
        );
    }

    @Transactional
    public MatriculaDTO actualizarMatricula(Long id, MatriculaDTO dto) {
        Matricula matricula = buscarMatricula(id);

        validarCurso(dto.getCursoId());
        validarEstudiante(dto.getEstudianteId());

        Optional<Matricula> existente =
                matriculaRepository.findByCursoIdAndEstudianteId(
                        dto.getCursoId(),
                        dto.getEstudianteId()
                );

        if (existente.isPresent()
                && !existente.get().getId().equals(id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El estudiante ya tiene una matricula en este curso"
            );
        }

        matricula.setCursoId(dto.getCursoId());
        matricula.setEstudianteId(dto.getEstudianteId());

        if (dto.getActivo() != null) {
            matricula.setActivo(dto.getActivo());
        }

        return MatriculaDTO.fromModel(
                matriculaRepository.save(matricula)
        );
    }

    @Transactional
    public void desactivarMatricula(Long id) {
        Matricula matricula = buscarMatricula(id);
        matricula.setActivo(false);
        matriculaRepository.save(matricula);
    }

    private Matricula buscarMatricula(Long id) {
        return matriculaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Matricula no encontrada"
                ));
    }

    private void validarCurso(Long cursoId) {
        Curso curso = cursoRepository.findById(cursoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Curso no encontrado"
                ));

        if (!Boolean.TRUE.equals(curso.getActivo())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El curso no esta activo"
            );
        }
    }

    private void validarEstudiante(Long estudianteId) {
        Usuario estudiante = usuarioRepository.findById(estudianteId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Estudiante no encontrado"
                ));

        if (estudiante.getRol() != Rol.ESTUDIANTE
                || !Boolean.TRUE.equals(estudiante.getActivo())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El usuario debe ser un estudiante activo"
            );
        }
    }
}
