package cl.mpazpro.actividades.repository;

import cl.mpazpro.actividades.model.TransicionPublicacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransicionPublicacionRepository extends JpaRepository<TransicionPublicacion, Long> {

    Optional<TransicionPublicacion> findByEventoId(String eventoId);
}
