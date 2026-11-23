
package cl.mpazpro.contenidos.repository;

import cl.mpazpro.contenidos.model.Unidad;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UnidadRepository extends JpaRepository<Unidad, Long> {

    List<Unidad> findByAsignatura_IdOrderByOrdenAsc(Long asignaturaId);
}
