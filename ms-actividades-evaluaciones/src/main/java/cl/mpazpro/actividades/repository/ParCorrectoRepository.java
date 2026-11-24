package cl.mpazpro.actividades.repository;

import cl.mpazpro.actividades.model.ParCorrecto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParCorrectoRepository extends JpaRepository<ParCorrecto, Long> {

    List<ParCorrecto> findByPregunta_Id(Long preguntaId);
}
