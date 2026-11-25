package cl.mpazpro.progreso.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtProvider jwtProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .httpBasic(basic -> basic.disable())
                .formLogin(form -> form.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/estudiante/inicio").hasRole("ESTUDIANTE")
                        .requestMatchers("/api/v1/estudiante/objetivos/*/progreso").hasRole("ESTUDIANTE")
                        .requestMatchers("/api/v1/estudiante/objetivos/*/prueba-final").hasRole("ESTUDIANTE")
                        .requestMatchers(HttpMethod.POST, "/api/v1/estudiante/pruebas/*/intentos").hasRole("ESTUDIANTE")
                        .requestMatchers(HttpMethod.GET, "/api/v1/estudiante/intentos/*/preguntas").hasRole("ESTUDIANTE")
                        .requestMatchers(HttpMethod.POST, "/api/v1/estudiante/intentos/*/entrega").hasRole("ESTUDIANTE")
                        .requestMatchers(HttpMethod.POST, "/api/v1/estudiante/actividades/*/respuestas").hasRole("ESTUDIANTE")
                        .requestMatchers(HttpMethod.POST, "/api/v1/estudiante/progreso/eventos").hasRole("ESTUDIANTE")
                        .requestMatchers(HttpMethod.POST, "/api/v1/sincronizacion/eventos").hasRole("ESTUDIANTE")
                        .requestMatchers("/api/v1/docente/asignaciones", "/api/v1/docente/asignaciones/**").hasRole("DOCENTE")
                        .requestMatchers(HttpMethod.GET, "/api/v1/docente/progreso").hasRole("DOCENTE")
                        .requestMatchers(HttpMethod.GET, "/api/v1/docente/estudiantes/*/resultados").hasRole("DOCENTE")
                        .requestMatchers("/api/v1/docente/informes", "/api/v1/docente/informes/**").hasRole("DOCENTE")
                        .requestMatchers(HttpMethod.GET, "/api/v1/docente/objetivos-disponibles").hasRole("DOCENTE")
                        .anyRequest().permitAll()
                )
                .addFilterBefore(
                        new JwtAuthenticationFilter(jwtProvider),
                        UsernamePasswordAuthenticationFilter.class
                )
                .build();
    }
}