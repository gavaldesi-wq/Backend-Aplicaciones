package cl.mpazpro.actividades.repository;

import cl.mpazpro.actividades.model.ItemSecuencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemSecuenciaRepository extends JpaRepository<ItemSecuencia, Long> {

    List<ItemSecuencia> findByPregunta_IdOrderByOrdenCorrectoAsc(Long preguntaId);
}
