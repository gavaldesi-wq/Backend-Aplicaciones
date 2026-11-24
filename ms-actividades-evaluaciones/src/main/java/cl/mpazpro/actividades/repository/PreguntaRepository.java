package cl.mpazpro.actividades.repository;

import cl.mpazpro.actividades.model.Pregunta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PreguntaRepository extends JpaRepository<Pregunta, Long> {

    List<Pregunta> findByActividad_IdOrderByOrdenAsc(Long actividadId);

    List<Pregunta> findByPruebaFinal_IdOrderByOrdenAsc(Long pruebaFinalId);
}
