
package cl.mpazpro.contenidos.repository;

import cl.mpazpro.contenidos.model.ContenidoDidactico;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ContenidoDidacticoRepository
        extends JpaRepository<ContenidoDidactico, Long> {

    List<ContenidoDidactico> findByObjetivoAprendizaje_IdOrderByOrdenAsc(
            Long objetivoAprendizajeId);
}
