package com.amzaki.catApp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * ─────────────────────────────────────────────────────────────
 * Spring Security Configuration
 * ─────────────────────────────────────────────────────────────
 *
 * This class defines WHO can access WHAT.
 *
 * Rules:
 *   GET  /api/cats  → anyone (no login required)
 *   POST /api/cats  → must have role USER or ADMIN
 *   DELETE /api/cats → must have role ADMIN only
 *
 * We also define two in-memory users for manual testing:
 *   - user  / password  → role: USER
 *   - admin / password  → role: ADMIN
 *
 * In tests, @WithMockUser bypasses UserDetailsService entirely,
 * but the SecurityFilterChain rules still apply.
 * ─────────────────────────────────────────────────────────────
 */
@Configuration
@EnableWebSecurity
@Profile("!test") // <-- Add this line
public class SecurityConfig {

    /**
     * The main security filter chain.
     * This is what Spring Security uses to decide if a request is allowed.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // ── CSRF ─────────────────────────────────────────────────────────
            // CSRF (Cross-Site Request Forgery) protection is ON by default.
            // For REST APIs (stateless), it's common to disable it.
            // CSRF only matters for browser-based form submissions with sessions.
            // Since we use STATELESS + Basic Auth, there is no session to hijack.
            .csrf(csrf -> csrf.disable())    // disabled for REST API / Postman usage

            // ── Session Management ────────────────────────────────────────────
            // STATELESS = no HTTP session. Every request must carry credentials.
            // This is standard for REST APIs.
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // ── Authorization Rules ───────────────────────────────────────────
            .authorizeHttpRequests(auth -> auth
                // Rule 1: GET /api/cats is public — no authentication needed
                .requestMatchers(HttpMethod.GET, "/api/cats").permitAll()

                // Rule 2: POST /api/cats requires USER or ADMIN role
                .requestMatchers(HttpMethod.POST, "/api/cats").hasAnyRole("USER", "ADMIN")

                // Rule 3: DELETE /api/cats requires ADMIN role only
                .requestMatchers(HttpMethod.DELETE, "/api/cats").hasRole("ADMIN")

                // Rule 4: anything else requires authentication
                .anyRequest().authenticated()
            )

            // ── HTTP Basic Auth ───────────────────────────────────────────────
            // Enables the "Authorization: Basic base64(user:pass)" header.
            // Used in tests with httpBasic() request post-processor.
            .httpBasic(org.springframework.security.config.Customizer.withDefaults());

        return http.build();
    }

    /**
     * Password encoder bean.
     * BCrypt is the standard secure hashing algorithm for passwords.
     * Always use an encoder — never store plain-text passwords!
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * In-memory users for demo/testing purposes.
     * In production you would load users from a database.
     *
     * Users:
     *   user  / password → ROLE_USER
     *   admin / password → ROLE_ADMIN
     */
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        var regularUser = User.builder()
                .username("user")
                .password(passwordEncoder.encode("password"))
                .roles("USER")   // Spring internally stores this as "ROLE_USER"
                .build();

        var adminUser = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("password"))
                .roles("ADMIN")  // Spring internally stores this as "ROLE_ADMIN"
                .build();

        return new InMemoryUserDetailsManager(regularUser, adminUser);
    }
}
