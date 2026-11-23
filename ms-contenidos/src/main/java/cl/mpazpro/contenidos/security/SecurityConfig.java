package cl.mpazpro.contenidos.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtProvider jwtProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        return http
                .csrf(csrf -> csrf.disable())
                .httpBasic(basic -> basic.disable())
                .formLogin(form -> form.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/actuator/health",
                                "/api/v1/health",
                                "/error"
                        ).permitAll()

                        .requestMatchers(
                                "/api/v1/utp",
                                "/api/v1/utp/**"
                        ).hasRole("UTP")

                        .requestMatchers(
                                "/api/v1/estudiante",
                                "/api/v1/estudiante/**"
                        ).hasRole("ESTUDIANTE")

                        .requestMatchers(
                                HttpMethod.POST, "/api/v1/**"
                        ).hasRole("UTP")

                        .requestMatchers(
                                HttpMethod.PUT, "/api/v1/**"
                        ).hasRole("UTP")

                        .requestMatchers(
                                HttpMethod.PATCH, "/api/v1/**"
                        ).hasRole("UTP")

                        .requestMatchers(
                                HttpMethod.DELETE, "/api/v1/**"
                        ).hasRole("UTP")

                        .anyRequest().authenticated()
                )

                .addFilterBefore(
                        new JwtAuthenticationFilter(jwtProvider),
                        UsernamePasswordAuthenticationFilter.class
                )

                .build();
    }
}