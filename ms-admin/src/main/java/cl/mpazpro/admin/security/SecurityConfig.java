
package cl.mpazpro.admin.security;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
            .csrf(AbstractHttpConfigurer::disable)

            .httpBasic(AbstractHttpConfigurer::disable)

            .formLogin(AbstractHttpConfigurer::disable)

            .sessionManagement(sesion ->
                    sesion.sessionCreationPolicy(
                            SessionCreationPolicy.STATELESS
                    )
            )

            .exceptionHandling(excepciones -> excepciones
                    .authenticationEntryPoint(
                            (request, response, error) ->
                                    response.setStatus(
                                            HttpServletResponse.SC_UNAUTHORIZED
                                    )
                    )
            )

            .authorizeHttpRequests(reglas -> reglas

                    
                    .requestMatchers(
                            "/api/v1/auth/demo-login"
                    ).permitAll()

                    
                    .requestMatchers(
                            "/actuator/health",
                            "/api/v1/health",
                            "/error"
                    ).permitAll()

                    
                    .requestMatchers(
                            "/api/v1/auth/me"
                    ).authenticated()

                    
                    .requestMatchers(
                            "/api/v1/utp/**",
                            "/api/v1/usuarios/**"
                    ).hasRole("UTP")

                   
                    .requestMatchers(
                            "/api/v1/docente/**"
                    ).hasRole("DOCENTE")

                    
                    .requestMatchers(
                            "/api/v1/estudiante/**"
                    ).hasRole("ESTUDIANTE")

                    
                    .anyRequest().denyAll()
            )

            .addFilterBefore(
                    jwtAuthenticationFilter,
                    UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}
