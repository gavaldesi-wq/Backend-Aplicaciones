package cl.mpazpro.actividades.repository;

import cl.mpazpro.actividades.model.PruebaFinal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PruebaFinalRepository extends JpaRepository<PruebaFinal, Long> {

    List<PruebaFinal> findByObjetivoAprendizajeId(Long objetivoAprendizajeId);
}
