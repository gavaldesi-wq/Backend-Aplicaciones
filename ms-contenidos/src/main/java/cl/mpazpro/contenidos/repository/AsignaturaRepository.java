
package cl.mpazpro.contenidos.repository;

import cl.mpazpro.contenidos.model.Asignatura;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AsignaturaRepository extends JpaRepository<Asignatura, Long> {

    boolean existsByNombreIgnoreCase(String nombre);
}
