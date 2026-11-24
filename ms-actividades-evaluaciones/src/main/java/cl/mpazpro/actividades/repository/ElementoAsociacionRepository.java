package cl.mpazpro.actividades.repository;

import cl.mpazpro.actividades.model.ElementoAsociacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ElementoAsociacionRepository extends JpaRepository<ElementoAsociacion, Long> {

    List<ElementoAsociacion> findByPregunta_IdOrderByOrdenAsc(Long preguntaId);
}
