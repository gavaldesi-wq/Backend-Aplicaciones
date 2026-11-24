package cl.mpazpro.actividades;

import ch.vorburger.mariadb4j.DB;
import ch.vorburger.mariadb4j.DBConfigurationBuilder;
import cl.mpazpro.actividades.model.ActividadDesafio;
import cl.mpazpro.actividades.model.EstadoContenido;
import cl.mpazpro.actividades.repository.ActividadDesafioRepository;
import cl.mpazpro.actividades.repository.AlternativaRepository;
import cl.mpazpro.actividades.repository.PruebaFinalRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class EsquemaActividadesTest {

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
            base.createDB("mpaz_actividades");
            registry.add("spring.datasource.url", () ->
                    "jdbc:mysql://127.0.0.1:" + config.getPort()
                            + "/mpaz_actividades?characterEncoding=utf8&connectionTimeZone=UTC&allowPublicKeyRetrieval=true&useSSL=false");
            registry.add("spring.datasource.username", () -> "root");
            registry.add("spring.datasource.password", () -> "");
            registry.add("spring.jpa.database-platform", () -> "org.hibernate.dialect.MariaDBDialect");
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
    private ActividadDesafioRepository actividades;

    @Autowired
    private AlternativaRepository alternativas;

    @Autowired
    private PruebaFinalRepository pruebas;

    @Test
    void creaLasTablasYCargaLosDatosDelContrato() {
        ActividadDesafio actividad = actividades.findById(40L).orElseThrow();
        assertEquals("Práctica de idea principal", actividad.getTitulo());
        assertEquals(EstadoContenido.PUBLICADO, actividad.getEstado());
        assertEquals(31L, actividad.getObjetivoAprendizajeId());
        assertEquals(3, alternativas.findByPregunta_IdOrderByOrdenAsc(301L).size());
        assertTrue(alternativas.findByPregunta_IdOrderByOrdenAsc(301L).get(1).isEsCorrecta());
        assertEquals(70L, pruebas.findById(70L).orElseThrow().getId());
    }
}
