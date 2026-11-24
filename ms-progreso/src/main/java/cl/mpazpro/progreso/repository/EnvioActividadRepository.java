package cl.mpazpro.progreso.repository;

import cl.mpazpro.progreso.model.EnvioActividad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EnvioActividadRepository extends JpaRepository<EnvioActividad, Long> {

    Optional<EnvioActividad> findByClientEventId(String clientEventId);
}
