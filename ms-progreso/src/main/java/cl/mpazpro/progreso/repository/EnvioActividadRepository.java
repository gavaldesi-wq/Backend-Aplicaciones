package cl.mpazpro.progreso.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.mpazpro.progreso.model.EnvioActividad;

public interface EnvioActividadRepository extends JpaRepository<EnvioActividad, Long> {

    Optional<EnvioActividad> findByClientEventId(String clientEventId);
    java.util.List<EnvioActividad> findByEstudianteIdOrderByRegistradoEnAsc(Long estudianteId);
}
