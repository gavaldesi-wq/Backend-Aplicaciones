package cl.mpazpro.progreso.repository;

import cl.mpazpro.progreso.model.Asignacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AsignacionRepository extends JpaRepository<Asignacion, Long> {

    List<Asignacion> findByCursoId(Long cursoId);
}
