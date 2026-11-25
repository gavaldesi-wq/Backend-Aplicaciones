package cl.mpazpro.progreso.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.mpazpro.progreso.model.IntentoPrueba;

public interface IntentoPruebaRepository extends JpaRepository<IntentoPrueba, Long> {

    Optional<IntentoPrueba> findByClientEventId(String clientEventId);

    int countByPruebaIdAndEstudianteId(Long pruebaId, Long estudianteId);
    java.util.List<IntentoPrueba> findByEstudianteIdOrderByIniciadoEnAsc(Long estudianteId);
}
