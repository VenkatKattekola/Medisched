package org.example.medisched.web.config;

import org.example.medisched.web.security.jwt.AuthEntryPointJwt;
import org.example.medisched.web.security.jwt.AuthTokenFilter;
import org.example.medisched.service.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod; // Make sure this is imported
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)// Enables method-level security with @PreAuthorize, @PostAuthorize, etc.
public class SecurityConfig {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // --- DIAGNOSTIC CHANGE: PERMIT ALL POSTS TO /api/auth/** DIRECTLY AT THE TOP ---
                        .requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll() // <--- ADD THIS LINE AND ENSURE IT'S NEAR THE TOP
                        // --- END DIAGNOSTIC CHANGE ---

                        // Your existing specific permitAll for register/login can stay, but this broader one should catch it
                        .requestMatchers(HttpMethod.POST, "/api/auth/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()

                        // For basic testing/health checks
                        .requestMatchers("/api/test/**").permitAll()

                        // Allow public access to doctor listing etc.
                        .requestMatchers(HttpMethod.GET, "/api/doctors").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/doctors/{id}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/availabilities/doctor/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/patients").permitAll()

                        // Protected endpoints
                        .requestMatchers(HttpMethod.POST, "/api/appointments/book/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/patients/{patientId}/appointments").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/patients/{id}").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/appointments/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/appointments/**").authenticated()

                        .anyRequest().authenticated() // All other requests require authentication
                );

        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
