package com.example.ecommerce_app.Config;


import com.example.ecommerce_app.Filters.JwtAuthFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.NullSecurityContextRepository;
import org.springframework.security.web.savedrequest.NullRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;

@EnableWebSecurity
@Configuration
@AllArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        RequestCache nullRequestCache = new NullRequestCache();

        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .sessionManagement((htc) -> htc.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore( jwtAuthFilter , UsernamePasswordAuthenticationFilter.class)
                        .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/api/vendor/**").hasRole("VENDOR")
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                                        .anyRequest().permitAll()
                        );
                      return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
