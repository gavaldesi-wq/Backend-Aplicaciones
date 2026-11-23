
package cl.mpazpro.contenidos.repository;

import cl.mpazpro.contenidos.model.ObjetivoAprendizaje;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ObjetivoAprendizajeRepository
        extends JpaRepository<ObjetivoAprendizaje, Long> {

    List<ObjetivoAprendizaje> findByUnidad_IdOrderByIdAsc(Long unidadId);
}
