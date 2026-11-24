package cl.mpazpro.progreso.repository;

import cl.mpazpro.progreso.model.EventoSincronizado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventoSincronizadoRepository extends JpaRepository<EventoSincronizado, String> {
}
