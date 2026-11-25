package cl.mpazpro.progreso.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.mpazpro.progreso.model.RespuestaEstudiante;

public interface RespuestaEstudianteRepository extends JpaRepository<RespuestaEstudiante, Long> {
    java.util.List<RespuestaEstudiante> findByEnvioActividad_Id(Long envioActividadId);
    java.util.List<RespuestaEstudiante> findByIntento_Id(Long intentoId);
}
