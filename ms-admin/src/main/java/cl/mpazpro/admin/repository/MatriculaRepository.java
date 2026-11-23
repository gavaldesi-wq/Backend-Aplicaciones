package cl.mpazpro.admin.repository;

import cl.mpazpro.admin.model.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatriculaRepository extends JpaRepository<Matricula, Long> {

    List<Matricula> findByCursoId(Long cursoId);

    List<Matricula> findByEstudianteId(Long estudianteId);

    Optional<Matricula> findByCursoIdAndEstudianteId(
            Long cursoId,
            Long estudianteId
    );

    List<Matricula> findByCursoIdAndActivoTrue(Long cursoId);
}
