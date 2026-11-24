package cl.mpazpro.progreso;

import ch.vorburger.mariadb4j.DB;
import ch.vorburger.mariadb4j.DBConfigurationBuilder;
import cl.mpazpro.progreso.model.Asignacion;
import cl.mpazpro.progreso.repository.AsignacionRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class EsquemaProgresoTest {

    private static DB base;

    @DynamicPropertySource
    static void baseEmbebida(DynamicPropertyRegistry registry) {
        try {
            DBConfigurationBuilder config = DBConfigurationBuilder.newBuilder();
            config.setPort(0);
            config.addArg("--character-set-server=utf8mb4");
            config.addArg("--collation-server=utf8mb4_unicode_ci");
            base = DB.newEmbeddedDB(config.build());
            base.start();
            base.createDB("mpaz_progreso");
            registry.add("spring.datasource.url", () ->
                    "jdbc:mysql://127.0.0.1:" + config.getPort()
                            + "/mpaz_progreso?characterEncoding=utf8&connectionTimeZone=UTC&allowPublicKeyRetrieval=true&useSSL=false");
            registry.add("spring.datasource.username", () -> "root");
            registry.add("spring.datasource.password", () -> "");
            registry.add("spring.jpa.database-platform", () -> "org.hibernate.dialect.MariaDBDialect");
            registry.add("spring.jpa.hibernate.ddl-auto", () -> "none");
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo iniciar la base de prueba", e);
        }
    }

    @AfterAll
    static void detener() throws Exception {
        if (base != null) {
            base.stop();
        }
    }

    @Autowired
    private AsignacionRepository asignaciones;

    @Autowired
    private DataSource dataSource;

    @Test
    void creaLasTablasYCargaLaAsignacionDelContrato() throws Exception {
        Asignacion asignacion = asignaciones.findById(1L).orElseThrow();
        assertEquals(1L, asignacion.getCursoId());
        assertNull(asignacion.getEstudianteId());
        assertEquals(31L, asignacion.getObjetivoAprendizajeId());
        assertEquals(2, asignacion.getLimiteIntentosPrueba());

        Set<String> tablas = new HashSet<>();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(
                     "SELECT table_name FROM information_schema.tables WHERE table_schema = DATABASE()")) {
            while (result.next()) {
                tablas.add(result.getString(1));
            }
        }
        assertTrue(tablas.contains("asignacion"));
        assertTrue(tablas.contains("progreso"));
        assertTrue(tablas.contains("envio_actividad"));
        assertTrue(tablas.contains("intento_prueba"));
        assertTrue(tablas.contains("respuesta_estudiante"));
        assertTrue(tablas.contains("informe_pedagogico"));
        assertTrue(tablas.contains("validacion"));
        assertTrue(tablas.contains("observacion_docente"));
        assertTrue(tablas.contains("evento_sincronizado"));
    }
}
