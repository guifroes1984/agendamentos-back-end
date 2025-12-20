package com.guifroes1984.agendamentos.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth -> auth
                // 🔓 Endpoints públicos
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/init/**").permitAll()
                
                // 📚 Swagger/OpenAPI
                .requestMatchers(
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/swagger-ui.html",
                    "/webjars/**",
                    "/swagger-resources/**"
                ).permitAll()
                
                // 🏥 Health check
                .requestMatchers("/health", "/actuator/health").permitAll()
                
                // 📝 Serviços públicos
                .requestMatchers(HttpMethod.GET, "/api/servicos/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/profissionais/disponiveis").permitAll()
                
                // 👑 Admin only
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/**").hasRole("ADMIN")
                
                // 💅 Profissional
                .requestMatchers("/api/profissionais/me/**").hasRole("PROFISSIONAL")
                .requestMatchers(HttpMethod.GET, "/api/profissionais/**").hasAnyRole("PROFISSIONAL", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/profissionais/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/profissionais/**").hasAnyRole("PROFISSIONAL", "ADMIN")
                
                // 👤 Cliente
                .requestMatchers("/api/clientes/me/**").hasRole("CLIENTE")
                
                // 📅 Agendamentos
                .requestMatchers(HttpMethod.GET, "/api/agendamentos/**").hasAnyRole("CLIENTE", "PROFISSIONAL", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/agendamentos/**").hasAnyRole("CLIENTE", "ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/agendamentos/**").hasAnyRole("CLIENTE", "PROFISSIONAL", "ADMIN")
                
                // 🔒 Outros endpoints exigem autenticação
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
            "http://localhost:3000",
            "http://localhost:4200",
            "http://localhost:5173",
            "http://localhost:8080"
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
