package cl.mpazpro.actividades.repository;

import cl.mpazpro.actividades.model.Alternativa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlternativaRepository extends JpaRepository<Alternativa, Long> {

    List<Alternativa> findByPregunta_IdOrderByOrdenAsc(Long preguntaId);
}
