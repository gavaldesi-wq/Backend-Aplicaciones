package cl.mpazpro.admin.repository;

import cl.mpazpro.admin.model.HistorialAprobacion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HistorialAprobacionRepository
        extends JpaRepository<HistorialAprobacion, Long> {

    Optional<HistorialAprobacion> findByEventoId(String eventoId);

    Page<HistorialAprobacion> findByTipoRecursoAndRecursoId(
            String tipoRecurso,
            Long recursoId,
            Pageable pageable
    );
}
