package cl.mpazpro.actividades.repository;

import cl.mpazpro.actividades.model.ActividadDesafio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActividadDesafioRepository extends JpaRepository<ActividadDesafio, Long> {

    List<ActividadDesafio> findByObjetivoAprendizajeIdOrderByOrdenAsc(Long objetivoAprendizajeId);
}
