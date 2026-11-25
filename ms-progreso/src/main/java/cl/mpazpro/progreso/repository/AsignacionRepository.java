package cl.mpazpro.progreso.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.mpazpro.progreso.model.Asignacion;

public interface AsignacionRepository extends JpaRepository<Asignacion, Long> {

    List<Asignacion> findByCursoId(Long cursoId);
    java.util.List<Asignacion> findByObjetivoAprendizajeIdAndActivaTrue(Long objetivoAprendizajeId);
    java.util.List<Asignacion> findByCursoIdAndObjetivoAprendizajeId(Long cursoId, Long objetivoAprendizajeId);
    List<Asignacion> findByActivaTrue();
}
