package com.example.civic_fix.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(
            "http://localhost:5173",
            "http://localhost:3000"
        ));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth -> auth
                // ── Public ────────────────────────────────────────────────
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/error").permitAll()
                .requestMatchers("/uploads/**").permitAll()

                // ── Issues: all authenticated users can READ issues ───────
                // Citizens need GET /api/issues for Civic Feed
                .requestMatchers(HttpMethod.GET,  "/api/issues").authenticated()
                .requestMatchers(HttpMethod.GET,  "/api/issues/**").authenticated()
                .requestMatchers(HttpMethod.POST, "/api/issues").hasRole("CITIZEN")

                // ── Attachments ───────────────────────────────────────────
                .requestMatchers(HttpMethod.GET,  "/api/attachments/**").authenticated()
                .requestMatchers(HttpMethod.POST, "/api/attachments").hasAnyRole("CITIZEN", "OFFICER", "ADMIN")

                // ── Issue updates ─────────────────────────────────────────
                .requestMatchers(HttpMethod.GET,  "/api/issue-updates/**").authenticated()
                .requestMatchers(HttpMethod.POST, "/api/issue-updates").hasAnyRole("OFFICER", "ADMIN")

                // ── Departments & Categories: all authenticated can read ───
                .requestMatchers(HttpMethod.GET,  "/api/departments/**").authenticated()
                .requestMatchers(HttpMethod.GET,  "/api/issue-categories/**").authenticated()
                .requestMatchers(HttpMethod.POST, "/api/departments").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT,  "/api/departments/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE,"/api/departments/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/issue-categories").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT,  "/api/issue-categories/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE,"/api/issue-categories/**").hasRole("ADMIN")

                // ── Admin assign ──────────────────────────────────────────
                .requestMatchers(HttpMethod.POST, "/api/issues/assign").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/issues/bulk-assign").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET,  "/api/issues/unassigned").hasRole("ADMIN")

                // ── Resolve: officer or admin ─────────────────────────────
                .requestMatchers(HttpMethod.PUT,  "/api/issues/*/resolve").hasAnyRole("OFFICER", "ADMIN")

                // ── Notifications ─────────────────────────────────────────
                .requestMatchers("/api/notifications/**").authenticated()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")

                // ── Everything else needs auth ────────────────────────────
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}