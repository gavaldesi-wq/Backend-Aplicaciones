package cl.mpazpro.admin.repository;

import cl.mpazpro.admin.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {

    boolean existsByNombreIgnoreCaseAndAnioEscolar(
            String nombre,
            Integer anioEscolar
    );
    List<Curso> findByDocenteIdAndActivoTrue(Long docenteId);
}
