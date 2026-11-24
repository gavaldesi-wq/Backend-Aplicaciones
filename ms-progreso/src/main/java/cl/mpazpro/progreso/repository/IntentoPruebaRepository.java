package cl.mpazpro.progreso.repository;

import cl.mpazpro.progreso.model.IntentoPrueba;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IntentoPruebaRepository extends JpaRepository<IntentoPrueba, Long> {

    Optional<IntentoPrueba> findByClientEventId(String clientEventId);

    int countByPruebaIdAndEstudianteId(Long pruebaId, Long estudianteId);
}
